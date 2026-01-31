(ns rhinocratic.deltiologue.web.api.handlers.references
  (:require
   [clojure.string :as string]
   [rhinocratic.deltiologue.db.queries.references :as q]
   ;;  [rhinocratic.deltiologue.web.api.routes :as rts]
   [rhinocratic.deltiologue.web.api.handlers.util :as u]))

(defn references
  "Fetch all references"
  [conn _req]
  (let [references (q/all-references conn)]
    {:status 200
     :body references}))

(defn new-reference
  "Create a new reference"
  [conn req]
  (let [reference (get-in req [:body-params])
        saved-reference (q/save-reference conn reference)
        saved-reference-id (:reference/id saved-reference)
        route-name :rhinocratic.deltiologue.web.api.routes.references/reference
        path-params {:reference-id saved-reference-id}]
    {:status 201
     :body saved-reference
     :headers (u/make-location req route-name path-params)}))

(defn get-reference
  "Fetch a reference by ID"
  [conn req]
  (let [reference-id (get-in req [:parameters :path :reference-id])
        reference (q/get-reference conn reference-id)]
    {:status 200 :body reference}))

(defn update-reference
  "Update an existing reference"
  [conn req]
  (let [reference (get-in req [:body-params])
        saved-reference (q/save-reference conn reference)]
    {:status 200 :body saved-reference}))

(defn delete-reference
  "Delete a reference"
  [conn req]
  (let [reference-id (get-in req [:parameters :path :reference-id])
        deleted-reference (q/delete-reference conn reference-id)]
    {:status 200 :body delete-reference}))