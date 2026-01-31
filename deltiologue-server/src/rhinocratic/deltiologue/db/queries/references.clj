(ns rhinocratic.deltiologue.db.queries.references
  (:require
   [clojure.string :as string]
   [next.jdbc :as jdbc]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]))

(defn all-references
  "Fetch all references"
  [conn]
  (let [sql (-> (h/select :r/id
                          :r/idx
                          :r/medium
                          :r/accessed
                          :r/source
                          :r/title
                          :r/issue-date
                          :r/issue-note
                          :r/available)
                (h/from [:reference :r])
                (sql/format))]
    (jdbc/execute! conn sql)))

(defn- new-reference
  "Create a new reference"
  [conn reference]
  (let [sql (-> (h/insert-into :reference)
                (h/values [reference])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(defn get-reference
  "Fetch a reference by ID"
  [conn reference-id]
  (let [sql (-> (h/select :r/id
                          :r/idx
                          :r/medium
                          :r/accessed
                          :r/source
                          :r/title
                          :r/issue-date
                          :r/issue-note
                          :r/available)
                (h/from [:reference :r])
                (h/where [:= :r.id reference-id])
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(defn- update-reference
  "Update an existing reference"
  [conn reference]
  (let [sql (-> (h/update [:reference :r])
                (h/set (dissoc reference :id))
                (h/where [:= :r/id (:id reference)])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(defn save-reference
  "Save a reference"
  [conn reference]
  (if (:id reference)
    (update-reference conn reference)
    (new-reference conn reference)))

(defn delete-reference
  "Delete a reference"
  [conn reference-id]
  (let [sql (-> (h/delete-from :reference)
                (h/where [:= :id reference-id])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql)))

(comment

  (let [conn (:rhinocratic.deltiologue/db (user/system))
        new-ref {:issue_date nil,
                 :available "http://www.heyshamheritage.org.uk/heysham_harbour.html",
                 :source "Heysham Heritage Association 2019.",
                 :accessed (clojure.instant/read-instant-timestamp "2018-01-11T00:00:00.000-00:00"),
                 :title "Heysham Harbour",
                 :medium "Online",
                 :idx 7656765,
                 :issue_note nil}]
    (new-reference conn new-ref))

  (let [conn (:rhinocratic.deltiologue/db (user/system))]
    (all-references conn))

  (let [conn (:rhinocratic.deltiologue/db (user/system))]
    (get-reference conn 188))

  (let [conn (:rhinocratic.deltiologue/db (user/system))
        updated-ref {:id 188
                     :issue_date nil,
                     :available "http://www.heyshamheritage.org.uk/heysham_harbour.html",
                     :source "Heysham Heritage Association 2019.",
                     :accessed (clojure.instant/read-instant-timestamp "2018-01-11T00:00:00.000-00:00"),
                     :title "Heysham Harbour or thereabouts",
                     :medium "Online",
                     :idx 7656766,
                     :issue_note nil}]
    (update-reference conn updated-ref))

  (let [conn (:rhinocratic.deltiologue/db (user/system))]
    (delete-reference conn 188))

  (java.time.LocalDateTime/parse "2022-01-06T00:00:00Z")

  (clojure.instant/read-instant-date "2022-01-06T00:00:00Z")



  #())