(ns rhinocratic.deltiologue-migration.old-version.transform
  "Transform and cleanse the spreadsheet values into what's needed for insertion into the DB"
  (:require
   [clojure.string :as string]
   [clojure.instant :as inst]
   [rhinocratic.deltiologue-migration.old-version.images :as images]))

(defn includes-lower?
  [s substring]
  (-> s
      string/lower-case
      (string/includes? (string/lower-case substring))))

(defn empty-or-includes-any?
  [subs]
  (let [preds (concat [(fn [v] (string/blank? v))]
                      (mapv (fn [sub]
                              (fn [v]
                                (includes-lower? v sub))) subs))]
    (apply some-fn preds)))

(defn empty-or-matches-any?
  [regexes]
  (let [preds (concat [(fn [v] (string/blank? v))]
                      (mapv (fn [regex]
                              (fn [v]
                                (re-matches regex v))) regexes))]
    (apply some-fn preds)))

(defn trim-lower
  [s]
  (->> s
       (string/trim)
       (string/lower-case)))

(defn yes-no->boolean
  "Convert a yes/no string to a boolean"
  [s]
  (not (or (string/blank? s) (= (trim-lower s) "no"))))

(defn tokenize
  "Tokenize a newline and/or ; separated string"
  [s]
  (if s
    (->> s
         (#(string/replace % #"\n+" ""))
         (#(string/split % #";+"))
         (map string/trim)
         (remove string/blank?)
         vec)
    []))

(def months
  "Mappings of month names to integers, taking account of spelling errors"
  (->
   (zipmap
    ["january" "february" "march" "april" "may" "june" "july" "august" "september" "october" "november" "december"]
    (range 1 13))
   (assoc "sepember" 9)
   (assoc "aug" 8)
   (assoc "apri" 4)))

(defn possibly-approximate-date
  "Parse a date, recording whether or not it is approximate.
   Create separate fields for year, month, day and (to the greatest accuracy possible)
   the instant"
  [date]
  (if (and date
           (not (re-find #"\?+" (str date)))
           (not (re-find #"NK" (str date)))
           (not (re-find #"Unfranked" (str date)))
           (not (re-find #"Not known" (str date)))
           (not (re-find #"Undated" (str date))))
    (let [date-str (str date)
          [_ approx yr] (re-find #"([cC]\s*)?(\d{4})" date-str)
          [_ _ d _ m y] (re-find #"((\d{1,2})\s+)?((\w+)\s+)?(\d{4})" date-str)
          y (parse-long (or y yr "1999"))
          _ (when (= y "1999")
              (println "Rogue date:" date))
          m (months (if m (string/lower-case m) nil))
          d (when d (parse-long d))
          instant (cond
                    (and y m d) (inst/read-instant-date (format "%04d-%02d-%02d" y m d))
                    y (inst/read-instant-date (str y)))]
      (when-not (zero? y)
        {:approximate (or (some? approx) (nil? d) (nil? m))
         :year y
         :month m
         :day d
         :instant instant}))
    {:approximate false
     :year nil
     :month nil
     :day nil
     :instant nil}))

(defn iframe-src
  "Find the string specifying the \"src\" attribute of the Google Maps iframe"
  [iframe]
  (when iframe
    (last (re-find #"src=\"([^\"]+)\"" iframe))))

(defn iframe->wkt
  [google-map-ref]
  (when (not (string/blank? google-map-ref))
    (let [lat (last (re-find #"!1d([^!]+)!" google-map-ref))
          lon (last (re-find #"!2d([^!]+)!" google-map-ref))]
      (when-not (or (nil? lat) (nil? lon))
        [:ST_GeogFromText (str "SRID=4326;POINT(" lon " " lat ")")]))))

(defn area
  "Process the :area field from the spreadsheet, splitting it into primary and secondary location components.
   Also deal with an anomalous entry."
  [area-str]
  (if (string/blank? area-str)
    {:primary-area nil :secondary-area nil}
    (let [[_ weird] (re-matches #"[^;]+;\n(.*);" area-str)
          components (if weird
                       [(-> weird
                            (string/replace ";" "")
                            (string/trim)
                            (string/capitalize))]
                       (->> (string/split area-str #"\s+-\s+")
                            (map #(string/replace % ";" ""))
                            (map string/trim)
                            (map string/capitalize)))
          components (concat (vec components) (repeat 2 nil))]
      (zipmap [:primary-area :secondary-area] components))))

(defn lower-includes?
  [s substring]
  (string/includes? (string/lower-case s) substring))

(def link-regex  #"<span class=[”\"]popup\s+([^”\"]+)[”\"]>([^<]+)<\/span>")

(defn note-idx
  [note-lookup note-key]
  (->> note-lookup
       (filter #(string/includes? (key %) note-key))
       first
       second))

(defn new-link
  [note-lookup [original note-key link-text :as match]]
  (let [note-key (string/replace note-key "morecambe-pavilion" "the-tower-pavilion")
        found-idx (note-idx note-lookup note-key)]
    (str "<a href=\"/api/notes/" found-idx "\">" link-text "</a>")))

(defn replace-links
  [note-lookup text]
  (string/replace text link-regex #(new-link note-lookup %)))

(defn transform-field-dispatch
  [_ctx _m k _v]
  k)

(defmulti transform-field transform-field-dispatch)

(defmethod transform-field :no
  [_ m _k v]
  (assoc m :collection-index (int v)))

(defmethod transform-field :notes
  [note-lookup m _k v]
  (if (string/blank? v)
    (assoc m :notes nil)
    (assoc m :notes (replace-links note-lookup v))))

(defmethod transform-field :used
  [_ m _k v]
  (let [used (yes-no->boolean v)
        posted (and used (not (string/includes? (string/lower-case v) "not posted")))]
    (assoc m
           :used used
           :posted posted)))

(defmethod transform-field :divided-back-post-1902
  [_ m _k v]
  (assoc m :divided-back (yes-no->boolean v)))

(defmethod transform-field :subject
  [_ m _k v]
  (assoc m :subject-description (string/trim v)))

(defmethod transform-field :printed
  [_ m _k v]
  (assoc m :printed (yes-no->boolean v)))

(defmethod transform-field :location
  [_ m _k v]
  (assoc m :location (tokenize v)))

(defmethod transform-field :date-posted
  [_ m _k v]
  (let [date-str (if (nil? v) nil (str v))]
    (if (or
         (string/blank? date-str)
         (string/includes? (string/lower-case date-str) "not franked"))
      (assoc m
             :posted-year nil
             :posted-month nil
             :posted-day nil
             :posted-date nil
             :posted-date-approximate false
             :franked false)
      (let [{:keys [approximate year month day instant]} (possibly-approximate-date date-str)]
        (assoc m
               :posted-year year
               :posted-month month
               :posted-day day
               :posted-date instant
               :posted-date-approximate approximate
               :franked true)))))

(defmethod transform-field :image-date
  [_ m _k v]
  (let [date-str (if (nil? v) nil (str v))]
    (if (string/blank? date-str)
      (assoc m
             :publication-year nil
             :publication-month nil
             :publication-day nil
             :publication-date nil
             :publication-date-approximate false)
      (let [{:keys [approximate year month day instant]} (possibly-approximate-date date-str)]
        (assoc m
               :publication-year year
               :publication-month month
               :publication-day day
               :publication-date instant
               :publication-date-approximate approximate)))))

(defmethod transform-field :google-map-ref
  [_ m _k v]
  (if (string/blank? v)
    (assoc m
           :subject-current-view nil
           :subject-location nil)
    (assoc m
           :subject-current-view (iframe-src v)
           :subject-location (iframe->wkt v))))

(defmethod transform-field :area
  [_ m _k v]
  (merge m (area v)))

(defmethod transform-field :currently
  [_ m _k v]
  (if (string/blank? v)
    (assoc m :current-view-description nil)
    (assoc m :current-view-description v)))

(defmethod transform-field :recipient
  [_ m _k v]
  (if (string/blank? v)
    (assoc m :recipient-name nil)
    (assoc m :recipient-name v)))

(defmethod transform-field :events
  [_ m _k v]
  (assoc m :events (tokenize v)))

(defmethod transform-field :other-keywords
  [_ m _k v]
  (assoc m :other-tags (tokenize v)))

(defmethod transform-field :work
  [_ m _k v]
  (assoc m :work (tokenize v)))

(defmethod transform-field :transport
  [_ m _k v]
  (assoc m :transport (tokenize v)))

(defmethod transform-field :notable-buildings
  [_ m _k v]
  (assoc m :notable-buildings (tokenize v)))

(defmethod transform-field :rp
  [_ m _k v]
  (assoc m :rp (yes-no->boolean v)))

(defmethod transform-field :publisher
  [_ m _k v]
  (let [remove? (empty-or-matches-any? [#"^\s*[Nn]ot known?\s*$"])]
    (if (remove? v)
      (assoc m
             :publisher-name nil
             :series-name nil)
      (assoc m
             :publisher-name v
             :series-name v))))

(defmethod transform-field :stamp-attached
  [_ m _k v]
  (merge m
         (cond
           (string/blank? v) {:stamp-attached false :stamp-condition [:stamp_condition "absent"] :stamp-description nil}
           (lower-includes? v "partially removed") {:stamp-attached false :stamp-condition [:stamp_condition "partially removed"] :stamp-description (-> v
                                                                                                                                                         (string/replace "Partially removed:" "")
                                                                                                                                                         string/trim)}
           (lower-includes? v "removed") {:stamp-attached false :stamp-condition [:stamp_condition "removed"] :stamp-description nil}
           (lower-includes? v "(badly damaged)") {:stamp-attached true :stamp-condition [:stamp_condition "badly damaged"] :stamp-description (-> v
                                                                                                                                                  (string/replace "(badly damaged)" "")
                                                                                                                                                  (string/replace "Yes:" "")
                                                                                                                                                  string/trim)}
           (lower-includes? v "damaged") {:stamp-attached true :stamp-condition [:stamp_condition "damaged"] :stamp-description v}
           (or (lower-includes? v "yes:")
               (lower-includes? v "yes;")) (let [description (string/trim (last (re-find #"[Yy]es[:;](.*)" v)))]
                                             {:stamp-attached true :stamp-condition [:stamp_condition "intact"] :stamp-description (-> description
                                                                                                                                       (string/replace  "1Edward" "Edward")
                                                                                                                                       (string/replace "jubillee" "jubilee")
                                                                                                                                       (string/replace #"(\w)\s*\(" "$1 ("))})
           :else {:stamp-attached false :stamp-condition [:stamp_condition "absent"] :stamp-description nil})))

(defmethod transform-field :misc
  [_ m _k v]
  (assoc m :misc (tokenize v)))

(defmethod transform-field :references
  [_ m _k v]
  (if (string/blank? v)
    (assoc m :references nil)
    (assoc m :references v)))

(defmethod transform-field :alt-text
  [_ m _k v]
  (assoc m :image-front-alt v))

(defmethod transform-field :default
  [_ m _k _v]
  m)

(defn transform-cards
  [rows note-data]
  (->> rows
       (map #(reduce-kv (partial transform-field note-data) {} %))
       (map-indexed #(assoc %2 :postcard-id %1))
       (map-indexed #(assoc %2
                            :image-front (* 3 %1)
                            :image-rear (+ 1 (* 3 %1))
                            :image-rear-alt ""
                            :image-thumb (+ 2 (* 3 %1))
                            :transcript ""))))


(defn make-note-lookup
  [{:keys [note]}]
  (->> note
       (map :title)
       (map #(-> (string/lower-case %)
                 (string/replace #"[,–’/]" "")
                 (string/replace #"\s+" "-")))
       (map-indexed #(vector %2 %1))
       (into {})))

(defn transform
  [{:keys [card] :as data}]
  (let [note-lookup (make-note-lookup data)
        images (images/image-table)]
    (-> data
        (update :card transform-cards note-lookup)
        (assoc :image images))))

(comment

  (require '[rhinocratic.deltiologue-migration.old-version.spreadsheet :as spread]
           '[rhinocratic.deltiologue-migration.old-version.notes :as notes])

  (let [spread (spread/parse)
        nts (notes/parse)
        merged (merge spread nts)
        transformed (transform merged)]
    (tap> (-> (:card transformed)
              (nth 647)
              :posted-date)))

  (tap> (images/image-table))

  #_())