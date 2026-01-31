(ns rhinocratic.deltiologue.db.queries.tags
  (:require
   [clojure.string :as string]
   [next.jdbc :as jdbc]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]))

(defn tags
  [conn]
  (let [sql (-> (h/select :t/tag-name
                          :t/display-text)
                (h/from [:tag :t])
                (sql/format))]
    (jdbc/execute! conn sql)))
