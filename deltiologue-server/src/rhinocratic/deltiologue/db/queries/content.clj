(ns rhinocratic.deltiologue.db.queries.content
  (:require
   [clojure.string :as string]
   [next.jdbc :as jdbc]
   [next.jdbc.result-set :as rs]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]))

(def opts {:builder-fn rs/as-unqualified-maps})

(defn all-content-summaries
  "Fetch all content summaries"
  [conn]
  (let [sql (-> (h/select :c/id
                          :c/title)
                (h/from [:content :c])
                (sql/format))]
    (jdbc/execute! conn sql opts)))

(defn get-content
  "Fetch a content item by ID"
  [conn content-id]
  (let [sql (-> (h/select :c/id
                          :c/title
                          :c/content)
                (h/from [:content :c])
                (h/where [:= :c/id content-id])
                (sql/format))]
    (jdbc/execute-one! conn sql opts)))

(defn update-content
  "Update a content item"
  [conn content]
  (let [sql (-> (h/update [:content :c])
                (h/set (dissoc content :id))
                (h/where [:= :c/id (:id content)])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql opts)))