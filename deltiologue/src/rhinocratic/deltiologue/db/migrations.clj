(ns rhinocratic.deltiologue.db.migrations
  (:require
   [clojure.string :as string]
   [migratus.core :as migratus]
   [rhinocratic.deltiologue.config :as conf]))

(defn db-config
  [profile]
  (let [pg-pass (-> "config.edn"
                    (conf/system-config profile)
                    (get-in [:rhinocratic.deltiologue/db :datasource :password]))]
    {:store                :database
     :migration-dir        "resources/sql/migrations/"
     :migration-table-name "migrations"
     :db                   {:dbtype   "postgres"
                            :dbname   "deltiologue"
                            :user     "deltiologue"
                            :password pg-pass}}))

(defn create-migration
  [{:keys [name]}]
  (let [conf {:migration-dir "resources/sql/migrations/"}]
    (println "Creating new migration with name" name)
    (migratus/create conf name)))

(defn migrate
  [{:keys [profile]}]
  (let [profile (or profile :dev)
        conf (db-config profile)]
    (println "Performing migrations for profile" profile)
    (migratus/migrate conf)))

(defn rollback
  [{:keys [profile]}]
  (let [profile (or profile :dev)
        conf (db-config profile)]
    (println "Rolling back migrations for profile" profile)
    (migratus/rollback conf)))

(defn reset
  [{:keys [profile]}]
  (let [profile (or profile :dev)
        conf (db-config profile)]
    (println "Resetting migrations for profile" profile)
    (apply migratus/reset conf)))

(defn up
  [{:keys [profile ids]}]
  (let [profile (or profile :dev)
        conf (db-config profile)]
    (println "Applying migrations" (string/join ", " ids) "for profile" profile)
    (apply migratus/up conf ids)))

(defn down
  [{:keys [profile ids]}]
  (let [profile (or profile :dev)
        conf (db-config profile)]
    (println "Rolling back migrations" (string/join ", " ids) "for profile" profile)
    (apply migratus/down conf ids)))


(comment

  (db-config :dev)



  (def datasource (->> (user/system) :rhinocratic.deltiologue/db))
  (def conf (config datasource))

  (migratus/init config)

  (migratus/migrate config)

  (migratus/rollback config)

  (migratus/create conf "create-tables")

  (keys next.jdbc.connection/dbtypes))
