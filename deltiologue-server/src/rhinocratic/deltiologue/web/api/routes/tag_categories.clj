(ns rhinocratic.deltiologue.web.api.routes.tag-categories
  (:require
   [rhinocratic.deltiologue.web.api.handlers.tag-categories :as h]))

(defn routes
  "Routes for tag-category related functionality"
  [conn]
  ["/tag-categories" {:swagger {:tags ["tag categories"]}}
   ["" {:name ::tag-categories
        :get {:handler (partial #'h/all-tag-categories conn)
              :summary "Fetch all tag categories"}
        :post {:handler (partial #'h/new-tag-category conn)
               :summary "Create a tag category"
               :parameters {:body {:display-text string?}}
               :responses {201 {:body {:tag_category/id int?
                                       :tag_category/display_text string?
                                       :tag_category/category_name string?}}}}}]
   ["/:tag-category-id" {:name ::tag-category
                         :parameters {:path {:tag-category-id int?}}
                         :get {:handler (partial #'h/get-tag-category conn)
                               :summary "Fetch a tag category by ID"}
                         :put {:handler (partial #'h/update-tag-category conn)
                               :summary "Update a tag category"}
                         :delete {:handler (partial #'h/delete-tag-category conn)
                                  :summary "Delete a tag category"}}]])