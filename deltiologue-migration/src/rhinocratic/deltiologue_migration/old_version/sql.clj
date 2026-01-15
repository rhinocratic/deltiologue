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
  {:tag-category       [:category-name
                        :display-text]
   :tag                [:tag-name
                        :display-text]
   :stamp              [:stamp-description]
   :publisher          [:publisher-name]
   :recipient          [:recipient-name
                        :recipient-address
                        :recipient-location]
   :series             [:series-name]
   :image              [:url
                        :title
                        :alt-text]
   :note-image         [:filename
                        :caption
                        :alt-text]
   :postcard           [:collection-index
                        :divided-back
                        :rp
                        :used
                        :posted
                        :franked
                        :image-front
                        :image-front-alt
                        :image-rear
                        :image-rear-alt
                        :image-thumb
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
                        :transcript
                        :publisher
                        :recipient
                        :series
                        :series-entry]
   :postcard-tag       [:postcard-id
                        :tag-id
                        :tag-category-id]
   :postcard-stamp     [:postcard-id
                        :stamp-id
                        :stamp-condition]
   :slideshow          [:postcard-id]
   :note               [:title
                        :body]
   :reference          [:idx
                        :medium
                        :accessed
                        :source
                        :title
                        :issue-date
                        :issue-note
                        :available]
   :note-reference     [:note-id
                        :reference-id]})

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
   :series
   :recipient
   :publisher
   :note
   :note-image
   :reference
   :note-reference
   :image
   :postcard
   :postcard-tag
   :postcard-stamp
   :slideshow])

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