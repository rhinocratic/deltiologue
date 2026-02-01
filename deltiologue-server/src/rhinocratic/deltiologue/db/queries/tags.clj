(ns rhinocratic.deltiologue.db.queries.tags
  (:require
   [clojure.string :as string]
   [next.jdbc :as jdbc]
   [next.jdbc.result-set :as rs]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]))

(def opts {:builder-fn rs/as-unqualified-maps})

(defn all-tags
  "Fetch all tags"
  [conn]
  (let [sql (-> (h/select :t/id
                          :t/tag-name
                          :t/display-text)
                (h/from [:tag :t])
                (sql/format))]
    (jdbc/execute! conn sql opts)))

(defn get-tag
  "Fetch a tag by ID"
  [conn tag-id]
  (let [sql (-> (h/select :t/id
                          :t/tag-name
                          :t/display-text)
                (h/from [:tag :t])
                (h/where [:= :t/id tag-id])
                (sql/format))]
    (jdbc/execute-one! conn sql opts)))

(defn- new-tag
  "Create a new tag"
  [conn tag]
  (let [sql (-> (h/insert-into :tag)
                (h/values [tag])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql opts)))

(defn- update-tag
  "Update an existing tag"
  [conn tag]
  (let [sql (-> (h/update [:tag :t])
                (h/set (dissoc tag :id))
                (h/where [:= :t/id (:id tag)])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql opts)))

(defn save-tag
  "Save a tag"
  [conn tag]
  (if (:id tag)
    (update-tag conn tag)
    (new-tag conn tag)))

(defn delete-tag
  "Delete a tag"
  [conn tag-id]
  (let [sql (-> (h/delete-from :tag)
                (h/where [:= :tag/id tag-id])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql opts)))
