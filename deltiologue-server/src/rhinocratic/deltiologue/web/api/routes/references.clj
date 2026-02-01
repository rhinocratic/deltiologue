(ns rhinocratic.deltiologue.web.api.routes.references
  (:require
   [rhinocratic.deltiologue.web.api.handlers.references :as h]))

(defn routes
  [conn]
  ["/references" {:swagger {:tags ["references"]}}
   ["" {:name ::reference-list
        :get {:summary "Fetch all references"
              :handler (partial #'h/references conn)}
        :post {:summary "Create a new reference"
               :handler (partial #'h/new-reference conn)}}]
   ["/:reference-id" {:name ::reference
                      :parameters {:path {:reference-id int?}}
                      :get {:summary "Fetch a reference by ID"
                            :handler (partial #'h/get-reference conn)}
                      :put {:summary "Update an existing reference"
                            :handler (partial #'h/update-reference conn)}
                      :delete {:summary "Delete a reference"
                               :handler (partial #'h/delete-reference conn)}}]])