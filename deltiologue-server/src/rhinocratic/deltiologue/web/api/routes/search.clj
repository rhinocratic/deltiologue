(ns rhinocratic.deltiologue.web.api.routes.search
  (:require
   [clojure.spec.alpha :as s]
   [rhinocratic.deltiologue.web.api.handlers.search :as h]))

(s/def ::id int?)
(s/def ::collection_index int?)
(s/def ::subject_description string?)
(s/def ::image_thumb int?)
(s/def ::card-summary
  (s/keys :req-un [::id
                   ::collection_index
                   ::subject_description
                   ::image_thumb]))
(s/def ::card-summary-list
  (s/coll-of ::card-summary :into []))

(defn routes
  [conn]
  ["/search" {:swagger {:tags ["search"]}}
   ["" {:name ::search
        :parameters {:query {:q string?}}
        :get {:summary "Search for cards"
              :handler (partial #'h/search conn)
              :responses {200 {:body ::card-summary-list}}}}]])