(ns rhinocratic.deltiologue.db.queries.series
  (:require
   [clojure.string :as string]
   [next.jdbc :as jdbc]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]))

(defn all-series
  "Fetch all series"
  [conn]
  (let [sql (-> (h/select :s/id
                          :s/series-name)
                (h/from [:series :s])
                (sql/format))]
    (jdbc/execute! conn sql)))

(defn get-series
  "Fetch a series by ID"
  [conn series-id]
  (let [sql (-> (h/select :s/id
                          :s/series-name)
                (h/from [:series :s])
                (h/where [:= :s/id series-id])
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(defn new-series
  "Create a new series"
  [conn series]
  (let [sql (-> (h/insert-into :series)
                (h/values [series])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(defn update-series
  "Update an existing series"
  [conn series]
  (let [sql (-> (h/update [:series :s])
                (h/set (dissoc series :id))
                (h/where [:= :r/id (:id series)])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(defn save-series
  "Save a series"
  [conn series]
  (if (:id series)
    (update-series series)
    (new-series series)))

(defn delete-series
  "Delete a series"
  [conn series-id]
  (let [sql (-> (h/delete-from :series)
                (h/where [:= :id series-id])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql)))