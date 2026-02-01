(ns rhinocratic.deltiologue.web.api.routes.recipients
  (:require
   [rhinocratic.deltiologue.web.api.handlers.recipients :as h]))

(defn routes
  [conn]
  ["/recipients" {:swagger {:tags ["recipients"]}}
   ["" {:name ::recipient-list
        :get {:handler (partial #'h/all-recipients conn)
              :summary "Fetch all recipients"}
        :post {:handler (partial #'h/new-recipient conn)
               :summary "Create a new recipient"}}]
   ["/:recipient-id"
    ["" {:name ::recipient
         :parameters {:path {:recipient-id int?}}
         :get {:handler (partial #'h/get-recipient conn)
               :summary "Fetch a recipient by ID"}
         :put {:handler (partial #'h/update-recipient conn)
               :summary "Update an existing recipient"}
         :delete {:handler (partial #'h/delete-recipient conn)
                  :summary "Delete a recipient"}}]]])