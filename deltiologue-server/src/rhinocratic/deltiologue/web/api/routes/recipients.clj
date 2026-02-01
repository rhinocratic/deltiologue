(ns rhinocratic.deltiologue.web.api.routes.recipients
  (:require
   [clojure.spec.alpha :as s]
   [rhinocratic.deltiologue.web.api.handlers.recipients :as h]))

(s/def ::id int?)
(s/def ::recipient_name string?)
(s/def ::recipient_address string?)
(s/def ::lat double?)
(s/def ::lng double?)
(s/def ::recipient_location
  (s/nilable
   (s/keys :req-un [::lat
                    ::lng])))
(s/def ::new-recipient
  (s/keys :req-un [::recipient_name
                   ::recipient_address]
          :opt-un [::recipient_location]))
(s/def ::recipient
  (s/keys :req-un [::id
                   ::recipient_name
                   ::recipient_address]
          :opt-un [::recipient_location]))
(s/def ::recipient-list
  (s/coll-of ::recipient :into []))

(defn routes
  [conn]
  ["/recipients" {:swagger {:tags ["recipients"]}}
   ["" {:name ::recipient-list
        :get {:summary "Fetch all recipients"
              :handler (partial #'h/all-recipients conn)
              :responses {200 {:body ::recipient-list}}}
        :post {:summary "Create a new recipient"
               :handler (partial #'h/new-recipient conn)
               :parameters {:body ::new-recipient}
               :responses {201 {:body ::recipient}}}}]
   ["/:recipient-id"
    ["" {:name ::recipient
         :parameters {:path {:recipient-id int?}}
         :get {:summary "Fetch a recipient by ID"
               :handler (partial #'h/get-recipient conn)
               :responses {200 {:body ::recipient}}}
         :put {:summary "Update an existing recipient"
               :handler (partial #'h/update-recipient conn)
               :parameters {:body ::recipient}
               :responses {200 {:body ::recipient}}}
         :delete {:summary "Delete a recipient"
                  :handler (partial #'h/delete-recipient conn)
                  :responses {200 {:body ::recipient}}}}]]])