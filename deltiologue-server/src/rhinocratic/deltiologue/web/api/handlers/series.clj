(ns rhinocratic.deltiologue.web.api.handlers.series
  (:require
   [clojure.string :as string]
   [reitit.core :as r]
   [rhinocratic.deltiologue.db.queries.series :as q]
   [rhinocratic.deltiologue.web.api.handlers.util :as u]))

(defn all-series
  "Fetch all series"
  [conn _req]
  (let [series (q/all-series conn)]
    {:status 200 :body series}))

(defn get-series
  "Fetch a series by ID"
  [conn req]
  (let [series-id (get-in req [:parameters :path :series-id])
        series (q/get-series conn series-id)]
    (if series
      {:status 200 :body series}
      {:status 404})))

(defn new-series
  "Create a new series"
  [conn req]
  (let [series (get-in req [:body-params])
        saved-series (q/save-series conn series)
        saved-series-id (:series/id saved-series)
        route-name :rhinocratic.deltiologue.web.api.routes.series/series
        path-params {:series-id saved-series-id}]
    {:status 201
     :body saved-series
     :headers (u/make-location req route-name path-params)}))

(defn update-series
  "Update an existing series"
  [conn req]
  (let [series (get-in req [:body-params])
        saved-series (q/save-series conn series)]
    {:status 200
     :body saved-series}))

(defn delete-series
  "Delete a series"
  [conn req]
  (let [series-id (get-in req [:parameters :path :series-id])
        deleted (q/delete-series conn series-id)]
    {:status 200
     :body deleted}))