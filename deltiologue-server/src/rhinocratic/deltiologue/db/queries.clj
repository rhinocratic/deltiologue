(ns rhinocratic.deltiologue.db.queries
  (:require
   [clojure.string :as str]
   [next.jdbc :as jdbc]
   [next.jdbc.result-set :as rs]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]
   [honey.sql.pg-ops :as pgo]
   [clojure.datafy :as d]
   [clojure.core.protocols :as p]
   [com.brunobonacci.mulog :as u]))

(extend-protocol p/Datafiable
  net.postgis.jdbc.geometry.Point
  (datafy [p]
    {:lat (.y p)
     :lng (.x p)})
  net.postgis.jdbc.PGgeography
  (datafy [g]
    (d/datafy (.getGeometry g))))

(defn card-summary
  [conn]
  (let [sql (-> (h/select :p/collection-index
                          :p/subject-description)
                (h/from [:postcard :p])
                (sql/format))]
    (jdbc/execute! conn sql)))


(defn card-tags
  [conn card-id]
  (let [sql (-> (h/select :t/display-text
                          :t/tag-name
                          :tc/display-text
                          :tc/category-name)
                (h/from [:postcard-tag :pt])
                (h/left-join [:tag :t] [:= :pt.tag-id :t.id])
                (h/left-join [:tag-category :tc] [:= :pt.tag-category-id :tc.id])
                (h/where [:= :pt.postcard-id card-id])
                (sql/format))]
    (jdbc/execute! conn sql)))

(defn card-stamps
  [conn card-id]
  (let [sql (-> (h/select :s/stamp-description
                          :ps/stamp-condition)
                (h/from [:postcard-stamp :ps])
                (h/left-join [:stamp :s] [:= :ps.stamp-id :s.id])
                (h/where [:= :ps.postcard-id card-id])
                (sql/format))]
    (jdbc/execute! conn sql)))

(defn card
  [conn card-id]
  (let [sql (-> (h/select :p/collection-index
                          :p/divided-back
                          :p/rp
                          :p/used
                          :p/posted
                          :p/franked
                          :p/image-front
                          :p/image-front-alt
                          :p/image-rear
                          :p/image-rear_alt
                          :p/image-thumb
                          :p/publication-year
                          :p/publication-month
                          :p/publication-day
                          :p/publication-date
                          :p/publication-date-approximate
                          :p/posted-year
                          :p/posted-month
                          :p/posted-day
                          :p/posted-date
                          :p/posted-date-approximate
                          :p/subject-description
                          :p/subject-location
                          :p/subject-current-view
                          :p/notes
                          :p/transcript
                          :p/publisher
                          :p/recipient
                          :p/series
                          :pb/publisher-name
                          :r/recipient-name
                          :r/recipient-address
                          :r/recipient-location
                          :s/series-name
                          :p/series-entry)
                (h/from [:postcard :p])
                (h/left-join [:publisher :pb] [:= :p.publisher :pb.id])
                (h/left-join [:recipient :r] [:= :p.recipient :r.id])
                (h/left-join [:series :s] [:= :p.series :s.id])
                (h/where [:= :p/id card-id])
                (sql/format))
        card-detail (-> (jdbc/execute-one! conn sql {:builder-fn rs/as-maps})
                        (update :postcard/subject_location d/datafy)
                        (update :recipient/recipient_location d/datafy))
        tags (card-tags conn card-id)
        stamps (card-stamps conn card-id)]
    (-> (assoc card-detail
               :tags tags
               :stamps stamps))))

(defn search
  [conn terms]
  (let [sql (-> (h/select :p/collection-index
                          :p/subject-description
                          :p/image-thumb)
                (h/from [:postcard :p] [[:to_tsquery terms] :ts])
                (h/where [pgo/atat :p/fts :ts])
                (sql/format))
        results (jdbc/execute! conn sql)]
    {:results results
     :count (count results)}))

(defn note-summaries
  [conn]
  (let [sql (-> (h/select :n/id
                          :n/title)
                (h/from [:note :n])
                (sql/format))]
    (jdbc/execute! conn sql {:builder-fn rs/as-unqualified-maps})))

(defn note-references
  [conn note-id]
  (let [sql (-> (h/select :r/idx
                          :r/medium
                          :r/accessed
                          :r/source
                          :r/title
                          :r/issue-date
                          :r/issue-note
                          :r/available)
                (h/from [:reference :r])
                (h/join [:note-reference :nr] [:= :nr.reference-id :r.id])
                (h/where [:= :nr/note-id note-id])
                (sql/format))]
    (jdbc/execute! conn sql)))

(defn note
  [conn note-id]
  (let [sql (-> (h/select :n/title
                          :n/body)
                (h/from [:note :n])
                (h/where [:= :n/id note-id])
                (sql/format))
        note (jdbc/execute-one! conn sql)]
    (merge note {:references (note-references conn note-id)})))

(defn tags
  [conn]
  (let [sql (-> (h/select :t/tag-name
                          :t/display-text)
                (h/from [:tag :t])
                (sql/format))]
    (jdbc/execute! conn sql)))

(defn tag-categories
  [conn]
  (let [sql (-> (h/select :tc/id
                          :tc/display-text
                          :tc/category-name)
                (h/from [:tag-category :tc])
                (sql/format))]
    (jdbc/execute! conn sql)))

(defn tag-category
  [conn category-id]
  (let [sql (-> (h/select :tc/id
                          :tc/display-text
                          :tc/category-name)
                (h/from [:tag-category :tc])
                (h/where [:= :tc.id category-id])
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(defn new-tag-category
  [conn category]
  (let [category-name (-> category
                          :display-text
                          (str/trim)
                          (str/lower-case)
                          (str/replace #"\s+" "_"))
        category (assoc category :category-name category-name)
        sql (-> (h/insert-into :tag-category)
                (h/values [category])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(defn delete-tag-category
  [conn category-id]
  (let [sql (-> (h/delete-from :tag-category)
                (h/where [:= :id category-id])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(defn references
  [conn]
  (let [sql (-> (h/select :r/idx
                          :r/medium
                          :r/accessed
                          :r/source
                          :r/title
                          :r/issue-date
                          :r/issue-note
                          :r/available)
                (h/from [:reference :r])
                (sql/format))]
    (jdbc/execute! conn sql {:builder-fn rs/as-unqualified-maps})))

(defn stamps
  [conn]
  (let [sql (-> (h/select :s/stamp-description)
                (h/from [:stamp :s])
                (sql/format))]
    (jdbc/execute! conn sql)))

(comment

  (let [conn (:rhinocratic.deltiologue/db (user/system))]
    (tag-category conn 1))

  (let [conn (:rhinocratic.deltiologue/db (user/system))]
    (new-tag-category conn {:display-text "A further attempt"}))

  (let [conn (:rhinocratic.deltiologue/db (user/system))]
    (delete-tag-category conn 9))


  #_())



(comment

  (let [conn (->> (user/system) :rhinocratic.deltiologue/db)
        sql (-> (h/select :p/collection-index
                          :p/subject-description
                          :p/notes)
                (h/from [:postcard :p] [[:to_tsquery "sail"] :ts])
                (h/where [pgo/atat :p/fts :ts])
                (sql/format))]
    (-> (jdbc/execute! conn sql)))

  (let [conn (->> (user/system) :rhinocratic.deltiologue/db)]
    (tap> (-> (card-by-number conn 42)
              :subject_location)))


  #_())