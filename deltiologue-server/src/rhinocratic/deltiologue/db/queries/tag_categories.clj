(ns rhinocratic.deltiologue.db.queries.tag-categories
  (:require
   [clojure.string :as string]
   [next.jdbc :as jdbc]
   [next.jdbc.result-set :as rs]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]))

(def opts {:builder-fn rs/as-unqualified-maps})

(defn all-tag-categories
  [conn]
  (let [sql (-> (h/select :tc/id
                          :tc/display-text
                          :tc/category-name
                          :tc/colour)
                (h/from [:tag-category :tc])
                (sql/format))]
    (jdbc/execute! conn sql opts)))

(defn get-tag-category
  [conn category-id]
  (let [sql (-> (h/select :tc/id
                          :tc/display-text
                          :tc/category-name
                          :tc/colour)
                (h/from [:tag-category :tc])
                (h/where [:= :tc.id category-id])
                (sql/format))]
    (jdbc/execute-one! conn sql opts)))

(defn- new-tag-category
  [conn category]
  (let [sql (-> (h/insert-into :tag-category)
                (h/values [category])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql opts)))

(defn- update-tag-category
  [conn category]
  (let [sql (-> (h/update [:tag-category :tc])
                (h/set (dissoc category :id))
                (h/where [:= :tc/id (:id category)])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql opts)))

(defn save-tag-category
  [conn category]
  (let [category-name (-> category
                          :display-text
                          (string/trim)
                          (string/lower-case)
                          (string/replace #"\s+" "_"))
        category (assoc category :category-name category-name)]
    (if (:id category)
      (update-tag-category conn category)
      (new-tag-category conn category))))

(defn delete-tag-category
  [conn category-id]
  (let [sql (-> (h/delete-from :tag-category)
                (h/where [:= :id category-id])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql opts)))


(comment

  (let [conn (:rhinocratic.deltiologue/db (user/system))]
    (get-tag-category conn 1))

  (let [conn (:rhinocratic.deltiologue/db (user/system))]
    (save-tag-category conn {:display-text "Another brand new one"}))

  (let [conn (:rhinocratic.deltiologue/db (user/system))]
    (save-tag-category conn {:id 27 :display-text "Somewhat changed"}))

  (let [conn (:rhinocratic.deltiologue/db (user/system))]
    (delete-tag-category conn 9))


  #_())