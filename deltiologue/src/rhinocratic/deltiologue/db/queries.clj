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

(defn all-card-summaries
  [conn]
  (let [sql (-> (h/select :p/collection-index
                          :p/subject-description)
                (h/from [:postcard :p])
                (sql/format))]
    (jdbc/execute! conn sql {:builder-fn rs/as-unqualified-maps})))

(defn card-by-number
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
                          :p/image-thumb-alt
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
                (sql/format))]
    (-> (jdbc/execute-one! conn sql {:builder-fn rs/as-unqualified-maps})
        (update :subject_location d/datafy)
        (update :recipient_location d/datafy))))

(defn search
  [conn terms]
  (let [sql (-> (h/select :p/collection-index
                          :p/subject-description
                          :p/image-thumb
                          :p/image-thumb-alt)
                (h/from [:postcard :p] [[:to_tsquery terms] :ts])
                (h/where [pgo/atat :p/fts :ts])
                (sql/format))
        results (jdbc/execute! conn sql {:builder-fn rs/as-unqualified-maps})]
    (tap> results)
    results))

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