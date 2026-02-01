(ns rhinocratic.deltiologue.web.api.routes.notes
  (:require
   [clojure.spec.alpha :as s]
   [rhinocratic.deltiologue.web.api.handlers.notes :as h]))

(s/def ::id int?)
(s/def ::title string?)
(s/def ::body string?)
(s/def ::note-summary
  (s/keys :req-un [::id
                   ::title]))
(s/def ::note-summary-section-key string?)
(s/def ::note-summary-section-items
  (s/coll-of ::note-summary :into []))
(s/def ::note-summary-index
  (s/map-of ::note-summary-section-key
            ::note-summary-section-items))
(s/def ::new-note
  (s/keys :req-un [::title
                   ::body]))
(s/def ::note
  (s/keys :req-un [::id
                   ::title
                   ::body]))

(defn routes
  [conn]
  ["/notes" {:swagger {:tags ["notes"]}}
   ["" {:name ::note-summaries
        :get {:summary "Fetch summaries of all notes"
              :handler (partial #'h/all-note-summaries conn)
              :responses {200 {:body ::note-summary-index}}}
        :post {:summary "Create a new note"
               :handler (partial #'h/new-note conn)
               :parameters {:body ::new-note}
               :responses {201 {:body ::note}}}}]
   ["/:note-id"
    ["" {:name ::note
         :parameters {:path {:note-id int?}}
         :get {:summary "Fetch a note by ID"
               :handler (partial #'h/get-note conn)
               :responses {200 {:body ::note}}}
         :put {:summary "Update an existing note"
               :handler (partial #'h/update-note conn)
               :parameters {:body ::note}
               :responses {200 {:body ::note}}}
         :delete {:summary "Delete a note"
                  :handler (partial #'h/delete-note conn)
                  :responses {200 {:body ::note}}}}]]])