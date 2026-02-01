(ns rhinocratic.deltiologue.web.api.routes.references
  (:require
   [clojure.spec.alpha :as s]
   [rhinocratic.deltiologue.web.api.handlers.references :as h]))

(s/def ::id int?)
(s/def ::idx int?)
(s/def ::medium (s/nilable string?))
(s/def ::accessed (s/nilable inst?))
(s/def ::source (s/nilable string?))
(s/def ::title (s/nilable string?))
(s/def ::issue_date (s/nilable inst?))
(s/def ::issue_note (s/nilable string?))
(s/def ::available (s/nilable string?))
(s/def ::new-reference
  (s/keys :req-un [::idx]
          :opt-un [::medium
                   ::accessed
                   ::source
                   ::title
                   ::issue_date
                   ::available]))
(s/def ::reference
  (s/keys :req-un [::id
                   ::idx]
          :opt-un [::medium
                   ::accessed
                   ::source
                   ::title
                   ::issue_date
                   ::available]))
(s/def ::reference-list
  (s/coll-of ::reference :into []))

(defn routes
  [conn]
  ["/references" {:swagger {:tags ["references"]}}
   ["" {:name ::reference-list
        :get {:summary "Fetch all references"
              :handler (partial #'h/references conn)
              :responses {200 {:body ::reference-list}}}
        :post {:summary "Create a new reference"
               :handler (partial #'h/new-reference conn)
               :parameters {:body ::new-reference}
               :responses {200 {:body ::reference}}}}]
   ["/:reference-id" {:name ::reference
                      :parameters {:path {:reference-id int?}}
                      :get {:summary "Fetch a reference by ID"
                            :handler (partial #'h/get-reference conn)
                            :responses {200 {:body ::reference}}}
                      :put {:summary "Update an existing reference"
                            :handler (partial #'h/update-reference conn)
                            :parameters {:body ::reference}
                            :responses {200 {:body ::reference}}}
                      :delete {:summary "Delete a reference"
                               :handler (partial #'h/delete-reference conn)
                               :responses {200 {:body ::reference}}}}]])