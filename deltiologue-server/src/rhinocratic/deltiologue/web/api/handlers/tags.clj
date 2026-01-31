(ns rhinocratic.deltiologue.web.api.handlers.tags
  (:require
   [clojure.string :as string]
   [reitit.core :as r]
   [rhinocratic.deltiologue.db.queries.tags :as q]
   ;;  [rhinocratic.deltiologue.web.api.routes :as rts]
   [rhinocratic.deltiologue.web.api.handlers.util :as u]))

(defn all-tags
  "Fetch all tags"
  [conn _req]
  (let [tags (q/tags conn)]
    {:status 200 :body tags}))

(defn new-tag
  "Create a new tag"
  [conn req]
  (let [tag (get-in req [:body-params])
        saved-tag (q/save-tag conn tag)
        saved-tag-id (:tag/id saved-tag)
        route-name :rhinocratic.deltiologue.web.api.routes.tags/tag
        path-params {:tag-id saved-tag-id}]
    {:status 201
     :body saved-tag
     :headers (u/make-location req route-name path-params)}))

(defn get-tag
  "Fetch a tag by ID"
  [conn req]
  (let [tag-id (get-in req [:parameters :path :tag-id])
        tag (q/get-tag conn tag-id)]
    {:status 200 :body tag}))

(defn update-tag
  "Update an existing tag"
  [conn req]
  (let [tag (get-in req [:body-params])
        saved-tag (q/save-tag conn tag)]
    {:status 200
     :body saved-tag}))

(defn delete-tag
  "Delete a tag"
  [conn req]
  (let [tag-id (get-in req [:parameters :path :tag-id])
        deleted (q/delete-tag conn tag-id)]
    {:status 200
     :body deleted}))
