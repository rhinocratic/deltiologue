(ns rhinocratic.deltiologue.db.queries.notes
  (:require
   [clojure.string :as string]
   [next.jdbc :as jdbc]
   [next.jdbc.result-set :as rs]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]))

(def opts {:builder-fn rs/as-unqualified-maps})

(defn all-note-summaries
  "Fetch all note summaries"
  [conn]
  (let [sql (-> (h/select :n/id
                          :n/title)
                (h/from [:note :n])
                (sql/format))]
    (jdbc/execute! conn sql opts)))

(defn note-references
  "Fetch all references associated with a note"
  [conn note-id]
  (let [sql (-> (h/select :r/id
                          :r/index
                          :r/authors
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
    (jdbc/execute! conn sql opts)))

(defn get-note
  "Fetch a note by ID"
  [conn note-id]
  (let [sql (-> (h/select :n/id
                          :n/title
                          :n/body)
                (h/from [:note :n])
                (h/where [:= :n/id note-id])
                (sql/format))
        note (jdbc/execute-one! conn sql opts)]
    (when note
      (merge note {:references (note-references conn note-id)}))))

(defn new-note
  "Create a new note"
  [conn note]
  (let [sql (-> (h/insert-into :note)
                (h/values [note])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql opts)))

(defn update-note
  "Update a note"
  [conn note]
  (let [sql (-> (h/update [:note :n])
                (h/set (dissoc note :id))
                (h/where [:= :n/id (:id note)])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql opts)))

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
    (jdbc/execute-one! conn sql opts)))
