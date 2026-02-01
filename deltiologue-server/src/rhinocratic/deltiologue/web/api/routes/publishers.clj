(ns rhinocratic.deltiologue.web.api.routes.publishers
  (:require
   [rhinocratic.deltiologue.web.api.handlers.publishers :as h]))

(defn routes
  [conn]
  ["/publishers" {:swagger {:tags ["publishers"]}}
   ["" {:name ::publisher-list
        :get {:summary "Fetch all publishers"
              :handler (partial #'h/all-publishers conn)}
        :post {:summary "Create a new publisher"
               :handler (partial #'h/new-publisher conn)}}]
   ["/:publisher-id"
    ["" {:name ::publisher
         :parameters {:path {:publisher-id int?}}
         :get {:summary "Fetch a publisher by ID"
               :handler (partial #'h/get-publisher conn)}
         :put {:summary "Update an existing publisher"
               :handler (partial #'h/update-publisher conn)}
         :delete {:summary "Delete a publisher"
                  :handler (partial #'h/delete-publisher conn)}}]]])