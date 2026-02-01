(ns rhinocratic.deltiologue.web.api.routes.content
  (:require
   [clojure.spec.alpha :as s]
   [rhinocratic.deltiologue.web.api.handlers.content :as h]))

(s/def ::id int?)
(s/def ::title string?)
(s/def ::content string?)
(s/def ::content-summary
  (s/keys :req-un [::id
                   ::title]))
(s/def ::content-summary-list
  (s/coll-of ::content-summary :into []))
(s/def ::content-item
  (s/keys :req-un [::id
                   ::title
                   ::content]))

(defn routes
  [conn]
  ["/content" {:swagger {:tags ["content"]}}
   ["" {:name ::content-summaries
        :get {:summary "Fetch summaries of all content items"
              :handler (partial #'h/all-content-summaries conn)
              :responses {200 {:body ::content-summary-list}}}}]
   ["/:content-id"
    ["" {:name ::content
         :parameters {:path {:content-id int?}}
         :get {:summary "Fetch a content item by ID"
               :handler (partial #'h/get-content conn)
               :responses {200 {:body ::content-item}}}
         :put {:summary "Update an existing content item"
               :handler (partial #'h/update-content conn)
               :parameters {:body ::content-item}
               :responses {200 {:body ::content-item}}}}]]])