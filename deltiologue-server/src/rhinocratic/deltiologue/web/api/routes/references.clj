(ns rhinocratic.deltiologue.web.api.routes.references
  (:require
   [rhinocratic.deltiologue.web.api.handlers.references :as h]))

(defn routes
  [conn]
  ["/references" {:swagger {:tags ["references"]}}
   ["" {:name ::reference-list
        :get {:handler (partial #'h/references conn)
              :summary "Fetch all references"}
        :post {:handler (partial #'h/new-reference conn)
               :summary "Create a new reference"}}]
   ["/:reference-id" {:name ::reference
                      :parameters {:path {:reference-id int?}}
                      :get {:handler (partial #'h/get-reference conn)
                            :summary "Fetch a reference by ID"}
                      :put {:handler (partial #'h/update-reference conn)
                            :summary "Update an existing reference"}
                      :delete {:handler (partial #'h/delete-reference conn)
                               :summary "Delete a reference"}}]])