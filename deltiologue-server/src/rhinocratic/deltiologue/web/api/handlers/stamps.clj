(ns rhinocratic.deltiologue.web.api.handlers.stamps
  (:require
   [clojure.string :as string]
   [reitit.core :as r]
   [rhinocratic.deltiologue.db.queries.stamps :as q]
   [rhinocratic.deltiologue.web.api.handlers.util :as u]))

(defn all-stamps
  "Fetch all stamps"
  [conn _req]
  (let [stamps (q/all-stamps conn)]
    {:status 200 :body stamps}))

(defn get-stamp
  "Fetch a stamp by ID"
  [conn req]
  (let [stamp-id (get-in req [:parameters :path :stamp-id])
        stamp (q/get-stamp conn stamp-id)]
    {:status 200 :body stamp}))

(defn new-stamp
  "Create a new stamp"
  [conn req]
  (let [stamp (get-in req [:body-params])
        saved-stamp (q/save-stamp conn stamp)
        saved-stamp-id (:stamp/id saved-stamp)
        route-name :rhinocratic.deltiologue.web.api.routes.stamps/stamp
        path-params {:stamp-id saved-stamp-id}]
    {:status 201
     :body saved-stamp
     :headers (u/make-location req route-name path-params)}))

(defn update-stamp
  "Update an existing stamp"
  [conn req]
  (let [stamp (get-in req [:body-params])
        saved-stamp (q/save-stamp conn stamp)]
    {:status 200
     :body saved-stamp}))

(defn delete-stamp
  "Delete a stamp"
  [conn req]
  (let [stamp-id (get-in req [:parameters :path :stamp-id])
        deleted (q/delete-stamp conn stamp-id)]
    {:status 200
     :body deleted}))
