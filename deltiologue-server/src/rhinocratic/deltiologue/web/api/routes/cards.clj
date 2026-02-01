(ns rhinocratic.deltiologue.web.api.routes.cards
  (:require
   [rhinocratic.deltiologue.web.api.handlers.cards :as h]))

(defn routes
  [conn]
  ["/cards" {:swagger {:tags ["cards"]}}
   ["" {:name ::card-list
        :post {:handler (partial #'h/new-card conn)
               :summary "Create a new card"}}]
   ["/:card-id" {:name ::card
                 :parameters {:path {:card-id int?}}
                 :get {:handler (partial #'h/get-card conn)
                       :summary "Fetch a card by ID"}
                 :put {:handler (partial #'h/update-card conn)
                       :summary "Update a card by ID"}
                 :delete {:handler (partial #'h/delete-card conn)
                          :summary "Delete a card"}}]])