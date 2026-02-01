(ns rhinocratic.deltiologue.db.queries.recipients
  (:require
   [clojure.string :as string]
   [next.jdbc :as jdbc]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]))

(defn all-recipients
  "Fetch all recipients"
  [conn]
  (let [sql (-> (h/select :r/id
                          :r/recipient-name
                          :r/recipient-address
                          :r/recipient-location)
                (h/from [:recipient :r])
                (sql/format))]
    (jdbc/execute! conn sql)))

(defn get-recipient
  "Fetch a recipient by ID"
  [conn recipient-id]
  (let [sql (-> (h/select :r/id
                          :r/recipient-name
                          :r/recipient-address
                          :r/recipient-location)
                (h/from [:recipient :r])
                (h/where [:= :r/id recipient-id])
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(defn new-recipient
  "Create a new recipient"
  [conn recipient]
  (let [sql (-> (h/insert-into :recipient)
                (h/values [recipient])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(defn update-recipient
  "Update an existing recipient"
  [conn recipient]
  (let [sql (-> (h/update [:recipient :r])
                (h/set (dissoc recipient :id))
                (h/where [:= :r/id (:id recipient)])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(defn save-recipient
  "Save a recipient"
  [conn recipient]
  (if (:id recipient)
    (update-recipient recipient)
    (new-recipient recipient)))

(defn delete-recipient
  "Delete a recipient"
  [conn recipient-id]
  (let [sql (-> (h/delete-from :recipient)
                (h/where [:= :id recipient-id])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql)))