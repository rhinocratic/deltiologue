(ns rhinocratic.deltiologue.web.api.routes.series
  (:require
   [rhinocratic.deltiologue.web.api.handlers.series :as h]))

(defn routes
  [conn]
  ["/series" {:swagger {:tags ["series"]}}
   ["" {:name ::series-list
        :get {:summary "Fetch all series"
              :handler (partial #'h/all-series conn)}
        :post {:summary "Create a new series"
               :handler (partial #'h/new-series conn)}}]
   ["/:series-id"
    ["" {:name ::series
         :parameters {:path {:series-id int?}}
         :get {:summary "Fetch a series by ID"
               :handler (partial #'h/get-series conn)}
         :put {:summary "Update an existing series"
               :handler (partial #'h/update-series conn)}
         :delete {:summary "Delete a series"
                  :handler (partial #'h/delete-series conn)}}]]])