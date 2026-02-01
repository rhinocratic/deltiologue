(ns rhinocratic.deltiologue.web.api.routes.publishers
  (:require
   [clojure.spec.alpha :as s]
   [rhinocratic.deltiologue.web.api.handlers.publishers :as h]))

(s/def ::id int?)
(s/def ::publisher_name string?)
(s/def ::new-publisher
  (s/keys :req-un [::publisher_name]))
(s/def ::publisher
  (s/keys :req-un [::id
                   ::publisher_name]))
(s/def ::publisher-list
  (s/coll-of ::publisher :into []))

(defn routes
  [conn]
  ["/publishers" {:swagger {:tags ["publishers"]}}
   ["" {:name ::publisher-list
        :get {:summary "Fetch all publishers"
              :handler (partial #'h/all-publishers conn)
              :responses {200 {:body ::publisher-list}}}
        :post {:summary "Create a new publisher"
               :handler (partial #'h/new-publisher conn)
               :parameters {:body ::new-publisher}
               :responses {201 {:body ::publisher}}}}]
   ["/:publisher-id"
    ["" {:name ::publisher
         :parameters {:path {:publisher-id int?}}
         :get {:summary "Fetch a publisher by ID"
               :handler (partial #'h/get-publisher conn)
               :responses {200 {:body ::publisher}}}
         :put {:summary "Update an existing publisher"
               :handler (partial #'h/update-publisher conn)
               :parameters {:body ::publisher}
               :responses {200 {:body ::publisher}}}
         :delete {:summary "Delete a publisher"
                  :handler (partial #'h/delete-publisher conn)
                  :responses {200 {:body ::publisher}}}}]]])