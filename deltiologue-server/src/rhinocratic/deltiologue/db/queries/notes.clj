(ns rhinocratic.deltiologue.db.queries.notes
  (:require
   [clojure.string :as string]
   [next.jdbc :as jdbc]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]))

(defn all-note-summaries
  "Fetch all note summaries"
  [conn]
  (let [sql (-> (h/select :n/id
                          :n/title)
                (h/from [:note :n])
                (sql/format))]
    (jdbc/execute! conn sql)))

(defn note-references
  "Fetch all references associated with a note"
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

(defn get-note
  "Fetch a note by ID"
  [conn note-id]
  (let [sql (-> (h/select :n/title
                          :n/body)
                (h/from [:note :n])
                (h/where [:= :n/id note-id])
                (sql/format))
        note (jdbc/execute-one! conn sql)]
    (merge note {:references (note-references conn note-id)})))

(defn new-note
  "Create a new note"
  [conn note]
  (let [sql (-> (h/insert-into :note)
                (h/values [note])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(defn update-note
  "Update a note"
  [conn note]
  (let [sql (-> (h/update [:note :n])
                (h/set (dissoc note :id))
                (h/where [:= :n/id (:id note)])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(defn save-note
  "Save a note"
  [conn note]
  (if (:id note)
    (update-note note)
    (new-note note)))

(defn delete-note
  "Delete a note"
  [conn note-id]
  (let [sql (-> (h/delete-from :note)
                (h/where [:= :id note-id])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql)))
