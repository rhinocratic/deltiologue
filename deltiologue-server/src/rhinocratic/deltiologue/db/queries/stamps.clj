(ns rhinocratic.deltiologue.db.queries.stamps
  (:require
   [clojure.string :as string]
   [next.jdbc :as jdbc]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]))

(defn all-stamps
  "Fetch all stamps"
  [conn]
  (let [sql (-> (h/select :s/stamp-description)
                (h/from [:stamp :s])
                (sql/format))]
    (jdbc/execute! conn sql)))

(defn get-stamp
  "Fetch a stamp by ID"
  [conn stamp-id]
  (let [sql (-> (h/select :s/stamp-description)
                (h/from [:stamp :s])
                (h/where [:= :s/id stamp-id])
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(defn- new-stamp
  "Create a new stamp"
  [conn stamp]
  (let [sql (-> (h/insert-into :stamp)
                (h/values [stamp])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(defn- update-stamp
  "Update an existing stamp"
  [conn stamp]
  (let [sql (-> (h/update [:stamp :s])
                (h/set (dissoc stamp :id))
                (h/where [:= :s/id (:id stamp)])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(defn save-stamp
  "Save a stamp"
  [conn stamp]
  (if (:id stamp)
    (update-stamp conn stamp)
    (new-stamp conn stamp)))

(defn delete-stamp
  "Delete a stamp"
  [conn stamp-id]
  (let [sql (-> (h/delete-from :stamp)
                (h/where [:= :id stamp-id])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(comment

  (let [conn (:rhinocratic.deltiologue/db (user/system))]
    (get-stamp conn 1))

  #_())

