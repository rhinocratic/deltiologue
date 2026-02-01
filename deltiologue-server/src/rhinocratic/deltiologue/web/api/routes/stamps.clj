(ns rhinocratic.deltiologue.web.api.routes.stamps
  (:require
   [clojure.spec.alpha :as s]
   [rhinocratic.deltiologue.web.api.handlers.stamps :as h]))

(s/def ::id int?)
(s/def ::stamp_description string?)
(s/def ::new-stamp
  (s/keys :req-un [::stamp_description]))
(s/def ::stamp
  (s/keys :req-un [::id
                   ::stamp_description]))
(s/def ::stamp-list
  (s/coll-of ::stamp :into []))

(defn routes
  [conn]
  ["/stamps" {:swagger {:tags ["stamps"]}}
   ["" {:name ::stamp-list
        :get {:summary "Fetch all stamps"
              :handler (partial #'h/all-stamps conn)
              :responses {200 {:body ::stamp-list}}}
        :post {:summary "Create a new stamp"
               :handler (partial #'h/new-stamp conn)
               :parameters {:body ::new-stamp}
               :responses {201 {:body ::stamp}}}}]
   ["/:stamp-id" {:name ::stamp
                  :parameters {:path {:stamp-id int?}}
                  :get {:summary "Fetch a stamp by ID"
                        :handler (partial #'h/get-stamp conn)
                        :responses {200 {:body ::stamp}}}
                  :put {:summary "Update an existing stamp"
                        :handler (partial #'h/update-stamp conn)
                        :parameters {:body ::stamp}
                        :responses {200 {:body ::stamp}}}
                  :delete {:summary "Delete a stamp"
                           :handler (partial #'h/delete-stamp conn)
                           :responses {200 {:body ::stamp}}}}]])