(ns rhinocratic.deltiologue.web.api.routes.notes
  (:require
   [rhinocratic.deltiologue.web.api.handlers.notes :as h]))

(defn routes
  [conn]
  ["/notes" {:swagger {:tags ["notes"]}}
   ["" {:name ::note-summaries
        :get {:summary "Fetch summaries of all notes"
              :handler (partial #'h/all-note-summaries conn)}
        :post {:summary "Create a new note"
               :handler (partial #'h/new-note conn)}}]
   ["/:note-id"
    ["" {:name ::note
         :parameters {:path {:note-id int?}}
         :get {:summary "Fetch a note by ID"
               :handler (partial #'h/get-note conn)}
         :put {:summary "Update an existing note"
               :handler (partial #'h/update-note conn)}
         :delete {:summary "Delete a note"
                  :handler (partial #'h/delete-note conn)}}]]])