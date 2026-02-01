(ns rhinocratic.deltiologue.web.api.routes.recipients
  (:require
   [rhinocratic.deltiologue.web.api.handlers.recipients :as h]))

(defn routes
  [conn]
  ["/recipients" {:swagger {:tags ["recipients"]}}
   ["" {:name ::recipient-list
        :get {:summary "Fetch all recipients"
              :handler (partial #'h/all-recipients conn)}
        :post {:summary "Create a new recipient"
               :handler (partial #'h/new-recipient conn)}}]
   ["/:recipient-id"
    ["" {:name ::recipient
         :parameters {:path {:recipient-id int?}}
         :get {:summary "Fetch a recipient by ID"
               :handler (partial #'h/get-recipient conn)}
         :put {:summary "Update an existing recipient"
               :handler (partial #'h/update-recipient conn)}
         :delete {:summary "Delete a recipient"
                  :handler (partial #'h/delete-recipient conn)}}]]])