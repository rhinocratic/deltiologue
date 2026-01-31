(ns rhinocratic.deltiologue.web.api.routes.notes
  (:require
   [rhinocratic.deltiologue.web.api.handlers.notes :as h]))

(defn routes
  [conn]
  ["/notes" {:swagger {:tags ["notes"]}}
   ["" {:name ::note-summaries
        :get {:handler (partial #'h/note-summaries conn)
              :summary "Fetch summaries of all notes"}}]
   ["/:note-id" {:name (keyword (str *ns*) "note")
                 :parameters {:path {:note-id int?}}
                 :get {:handler (partial #'h/note conn)
                       :summary "Fetch a note"}}]])