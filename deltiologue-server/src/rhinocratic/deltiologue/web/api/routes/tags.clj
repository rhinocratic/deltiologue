(ns rhinocratic.deltiologue.web.api.routes.tags
  (:require
   [rhinocratic.deltiologue.web.api.handlers.tags :as h]))

(defn routes
  [conn]
  ["/tags" {:swagger {:tags ["tags"]}}
   ["" {:name ::tag-list
        :get {:summary "Fetch all tags"
              :handler (partial #'h/all-tags conn)}
        :post {:summary "Create a new tag"
               :handler (partial #'h/new-tag conn)}}]
   ["/:tag-id" {:name ::tag
                :parameters {:path {:tag-id int?}}
                :get {:summary "Fetch a tag by ID"
                      :handler (partial #'h/get-tag conn)}
                :put {:summary "Update an existing tag"
                      :handler (partial #'h/update-tag conn)}
                :delete {:summary "Delete a tag"
                         :handler (partial #'h/delete-tag conn)}}]])