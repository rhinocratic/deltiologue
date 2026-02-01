(ns rhinocratic.deltiologue.web.api.routes.publishers
  (:require
   [rhinocratic.deltiologue.web.api.handlers.publishers :as h]))

(defn routes
  [conn]
  ["/publishers" {:swagger {:tags ["publishers"]}}
   ["" {:name ::publisher-list
        :get {:handler (partial #'h/all-publishers conn)
              :summary "Fetch all publishers"}
        :post {:handler (partial #'h/new-publisher conn)
               :summary "Create a new publisher"}}]
   ["/:publisher-id"
    ["" {:name ::publisher
         :parameters {:path {:publisher-id int?}}
         :get {:handler (partial #'h/get-publisher conn)
               :summary "Fetch a publisher by ID"}
         :put {:handler (partial #'h/update-publisher conn)
               :summary "Update an existing publisher"}
         :delete {:handler (partial #'h/delete-publisher conn)
                  :summary "Delete a publisher"}}]]])