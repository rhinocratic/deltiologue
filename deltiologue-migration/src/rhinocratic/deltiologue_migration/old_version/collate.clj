(ns rhinocratic.deltiologue-migration.old-version.collate
  "Aggregate the information from individual spreadsheet rows into separate maps for each table, creating foreign key links where needed"
  (:require
   [clojure.set :as set]
   [clojure.string :as string]))

(defn rename-fields
  [rename-ks rows]
  (map #(set/rename-keys % rename-ks) rows))

(defn index-by
  [group-key index-key rows]
  (->> rows
       (group-by group-key)
       (map-indexed (fn [idx [_ vs]] (map #(assoc % index-key idx) vs)))
       (apply concat)))

(defn dedupe-rows
  [dedupe-key rows]
  (->> rows
       (sort-by dedupe-key)
       (partition-by dedupe-key)
       (map first)))

(defn project
  [projection-keys rows]
  (map #(select-keys % projection-keys) rows))

(defn link-to-postcards
  [rows dest-table link-table join-key index-key & other-keys]
  (let [indexed (->> rows
                     (map-indexed #(assoc %2 :postcard-id %1))
                     (remove #(nil? (join-key %)))
                     (project (concat [:postcard-id join-key] other-keys))
                     (index-by join-key index-key))]
    {dest-table (->> indexed
                     (dedupe-rows join-key)
                     (sort-by index-key)
                     (project [join-key]))
     link-table (->> indexed
                     (project (concat [:postcard-id index-key] other-keys))
                     (sort-by :postcard-id))}))

(defn canonical-tag
  [tag]
  (some-> tag
          string/trim
          (string/replace #"\s+" "_")
          (string/lower-case)))

(defn non-blank?
  [tag]
  (and
   (some? tag)
   (not (string/blank? tag))))

(defn extract-fields
  [result tag-key tag-values]
  (concat result (for [v (filter non-blank? tag-values)]
                   {:tag-category tag-key
                    :tag (canonical-tag v)
                    :tag-text (string/trim v)})))

(defn card-tags
  [id card]
  (-> (select-keys card [:events :location :other-tags :transport :work :notable-buildings :misc])
      (assoc :area-primary [(:area-primary card)])
      (assoc :area-secondary [(:area-secondary card)])
      (update :misc #(concat % (:other-tags card)))
      (dissoc :other-tags)
      (->> (reduce-kv extract-fields []))
      (->> (map #(assoc % :id id)))))

(defn merged-tags
  [cards]
  (let [rfn (fn [result {:keys [id tag tag-category tag-text]}]
              (-> result
                  (update-in [[tag-category tag] :cards] (fnil concat []) [id])
                  (update-in [[tag-category tag] :tag-text] (constantly tag-text))))]
    (->> cards
         (map-indexed card-tags)
         (apply concat)
         (reduce rfn {}))))

(defn make-series
  [rows collated]
  (let [series (->> rows
                    (map :series-name)
                    distinct
                    (filter some?)
                    (map-indexed #(vector %1 %2))
                    (into {}))
        lookup (reduce-kv (fn [m k v] (assoc m v k)) {} series)
        series-table (map #(hash-map :series-name %) (vals series))]
    (-> collated
        (update :postcard #(map (fn [card] (assoc card
                                                  :series (lookup (:series-name card))
                                                  :series-entry nil)) %))
        (update :postcard #(map (fn [card] (dissoc card :series-name)) %))
        (assoc :series series-table))))

(defn make-recipient
  [rows collated]
  (let [recipients (->> rows
                        (map :recipient-name)
                        distinct
                        (filter some?)
                        (map-indexed #(vector %1 %2))
                        (into (sorted-map)))
        lookup (reduce-kv (fn [m k v] (assoc m v k)) {} recipients)
        recipient-table (map #(hash-map :recipient-name %
                                        :recipient-address %
                                        :recipient-location nil) (vals recipients))]
    (-> collated
        (update :postcard #(map (fn [card] (assoc card
                                                  :recipient (lookup (:recipient-name card)))) %))
        (update :postcard #(map (fn [card] (dissoc card :recipient-name)) %))
        (assoc :recipient recipient-table))))

(defn make-publisher
  [rows collated]
  (let [publishers (->> rows
                        (map :publisher-name)
                        distinct
                        (filter some?)
                        (map-indexed #(vector %1 %2))
                        (into (sorted-map)))
        lookup (reduce-kv (fn [m k v] (assoc m v k)) {} publishers)
        publisher-table (->> publishers
                             vals
                             (map #(hash-map :publisher-name %)))]
    (-> collated
        (update :postcard #(map (fn [card] (assoc card
                                                  :publisher (lookup (:publisher-name card)))) %))
        (update :postcard #(map (fn [card] (dissoc card :publisher-name)) %))
        (assoc :publisher publisher-table))))

(defn collate-table-dispatch
  [_rows _m table]
  table)

(defmulti collate-table #'collate-table-dispatch)

(defmethod collate-table :postcard
  [rows m _table]
  (merge m {:postcard (project [:collection-index
                                :divided-back
                                :rp
                                :used
                                :posted
                                :franked
                                :image-front
                                :image-front-alt
                                :image-rear
                                :image-rear-alt
                                :image-thumb
                                :publication-year
                                :publication-month
                                :publication-day
                                :publication-date
                                :publication-date-approximate
                                :posted-year
                                :posted-month
                                :posted-day
                                :posted-date
                                :posted-date-approximate
                                :location-description
                                :subject-description
                                :subject-location
                                :subject-current-view
                                :notes
                                :transcript
                                :publisher
                                :recipient
                                :series
                                :publisher-name
                                :recipient-name
                                :series-name] rows)}))

(defmethod collate-table :series
  [rows m _table]
  (make-series rows m))

(defmethod collate-table :publisher
  [rows m _table]
  (make-publisher rows m))

(defmethod collate-table :recipient
  [rows m _table]
  (make-recipient rows m))

(defmethod collate-table :stamp
  [rows m _table]
  (merge m (link-to-postcards rows :stamp :postcard-stamp :stamp-description :stamp-id :stamp-condition)))

(defmethod collate-table :tag
  [rows m _table]
  (let [merged (merged-tags rows)
        tags (->> merged
                  (reduce (fn [result [[_category tag] {:keys [cards tag-text]}]]
                            (assoc result tag tag-text)) {})
                  (map #(zipmap [:tag-name :display-text] %)))
        tag-position (->> tags
                          (map-indexed #(vector (:tag-name %2) %1))
                          (into {}))
        tag-category-keys (->> merged
                               keys
                               (map first)
                               distinct)
        tag-category-position (->> tag-category-keys
                                   (map-indexed #(vector %2 %1))
                                   (into {}))
        cat-display {:notable-buildings "Notable Buildings"
                     :location "Location"
                     :area-primary "Primary Area"
                     :misc "Miscellaneous"
                     :transport "Transport"
                     :work "Work"
                     :events "Events"
                     :area-secondary "Secondary Area"}
        tag-categories (->> tag-category-keys
                            (mapv #(hash-map :category-name (string/replace (name %) "-" "_")
                                             :display-text (get cat-display %))))
        postcard-tags (->> merged
                           (map-indexed (fn [id [[category tag] {:keys [cards tag-text]}]]
                                          (map
                                           (fn [card-id]
                                             {:postcard-id card-id
                                              :tag-id (tag-position tag)
                                              :tag-category-id (tag-category-position category)})
                                           cards)))
                           (apply concat)
                           distinct)]
    (-> m
        (assoc :tag tags)
        (assoc :tag-category tag-categories)
        (assoc :postcard-tag postcard-tags))))

(defmethod collate-table :default
  [_rows m _table]
  m)

(defn collate
  [{:keys [card] :as data}]
  (let [card-tables [:postcard
                     :publisher
                     :recipient
                     :series
                     :stamp
                     :tag]
        collated-cards (reduce (partial collate-table card) {} card-tables)]
    (->> (assoc collated-cards :slideshow [{:postcard-id 42}
                                           {:postcard-id 455}])
         (merge (dissoc data :card)))))

(comment

  (require '[rhinocratic.deltiologue-migration.old-version.spreadsheet :as spreadsheet]
           '[rhinocratic.deltiologue-migration.old-version.notes :as notes]
           '[rhinocratic.deltiologue-migration.old-version.transform :as transform])

  (def m (->> (merge
               (spreadsheet/parse)
               (notes/parse)
               (transform/transform))))

  (tap> (merged-tags (:card m)))

  (tap> (collate m))

  (tap> (keys m))

  #_())
