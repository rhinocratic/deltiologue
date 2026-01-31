(ns rhinocratic.deltiologue.db.queries.cards
  (:require
   [clojure.string :as string]
   [next.jdbc :as jdbc]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]
   [clojure.datafy :as dat]
   [clojure.core.protocols :as prot]))

;; Extend the Datafiable protocol to the objects returned in Postgres' geospatial columns
(extend-protocol prot/Datafiable
  net.postgis.jdbc.geometry.Point
  (datafy [p]
    {:lat (.y p)
     :lng (.x p)})
  net.postgis.jdbc.PGgeography
  (datafy [g]
    (dat/datafy (.getGeometry g))))

(defn- card-tags
  "Fetch all tags associated with a card"
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

(defn- card-stamps
  "Fetch all stamps associated with a card"
  [conn card-id]
  (let [sql (-> (h/select :s/stamp-description
                          :ps/stamp-condition)
                (h/from [:postcard-stamp :ps])
                (h/left-join [:stamp :s] [:= :ps.stamp-id :s.id])
                (h/where [:= :ps.postcard-id card-id])
                (sql/format))]
    (jdbc/execute! conn sql)))

(defn get-card
  "Fetch a card by ID"
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
        card-detail (-> (jdbc/execute-one! conn sql)
                        (update :postcard/subject_location dat/datafy)
                        (update :recipient/recipient_location dat/datafy))
        tags (card-tags conn card-id)
        stamps (card-stamps conn card-id)]
    (-> (assoc card-detail
               :tags tags
               :stamps stamps))))

(defn- new-card
  "Create a new card"
  [conn card]
  (let [sql (-> (h/insert-into :note)
                (h/values [card])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(defn- update-card
  "Update an existing card"
  [conn card]
  (let [sql (-> (h/update [:postcard :p])
                (h/set (dissoc card :id))
                (h/where [:= :p/id (:id card)])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(defn save-card
  "Save a card"
  [conn card]
  (if (:id card)
    (update-card card)
    (new-card card)))

(defn delete-card
  "Delete a card"
  [conn card-id]
  (let [sql (-> (h/delete-from :postcard)
                (h/where [:= :id card-id])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql)))