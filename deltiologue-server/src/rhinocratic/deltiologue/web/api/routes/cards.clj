(ns rhinocratic.deltiologue.web.api.routes.cards
  (:require
   [rhinocratic.deltiologue.web.api.handlers.cards :as h]))

(defn routes
  [conn]
  ["/cards" {:swagger {:tags ["cards"]}}
   ["" {:name ::card-list
        :post {:summary "Create a new card"
               :handler (partial #'h/new-card conn)}}]
   ["/:card-id" {:name ::card
                 :parameters {:path {:card-id int?}}
                 :get {:summary "Fetch a card by ID"
                       :handler (partial #'h/get-card conn)}
                 :put {:summary "Update a card by ID"
                       :handler (partial #'h/update-card conn)}
                 :delete {:summary "Delete a card"
                          :handler (partial #'h/delete-card conn)}}]])