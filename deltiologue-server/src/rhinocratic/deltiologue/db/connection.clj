(ns rhinocratic.deltiologue.db.connection
  (:require
   [next.jdbc :as jdbc]
   [next.jdbc.connection :as connection]
   [com.brunobonacci.mulog :as u])
  (:import (com.zaxxer.hikari HikariDataSource)))

(defn pooled-datasource
  "Create a JDBC DataSource from a map containing a DB spec."
  ^HikariDataSource [datasource]
  (u/log ::create-pooled-datasource :message "Creating pooled datasource")
  (try
    (let [datasource (connection/->pool com.zaxxer.hikari.HikariDataSource datasource)]
      (.close (jdbc/get-connection datasource)) ;; Get a connection and immediately close it, just to instantiate the data source.
      datasource)
    (catch Exception e
      (println (.getMessage e))
      (u/log ::db-exception :message (.getMessage e)))))
