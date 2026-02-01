(ns rhinocratic.deltiologue.web.api.routes.series
  (:require
   [rhinocratic.deltiologue.web.api.handlers.series :as h]))

(defn routes
  [conn]
  ["/series" {:swagger {:tags ["series"]}}
   ["" {:name ::series-list
        :get {:handler (partial #'h/all-series conn)
              :summary "Fetch all series"}
        :post {:handler (partial #'h/new-series conn)
               :summary "Create a new series"}}]
   ["/:series-id"
    ["" {:name ::series
         :parameters {:path {:series-id int?}}
         :get {:handler (partial #'h/get-series conn)
               :summary "Fetch a series by ID"}
         :put {:handler (partial #'h/update-series conn)
               :summary "Update an existing series"}
         :delete {:handler (partial #'h/delete-series conn)
                  :summary "Delete a series"}}]]])