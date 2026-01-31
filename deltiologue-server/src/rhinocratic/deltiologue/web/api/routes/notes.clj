(ns rhinocratic.deltiologue.web.api.routes.notes
  (:require
   [rhinocratic.deltiologue.web.api.handlers.notes :as h]))

(defn routes
  [conn]
  ["/notes" {:swagger {:tags ["notes"]}}
   ["" {:name ::note-summaries
        :get {:handler (partial #'h/all-note-summaries conn)
              :summary "Fetch summaries of all notes"}
        :post {:handler (partial #'h/new-note conn)
               :summary "Create a new note"}}]
   ["/:note-id"
    ["" {:name ::note
         :parameters {:path {:note-id int?}}
         :get {:handler (partial #'h/get-note conn)
               :summary "Fetch a note by ID"}
         :put {:handler (partial #'h/update-note conn)
               :summary "Update an existing note"}
         :delete {:handler (partial #'h/delete-note conn)
                  :summary "Delete a note"}}]]])