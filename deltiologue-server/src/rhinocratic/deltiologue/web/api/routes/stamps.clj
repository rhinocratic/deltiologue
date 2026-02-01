(ns rhinocratic.deltiologue.web.api.routes.stamps
  (:require
   [rhinocratic.deltiologue.web.api.handlers.stamps :as h]))

(defn routes
  [conn]
  ["/stamps" {:swagger {:tags ["stamps"]}}
   ["" {:name ::stamp-list
        :get {:summary "Fetch all stamps"
              :handler (partial #'h/all-stamps conn)}
        :post {:summary "Create a new stamp"
               :handler (partial #'h/new-stamp conn)}}]
   ["/:stamp-id" {:name ::stamp
                  :parameters {:path {:stamp-id int?}}
                  :get {:summary "Fetch a stamp by ID"
                        :handler (partial #'h/get-stamp conn)}
                  :put {:summary "Update an existing stamp"
                        :handler (partial #'h/update-stamp conn)}
                  :delete {:summary "Delete a stamp"
                           :handler (partial #'h/delete-stamp conn)}}]])