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
        :get {:handler (partial #'h/all-tag-categories conn)
              :summary "Fetch all tag categories"}
        :post {:handler (partial #'h/new-tag-category conn)
               :summary "Create a tag category"
               :parameters {:body ::new-tag-category}
               :responses {201 {:body ::tag-category}
                           :default {:body {:error string?}}}}}]
   ["/:tag-category-id" {:name ::tag-category
                         :parameters {:path {:tag-category-id int?}}
                         :get {:handler (partial #'h/get-tag-category conn)
                               :summary "Fetch a tag category by ID"
                               :responses {200 {:body ::tag-category}
                                           404 {:body nil}
                                           :default {:body {:error string?}}}}
                         :put {:handler (partial #'h/update-tag-category conn)
                               :summary "Update a tag category"
                               :parameters {:body ::tag-category}
                               :responses {200 {:body ::tag-category}
                                           :default {:body {:error string?}}}}
                         :delete {:handler (partial #'h/delete-tag-category conn)
                                  :summary "Delete a tag category"
                                  :responses {200 {:body ::tag-category}
                                              :default {:body {:error string?}}}}}]])