(ns rhinocratic.deltiologue.web.api.routes.tags
  (:require
   [rhinocratic.deltiologue.web.api.handlers.tags :as h]))

(defn routes
  [conn]
  ["/tags" {:swagger {:tags ["tags"]}}
   ["" {:name ::tags
        :get {:handler (partial #'h/all-tags conn)
              :summary "Fetch all tags"}
        :post {:handler (partial #'h/new-tag conn)
               :summary "Create a new tag"}}]
   ["/:tag-id" {:name ::tag
                :parameters {:path {:tag-id int?}}
                :get {:handler (partial #'h/get-tag conn)
                      :summary "Fetch a tag by ID"}
                :put {:handler (partial #'h/update-tag conn)
                      :summary "Update an existing tag"}
                :delete {:handler (partial #'h/delete-tag conn)
                         :summary "Delete a tag"}}]])