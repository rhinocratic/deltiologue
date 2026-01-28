(ns rhinocratic.deltiologue.web.api.routes
  (:require
   [reitit.ring.middleware.multipart :as multipart]
   [clojure.spec.alpha :as s]
   [com.brunobonacci.mulog :as u]
   [rhinocratic.deltiologue.web.api.handlers :as h]))

(defn routes
  [conn]
  [["/image" {:swagger {:tags ["files"]}}
    ["/upload"
     ["/temp" {:post {:summary "upload an image to a temporary location"
                      :parameters {:multipart {:file multipart/temp-file-part}}
                      :responses {200 {:body {:name string?, :size int?}}}
                      :handler #'h/upload-temp-image}}]]]
   ["/card" {:swagger {:tags ["cards"]}}
    ["" {:name ::card-summary
         :get {:handler (partial #'h/card-summary conn)
               :summary "Fetch a summary of all cards"}}]
    ["/:card-no/detail" {:name ::card-detail
                         :parameters {:path {:card-no int?}}
                         :get {:handler (partial #'h/card conn)}
                         :summary "Fetch a card by number"}]]
   ["/note" {:swagger {:tags ["notes"]}}
    ["" {:name ::note-summaries
         :get {:handler (partial #'h/note-summaries conn)
               :summary "Fetch summaries of all notes"}}]
    ["/:note-id" {:name (keyword (str *ns*) "note")
                  :parameters {:path {:note-id int?}}
                  :get {:handler (partial #'h/note conn)
                        :summary "Fetch a note"}}]]
   ["/reference" {:swagger {:tags ["references"]}}
    ["" {:name ::references
         :get {:handler (partial #'h/references conn)
               :summary "Fetch all references"}}]]
   ["/search" {:swagger {:tags ["search"]}}
    ["/:q" {:name ::search
            :parameters {:path {:q string?}}
            :get {:handler (partial #'h/search conn)}
            :summary "Search for cards"}]]])
