(ns rhinocratic.deltiologue.db.queries.search
  (:require
   [clojure.string :as string]
   [next.jdbc :as jdbc]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]
   [honey.sql.pg-ops :as pgo]))

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


(comment

  (let [conn (->> (user/system) :rhinocratic.deltiologue/db)
        sql (-> (h/select :p/collection-index
                          :p/subject-description
                          :p/notes)
                (h/from [:postcard :p] [[:to_tsquery "sail"] :ts])
                (h/where [pgo/atat :p/fts :ts])
                (sql/format))]
    (-> (jdbc/execute! conn sql)))

  #_())


