(ns rhinocratic.deltiologue.db.queries.stamps
  (:require
   [clojure.string :as string]
   [next.jdbc :as jdbc]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]))

(defn stamps
  [conn]
  (let [sql (-> (h/select :s/stamp-description)
                (h/from [:stamp :s])
                (sql/format))]
    (jdbc/execute! conn sql)))
