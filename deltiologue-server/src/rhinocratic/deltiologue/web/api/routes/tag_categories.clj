(ns rhinocratic.deltiologue.web.api.routes.tag-categories
  (:require
   [clojure.spec.alpha :as s]
   [rhinocratic.deltiologue.web.api.handlers.tag-categories :as h]))

(defn colour?
  [s]
  (re-matches #"^[A-Fa-f0-9]{6}$" s))

(s/def ::id int?)
(s/def ::display_text string?)
(s/def ::category_name string?)
(s/def ::colour (s/and string? colour?))
(s/def ::new-tag-category
  (s/keys :req-un [::display_text
                   ::colour]))
(s/def ::tag-category
  (s/keys :req-un [::id
                   ::category_name
                   ::display_text
                   ::colour]))

(defn routes
  [conn]
  ["/tag-categories" {:swagger {:tags ["tag categories"]}}
   ["" {:name ::tag-category-list
        :get {:summary "Fetch all tag categories"
              :handler (partial #'h/all-tag-categories conn)}
        :post {:summary "Create a tag category"
               :handler (partial #'h/new-tag-category conn)
               :parameters {:body ::new-tag-category}
               :responses {201 {:body ::tag-category}}}}]
   ["/:tag-category-id" {:name ::tag-category
                         :parameters {:path {:tag-category-id int?}}
                         :get {:summary "Fetch a tag category by ID"
                               :handler (partial #'h/get-tag-category conn)
                               :responses {200 {:body ::tag-category}}}
                         :put {:summary "Update a tag category"
                               :handler (partial #'h/update-tag-category conn)
                               :parameters {:body ::tag-category}
                               :responses {200 {:body ::tag-category}}}
                         :delete {:summary "Delete a tag category"
                                  :handler (partial #'h/delete-tag-category conn)
                                  :responses {200 {:body ::tag-category}}}}]])