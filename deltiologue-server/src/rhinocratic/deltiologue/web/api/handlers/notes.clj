(ns rhinocratic.deltiologue.web.api.handlers.notes
  (:require
   [clojure.string :as string]
   [reitit.core :as r]
   [rhinocratic.deltiologue.db.queries.notes :as q]
   [rhinocratic.deltiologue.web.api.handlers.util :as u]))

(defn- map-by-initial
  [m {:keys [:title] :as note}]
  (let [initial (str (first title))
        k (if (re-matches #"[0-9]" initial) "0-9" initial)]
    (update m k #((fnil conj []) %1 %2) note)))

(defn- sort-note-summaries
  [summaries]
  (->> summaries
       (sort-by :title)
       (reduce map-by-initial (sorted-map))))

(defn all-note-summaries
  "Fetch summaries of all notes"
  [conn _req]
  (let [summaries (q/all-note-summaries conn)
        sorted (sort-note-summaries summaries)]
    {:status 200 :body sorted}))

(defn get-note
  "Fetch a note by ID"
  [conn req]
  (let [note-id (get-in req [:parameters :path :note-id])
        note (q/get-note conn note-id)]
    (tap> note)
    (if note
      {:status 200 :body note}
      {:status 404})))

(defn new-note
  "Create a new note"
  [conn req]
  (let [note (get-in req [:body-params])
        saved-note (q/save-note conn note)
        saved-note-id (:note/id saved-note)
        route-name :rhinocratic.deltiologue.web.api.routes.notes/note
        path-params {:note-id saved-note-id}]
    {:status 201
     :body saved-note
     :headers (u/make-location req route-name path-params)}))

(defn update-note
  "Update an existing note"
  [conn req]
  (let [note (get-in req [:body-params])
        saved-note (q/save-note conn note)]
    {:status 200
     :body saved-note}))

(defn delete-note
  "Delete a note"
  [conn req]
  (let [note-id (get-in req [:parameters :path :note-id])
        deleted (q/delete-note conn note-id)]
    {:status 200
     :body deleted}))
