(ns rhinocratic.deltiologue.web.api.handlers.tag-categories
  (:require
   [clojure.string :as string]
   [reitit.core :as r]
   [rhinocratic.deltiologue.db.queries.tag-categories :as q]
   ;;  [rhinocratic.deltiologue.web.api.routes :as rts]
   [rhinocratic.deltiologue.web.api.handlers.util :as u]))

(defn tag-categories
  "Fetch all tag categories"
  [conn _req]
  (let [categories (q/tag-categories conn)]
    {:status 200 :body categories}))

(defn tag-category
  "Fetch a single tag category"
  [conn req]
  (let [category-id (get-in req [:parameters :path :category-id])
        category (q/tag-category conn category-id)]
    {:status 200 :body category}))

(defn new-tag-category
  "Create a new tag category"
  [conn req]
  (let [category (get-in req [:body-params])
        router (::r/router req)
        saved-category (q/save-tag-category conn category)
        id (:tag_category/id saved-category)
        path (-> router
                 ;;  (r/match-by-name ::rts/tag-category {:category-id id})
                 :path)]
    {:status 201
     :body saved-category
     :headers (u/make-location req path)}))

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
  (let [category-id (get-in req [:parameters :path :category-id])
        deleted (q/delete-tag-category conn category-id)]
    {:status 200
     :body deleted}))