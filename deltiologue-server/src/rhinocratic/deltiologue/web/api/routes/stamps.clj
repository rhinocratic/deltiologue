(ns rhinocratic.deltiologue.web.api.routes.stamps
  (:require
   [rhinocratic.deltiologue.web.api.handlers.stamps :as h]))

(defn routes
  [conn]
  ["/stamps" {:swagger {:tags ["stamps"]}}
   ["" {:name ::stamps
        :get {:handler (partial #'h/all-stamps conn)
              :summary "Fetch all stamps"}
        :post {:handler (partial #'h/new-stamp conn)
               :summary "Create a new stamp"}}]
   ["/:stamp-id" {:name ::stamp
                  :parameters {:path {:stamp-id int?}}
                  :get {:handler (partial #'h/get-stamp conn)
                        :summary "Fetch a stamp by ID"}
                  :put {:handler (partial #'h/update-stamp conn)
                        :summary "Update an existing stamp"}
                  :delete {:handler (partial #'h/delete-stamp conn)
                           :summary "Delete a stamp"}}]])