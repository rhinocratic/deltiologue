(ns rhinocratic.deltiologue.web.api.handlers.publishers
  (:require
   [clojure.string :as string]
   [reitit.core :as r]
   [rhinocratic.deltiologue.db.queries.publishers :as q]
   [rhinocratic.deltiologue.web.api.handlers.util :as u]))

(defn all-publishers
  "Fetch all publishers"
  [conn _req]
  (let [publishers (q/all-publishers conn)]
    {:status 200 :body publishers}))

(defn get-publisher
  "Fetch a publisher by ID"
  [conn req]
  (let [publisher-id (get-in req [:parameters :path :publisher-id])
        publisher (q/get-publisher conn publisher-id)]
    (if publisher
      {:status 200 :body publisher}
      {:status 404})))

(defn new-publisher
  "Create a new publisher"
  [conn req]
  (let [publisher (get-in req [:body-params])
        saved-publisher (q/save-publisher conn publisher)
        saved-publisher-id (:publisher/id saved-publisher)
        route-name :rhinocratic.deltiologue.web.api.routes.publishers/publisher
        path-params {:publisher-id saved-publisher-id}]
    {:status 201
     :body saved-publisher
     :headers (u/make-location req route-name path-params)}))

(defn update-publisher
  "Update an existing publisher"
  [conn req]
  (let [publisher (get-in req [:body-params])
        saved-publisher (q/save-publisher conn publisher)]
    {:status 200
     :body saved-publisher}))

(defn delete-publisher
  "Delete a publisher"
  [conn req]
  (let [publisher-id (get-in req [:parameters :path :publisher-id])
        deleted (q/delete-publisher conn publisher-id)]
    {:status 200
     :body deleted}))