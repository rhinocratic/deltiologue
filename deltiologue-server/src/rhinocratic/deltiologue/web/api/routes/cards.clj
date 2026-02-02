(ns rhinocratic.deltiologue.web.api.routes.cards
  (:require
   [clojure.spec.alpha :as s]
   [rhinocratic.deltiologue.web.api.handlers.cards :as h]))

(defn colour?
  [s]
  (re-matches #"^[A-Fa-f0-9]{6}$" s))

;; Embedded tags
(s/def ::tag_id int?)
(s/def ::tag_name string?)
(s/def ::tag_text string?)
(s/def ::category_id int?)
(s/def ::category_name string?)
(s/def ::category_text string?)
(s/def ::colour (s/and string? colour?))
(s/def ::tag
  (s/keys :req-un [::tag_id
                   ::tag_name
                   ::tag_text
                   ::category_id
                   ::category_name
                   ::category_text
                   ::colour]))
(s/def ::tags
  (s/coll-of ::tag :into []))

;; Location
(s/def ::lat double?)
(s/def ::lng double?)
(s/def ::location
  (s/nilable
   (s/keys :req-un [::lat
                    ::lng])))

;; Subject
(s/def ::description string?)
(s/def ::current_view string?)
(s/def ::subject
  (s/keys :req-un [::description
                   ::location
                   ::current_view]))

;; Publisher
(s/def ::name string?)
(s/def ::publisher
  (s/keys :req-un [::id
                   ::name]))

;; Series
(s/def ::name string?)
(s/def ::entry (s/nilable string?))
(s/def ::series
  (s/keys :req-un [::id
                   ::name
                   ::entry]))

;; Stamps
(defn condition?
  [s]
  (#{"intact" "partially removed" "damaged" "badly damaged"} s))
(s/def ::condition (s/and string? condition?))
(s/def ::stamp
  (s/keys :req-un [::id
                   ::description
                   ::condition]))
(s/def ::stamps
  (s/coll-of ::stamp :into []))

;; Flags
(s/def ::rp boolean?)
(s/def ::used boolean?)
(s/def ::posted boolean?)
(s/def ::franked boolean?)
(s/def ::divided_back boolean?)
(s/def ::flags
  (s/keys :req-un [::rp
                   ::used
                   ::posted
                   ::franked
                   ::divided_back]))

;; Images
(s/def ::alt string?)
(s/def ::image
  (s/keys :req-un [::id
                   ::alt]))
(s/def ::front ::image)
(s/def ::rear ::image)
(s/def ::thumb ::image)
(s/def ::images
  (s/keys :req-un [::front
                   ::rear
                   ::thumb]))

;; Recipient
(s/def ::address string?)
(s/def ::recipient
  (s/nilable
   (s/keys :req-un [::id
                    ::name
                    ::address
                    ::location])))

;; The card
(s/def ::id int?)
(s/def ::index int?)
(s/def ::notes string?)
(s/def ::card
  (s/keys :req-un [::id
                   ::index
                   ::images
                   ::subject
                   ::publisher
                   ::recipient
                   ::series
                   ::stamps
                   ::flags
                   ::tags
                   ::notes]))
(s/def ::new-card
  (s/keys :req-un [::index
                   ::images
                   ::subject
                   ::publisher
                   ::recipient
                   ::series
                   ::stamps
                   ::flags
                   ::tags
                   ::notes]))

;; Summary
(s/def ::card-summary
  (s/keys :req-un [::id
                   ::index
                   ::description
                   ::image]))
(s/def ::card-summary-list
  (s/coll-of ::card-summary :into []))

(defn routes
  [conn]
  ["/cards" {:swagger {:tags ["cards"]}}
   ["" {:name ::card-list
        :get {:summary "Fetch all card summaries"
              :handler (partial #'h/all-card-summaries conn)
              :responses {200 {:body ::card-summary-list}}}
        :post {:summary "Create a new card"
               :handler (partial #'h/new-card conn)
               #_#_:parameters {:body ::new-card}
               #_#_:responses {201 {:body ::card}}}}]
   ["/:card-id" {:name ::card
                 :parameters {:path {:card-id int?}}
                 :get {:summary "Fetch a card by ID"
                       :handler (partial #'h/get-card conn)
                       :responses {200 {:body ::card}}}
                 :put {:summary "Update a card by ID"
                       :handler (partial #'h/update-card conn)
                       #_#_:parameters {:body ::card}
                       #_#_:responses {200 {:body ::card}}}
                 :delete {:summary "Delete a card"
                          :handler (partial #'h/delete-card conn)
                          #_#_:responses {200 {:body ::card}}}}]])