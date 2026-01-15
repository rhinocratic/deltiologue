(ns rhinocratic.deltiologue-migration.load
  (:require
   [next.jdbc :as jdbc]
   [next.jdbc.connection :as connection]
   [rhinocratic.deltiologue-migration.old-version.spreadsheet :as spreadsheet]
   [rhinocratic.deltiologue-migration.old-version.notes :as notes]
   [rhinocratic.deltiologue-migration.old-version.transform :refer [transform]]
   [rhinocratic.deltiologue-migration.old-version.collate :refer [collate]]
   [rhinocratic.deltiologue-migration.old-version.sql :refer [seed-db truncate-db]]
   [rhinocratic.deltiologue-migration.old-version.images :as images]
   [rhinocratic.deltiologue-migration.config :as conf])
  (:import (com.zaxxer.hikari HikariDataSource)))

(defn pooled-datasource
  "Create a JDBC DataSource from a map containing a DB spec."
  ^HikariDataSource [datasource]
  (let [datasource (connection/->pool com.zaxxer.hikari.HikariDataSource datasource)]
    (.close (jdbc/get-connection datasource)) ;; Get a connection and immediately close it, just to instantiate the data source.
    datasource))

(defn seed
  [{:keys [profile]}]
  (let [profile (or profile :dev)
        config (-> "config.edn"
                   (conf/system-config profile)
                   (get-in [:rhinocratic.deltiologue/db :datasource]))]
    (with-open [datasource (pooled-datasource config)]
      (seed-db datasource))))

(defn truncate
  [{:keys [profile]}]
  (let [profile (or profile :dev)
        config (-> "config.edn"
                   (conf/system-config profile)
                   (get-in [:rhinocratic.deltiologue/db :datasource]))]
    (with-open [datasource (pooled-datasource config)]
      (truncate-db datasource))))

(defn update-images
  [_]
  (images/update-images))

(defn recreate-volume
  [_]
  (images/recreate-volume))
