(ns rhinocratic.deltiologue.web.api.handlers.notes
  (:require
   [clojure.string :as string]
   [reitit.core :as r]
   [rhinocratic.deltiologue.db.queries.notes :as q]
   ;;  [rhinocratic.deltiologue.web.api.routes :as rts]
   [rhinocratic.deltiologue.web.api.handlers.util :as u]))

(defn- map-by-initial
  [m {:keys [:note/title] :as note}]
  (let [initial (str (first title))
        k (if (re-matches #"[0-9]" initial) "0-9" initial)]
    (update m k #((fnil conj []) %1 %2) note)))

(defn- sort-note-summaries
  [summaries]
  (->> summaries
       (sort-by :note/title)
       (reduce map-by-initial (sorted-map))))

(defn note-summaries
  "Fetch summaries of all notes"
  [conn _req]
  (let [summaries (q/note-summaries conn)
        sorted (sort-note-summaries summaries)]
    (tap> sorted)
    {:status 200 :body sorted}))

(defn note
  "Fetch a note by ID"
  [conn req]
  (let [note-id (get-in req [:parameters :path :note-id])
        note (q/note conn note-id)]
    {:status 200 :body note}))