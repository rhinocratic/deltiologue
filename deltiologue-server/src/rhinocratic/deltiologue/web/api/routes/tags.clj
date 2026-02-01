(ns rhinocratic.deltiologue.web.api.routes.tags
  (:require
   [clojure.spec.alpha :as s]
   [rhinocratic.deltiologue.web.api.handlers.tags :as h]))

(s/def ::id int?)
(s/def ::tag_name string?)
(s/def ::display_text string?)
(s/def ::new-tag
  (s/keys :req-un [::display_text]))
(s/def ::tag
  (s/keys :req-un [::id
                   ::tag_name
                   ::display_text]))
(s/def ::tag-list
  (s/coll-of ::tag :into []))

(defn routes
  [conn]
  ["/tags" {:swagger {:tags ["tags"]}}
   ["" {:name ::tag-list
        :get {:summary "Fetch all tags"
              :handler (partial #'h/all-tags conn)
              :responses {200 {:body ::tag-list}}}
        :post {:summary "Create a new tag"
               :handler (partial #'h/new-tag conn)
               :parameters {:body ::new-tag}
               :responses {201 {:body ::tag}}}}]
   ["/:tag-id" {:name ::tag
                :parameters {:path {:tag-id int?}}
                :get {:summary "Fetch a tag by ID"
                      :handler (partial #'h/get-tag conn)
                      :responses {200 {:body ::tag}}}
                :put {:summary "Update an existing tag"
                      :handler (partial #'h/update-tag conn)
                      :parameters {:body ::tag}
                      :responses {200 {:body ::tag}}}
                :delete {:summary "Delete a tag"
                         :handler (partial #'h/delete-tag conn)
                         :responses {200 {:body ::tag}}}}]])