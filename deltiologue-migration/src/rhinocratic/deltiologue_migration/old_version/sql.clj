(ns rhinocratic.deltiologue-migration.old-version.sql
  (:require
   [clojure.string :as string]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]
   [next.jdbc :as jdbc]
   [next.jdbc.date-time :as dt]
   [camel-snake-kebab.core :as csk]
   [rhinocratic.deltiologue-migration.old-version.spreadsheet :as spreadsheet]
   [rhinocratic.deltiologue-migration.old-version.notes :as notes]
   [rhinocratic.deltiologue-migration.old-version.manual :as manual]
   [rhinocratic.deltiologue-migration.old-version.transform :as transform]
   [rhinocratic.deltiologue-migration.old-version.collate :as collate]))

(dt/read-as-instant)

(def table-cols
  {:tag-category   [:category-name
                    :display-text
                    :colour]
   :tag            [:tag-name
                    :display-text]
   :stamp          [:stamp-description]
   :note-image     [:filename
                    :caption
                    :alt-text]
   :postcard       [:draft
                    :index
                    :divided-back
                    :rp
                    :used
                    :posted
                    :franked
                    :image-front-alt
                    :image-rear-alt
                    :publication-year
                    :publication-month
                    :publication-day
                    :publication-date
                    :publication-date-approximate
                    :posted-year
                    :posted-month
                    :posted-day
                    :posted-date
                    :posted-date-approximate
                    :subject-description
                    :subject-location
                    :subject-current-view
                    :notes
                    :series-id
                    :publisher-id
                    :publication-description
                    :recipient-name
                    :recipient-address
                    :recipient-location]
   :postcard-tag   [:postcard-id
                    :tag-id
                    :tag-category-id]
   :postcard-stamp [:postcard-id
                    :stamp-id
                    :stamp-condition]
   :slideshow      [:postcard-id]
   :note           [:draft
                    :title
                    :body]
   :reference      [:index
                    :authors
                    :medium
                    :accessed
                    :source
                    :title
                    :issue-date
                    :issue-note
                    :available]
   :note-reference [:note-id
                    :reference-id]
   :content        [:title
                    :content
                    :draft]})

(defn insert-statements
  [table cols collated]
  (->
   (h/insert-into table)
   (h/values (for [row (table collated)]
               (select-keys row cols)))
   (sql/format)))

(def all-tables
  [:tag-category
   :tag
   :stamp
   :note
   :note-image
   :reference
   :note-reference
   :postcard
   :postcard-tag
   :postcard-stamp
   :slideshow
   :content])

(defn truncate-tables
  [connection]
  (let [sql (->> all-tables
                 reverse
                 (map csk/->snake_case_string)
                 (string/join ", ")
                 (#(str "TRUNCATE " % " RESTART IDENTITY"))
                 vector)]
    (jdbc/execute! connection sql)))

(defn generate-sql
  [table rows]
  (insert-statements table (table-cols table) rows))

(defn seed-table
  [connection table rows]
  (let [sql (generate-sql table rows)]
    (jdbc/execute! connection sql)))

(defn seed-db
  [connection]
  (let [rows (->> (merge (spreadsheet/parse) (notes/parse) (manual/tables))
                  transform/transform
                  collate/collate)]
    (truncate-tables connection)
    (doseq [table all-tables]
      (seed-table connection table rows))))

(defn truncate-db
  [connection]
  (truncate-tables connection))

(comment

  (require '[rhinocratic.deltiologue-migration.old-version.spreadsheet :as spreadsheet]
           '[rhinocratic.deltiologue-migration.old-version.notes :as notes]
           '[rhinocratic.deltiologue-migration.old-version.transform :as transform]
           '[rhinocratic.deltiologue-migration.old-version.manual :as manual]
           '[rhinocratic.deltiologue-migration.old-version.collate :as collate])

  (def m (->> (merge (spreadsheet/parse) (notes/parse) (manual/tables))
              transform/transform
              collate/collate))

  (tap> m)

  (tap> (:note m))

  (seed-db {#_datasource})

  (truncate-db {#_datasource})

  #_())