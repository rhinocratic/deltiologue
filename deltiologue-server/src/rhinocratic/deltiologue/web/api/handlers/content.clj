(ns rhinocratic.deltiologue.web.api.handlers.content
  (:require
   [clojure.string :as string]
   [reitit.core :as r]
   [rhinocratic.deltiologue.db.queries.content :as q]
   [rhinocratic.deltiologue.web.api.handlers.util :as u]))

(defn all-content-summaries
  "Fetch summaries of all content items"
  [conn _req]
  (let [summaries (q/all-content-summaries conn)]
    {:status 200 :body summaries}))

(defn get-content
  "Fetch a content item by ID"
  [conn req]
  (let [content-id (get-in req [:parameters :path :content-id])
        content (q/get-content conn content-id)]
    {:status 200 :body content}))

(defn update-content
  "Update an existing content item"
  [conn req]
  (let [content (get-in req [:body-params])
        saved (q/update-content conn content)]
    {:status 200
     :body saved}))