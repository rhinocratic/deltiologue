(ns rhinocratic.deltiologue-migration.old-version.collate
  "Aggregate the information from individual spreadsheet rows into separate maps for each table, creating foreign key links where needed"
  (:require
   [clojure.set :as set]
   [clojure.string :as string]
   [rhinocratic.deltiologue-migration.old-version.content :as content]))

(defn rename-fields
  [rename-ks rows]
  (map #(set/rename-keys % rename-ks) rows))

(defn index-by
  [group-key index-key rows]
  (->> rows
       (group-by group-key)
       (map-indexed (fn [idx [_ vs]] (map #(assoc % index-key (inc idx)) vs)))
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
                     (map-indexed #(assoc %2 :postcard-id (inc %1)))
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
      (assoc :primary-area [(:primary-area card)])
      (assoc :secondary-area [(:secondary-area card)])
      (update :misc #(concat % (:other-tags card)))
      (dissoc :other-tags)
      (->> (reduce-kv extract-fields []))
      (->> (map #(assoc % :id (inc id))))))

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

(defn collate-table-dispatch
  [_rows _m table]
  table)

(defmulti collate-table #'collate-table-dispatch)

(defmethod collate-table :postcard
  [rows m _table]
  (merge m {:postcard (->> rows (project [:index
                                          :divided-back
                                          :rp
                                          :used
                                          :posted
                                          :franked
                                          :image-front-alt
                                          :image-rear-alt
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
                                          :series-id
                                          :publisher-id
                                          :publication-description
                                          :recipient-name
                                          :recipient-address
                                          :recipient-location
                                          :recipient-name
                                          :series-name])
                           (map #(assoc % :draft false)))}))

;; (defmethod collate-table :series
;;   [rows m _table]
;;   (make-series rows m))

;; (defmethod collate-table :publisher
;;   [rows m _table]
;;   (make-publisher rows m))

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
                          (map-indexed #(vector (:tag-name %2) (inc %1)))
                          (into {}))
        tag-category-keys (->> merged
                               keys
                               (map first)
                               distinct)
        tag-category-position (->> tag-category-keys
                                   (map-indexed #(vector %2 (inc %1)))
                                   (into {}))
        cat-display {:notable-buildings "Notable Buildings"
                     :location "Location"
                     :primary-area "Primary Area"
                     :misc "Miscellaneous"
                     :transport "Transport"
                     :work "Work"
                     :events "Events"
                     :secondary-area "Secondary Area"}
        cat-colours {:notable-buildings "eeeeff"
                     :location "eeffee"
                     :primary-area "ffeeee"
                     :misc "eeeeee"
                     :transport "ffeeff"
                     :work "ddddff"
                     :events "ddffff"
                     :secondary-area "ddffee"}
        tag-categories (->> tag-category-keys
                            (mapv #(hash-map :category-name (string/replace (name %) "-" "_")
                                             :display-text (get cat-display %)
                                             :colour (get cat-colours %))))
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

(defn slideshow
  []
  (let [slideshow-ids [2 14 15 16 17 19 22 24 28 36 37 50 51 54 55 56 57 66 84 96 97 100 102 128 130 131 132 138 142 144 146 159 162 163 164 173 178 179 181 183 190 191 207 210 218 221 223 225 229 230 232 238 243 246 247 249 252 253 260 262 263 277 278 279 285 286 288 290 291 294 303 307 310 311 318 327 332 333 335 337 339 340 341 349 352 353 358 360 362 368 372 376 384 385 389 391 395 400 401 410 413 417 422 430 438 439 441 444 445 446 455 457 459 460 461 465 466 468 479 480 482 485 494 495 496 497 504 512 522 531 534 537 541 545 545 546 547 550 551 553 554 555 558 568 569 571 575 576 577 578 579 580 581 582 583 584 585 586 588 589 591 592 598 610 615 618 626 628 629 641 642 643 645 646 648 651 652 653 654 656]]
    (mapv #(hash-map :postcard-id %) slideshow-ids)))

(defn collate
  [{:keys [card] :as data}]
  (let [card-tables [:postcard
                     :stamp
                     :tag]
        collated-cards (reduce (partial collate-table card) {} card-tables)]
    (-> collated-cards
        (assoc :slideshow (slideshow))
        (assoc :content (content/parse))
        (#(merge (dissoc data :card) %)))))

(comment

  (require '[rhinocratic.deltiologue-migration.old-version.spreadsheet :as spreadsheet]
           '[rhinocratic.deltiologue-migration.old-version.notes :as notes]
           '[rhinocratic.deltiologue-migration.old-version.transform :as transform]
           '[rhinocratic.deltiologue-migration.old-version.manual :as manual]
           '[rhinocratic.deltiologue-migration.old-version.collate :as collate])

  (let [rows (->> (merge (spreadsheet/parse) (notes/parse) (manual/tables))
                  transform/transform
                  collate/collate)]
    (tap> rows))

  (tap> (merged-tags (:card m)))

  (tap> (collate m))

  (tap> (keys m))

  #_())
