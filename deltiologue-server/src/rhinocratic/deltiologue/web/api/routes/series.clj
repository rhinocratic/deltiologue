(ns rhinocratic.deltiologue.web.api.routes.series
  (:require
   [clojure.spec.alpha :as s]
   [rhinocratic.deltiologue.web.api.handlers.series :as h]))

(s/def ::id int?)
(s/def ::series_name string?)
(s/def ::new-series
  (s/keys :req-un [::series_name]))
(s/def ::series
  (s/keys :req-un [::id
                   ::series_name]))
(s/def ::series-list
  (s/coll-of ::series :into []))

(defn routes
  [conn]
  ["/series" {:swagger {:tags ["series"]}}
   ["" {:name ::series-list
        :get {:summary "Fetch all series"
              :handler (partial #'h/all-series conn)
              :responses {200 {:body ::series-list}}}
        :post {:summary "Create a new series"
               :handler (partial #'h/new-series conn)
               :parameters {:body ::new-series}
               :responses {201 {:body ::series}}}}]
   ["/:series-id"
    ["" {:name ::series
         :parameters {:path {:series-id int?}}
         :get {:summary "Fetch a series by ID"
               :handler (partial #'h/get-series conn)
               :responses {200 {:body ::series}}}
         :put {:summary "Update an existing series"
               :handler (partial #'h/update-series conn)
               :parameters {:body ::series}
               :responses {200 {:body ::series}}}
         :delete {:summary "Delete a series"
                  :handler (partial #'h/delete-series conn)
                  :responses {200 {:body ::series}}}}]]])