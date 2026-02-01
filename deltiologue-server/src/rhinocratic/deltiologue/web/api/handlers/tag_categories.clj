(ns rhinocratic.deltiologue.web.api.handlers.tag-categories
  (:require
   [clojure.string :as string]
   [reitit.core :as r]
   [rhinocratic.deltiologue.db.queries.tag-categories :as q]
   [rhinocratic.deltiologue.web.api.handlers.util :as u]))

(defn all-tag-categories
  "Fetch all tag categories"
  [conn _req]
  (let [categories (q/all-tag-categories conn)]
    {:status 200 :body categories}))

(defn get-tag-category
  "Fetch a single tag category"
  [conn req]
  (let [category-id (get-in req [:parameters :path :tag-category-id])
        category (q/get-tag-category conn category-id)]
    (if category
      {:status 200 :body category}
      {:status 404})))

(defn new-tag-category
  "Create a new tag category"
  [conn req]
  (let [category (get-in req [:body-params])
        saved-category (q/save-tag-category conn category)
        saved-category-id (:tag_category/id saved-category)
        route-name :rhinocratic.deltiologue.web.api.routes.tag-categories/tag-category
        path-params {:tag-category-id saved-category-id}]
    {:status 201
     :body saved-category
     :headers (u/make-location req route-name path-params)}))

(defn update-tag-category
  "Update a tag category"
  [conn req]
  (let [category (get-in req [:body-params])
        saved-category (q/save-tag-category conn category)]
    {:status 200
     :body saved-category}))

(defn delete-tag-category
  "Delete a tag category"
  [conn req]
  (let [category-id (get-in req [:parameters :path :tag-category-id])
        deleted (q/delete-tag-category conn category-id)]
    {:status 200
     :body deleted}))