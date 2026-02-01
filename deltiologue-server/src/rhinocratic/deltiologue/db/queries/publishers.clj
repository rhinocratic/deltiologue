(ns rhinocratic.deltiologue.db.queries.publishers
  (:require
   [clojure.string :as string]
   [next.jdbc :as jdbc]
   [next.jdbc.result-set :as rs]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]))

(def opts {:builder-fn rs/as-unqualified-maps})

(defn all-publishers
  "Fetch all publishers"
  [conn]
  (let [sql (-> (h/select :p/id
                          :p/publisher-name)
                (h/from [:publisher :p])
                (sql/format))]
    (jdbc/execute! conn sql opts)))

(defn get-publisher
  "Fetch a publisher by ID"
  [conn publisher-id]
  (let [sql (-> (h/select :p/id
                          :p/publisher-name)
                (h/from [:publisher :p])
                (h/where [:= :p/id publisher-id])
                (sql/format))]
    (jdbc/execute-one! conn sql opts)))

(defn- new-publisher
  "Create a new publisher"
  [conn publisher]
  (let [sql (-> (h/insert-into :publisher)
                (h/values [publisher])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql opts)))

(defn- update-publisher
  "Update an existing publisher"
  [conn publisher]
  (let [sql (-> (h/update [:publisher :p])
                (h/set (dissoc publisher :id))
                (h/where [:= :n/id (:id publisher)])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql opts)))

(defn save-publisher
  "Save a publisher"
  [conn publisher]
  (if (:id publisher)
    (update-publisher conn publisher)
    (new-publisher conn publisher)))

(defn delete-publisher
  "Delete a publisher"
  [conn publisher-id]
  (let [sql (-> (h/delete-from :publisher)
                (h/where [:= :id publisher-id])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql opts)))

