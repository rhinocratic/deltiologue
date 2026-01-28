(ns rhinocratic.deltiologue.web.api.handlers
  (:require
   [clojure.string :as string]
   [reitit.core :as r]
   [rhinocratic.deltiologue.db.queries :as q]
   [cheshire.core :as json]))

(defn error?
  [value]
  (contains? value :error))

(defn with-status
  "Attach a status code to the result of a handler operation"
  ([item]
   (with-status item 200))
  ([item status]
   (cond
     (error? item) (:error item)
     (nil? item) {:status 404}
     :else {:status status :body item})))

(defn card-summary
  "Fetch all card summaries"
  [conn _req]
  []
  (let [summaries (q/card-summary conn)]
    (with-status summaries)))

(defn card
  "Fetch a postcard by number"
  [conn req]
  (let [card-id (get-in req [:parameters :path :card-no])
        card (q/card conn card-id)]
    (with-status card)))

(defn- map-by-initial
  [m {:keys [title] :as note}]
  (let [initial (str (first title))
        k (if (re-matches #"[0-9]" initial) "0-9" initial)]
    (update m k #((fnil conj []) %1 %2) note)))

(defn- sort-note-summaries
  [summaries]
  (->> summaries
       (sort-by :title)
       (reduce map-by-initial (sorted-map))))

(defn note-summaries
  "Fetch summaries of all notes"
  [conn _req]
  (let [summary (q/note-summaries conn)
        sorted (sort-note-summaries summary)]
    {:status 200 :body sorted}))

(defn note
  "Fetch a note by ID"
  [conn req]
  (let [note-id (get-in req [:parameters :path :note-id])
        note (q/note conn note-id)]
    (with-status note)))

(defn references
  "Fetch all references"
  [conn _req]
  (let [references (q/references conn)]
    {:status 200
     :body references}))

(defn search
  "Search for cards"
  [conn req]
  (let [terms (get-in req [:parameters :path :q])
        results (q/search conn terms)]
    (with-status results)))

(defn upload-temp-image
  "Upload an image to a temporary location"
  [{{{:keys [file]} :multipart} :parameters}]
  {:status 200
   :body {:name (:filename file)
          :size (:size file)}})

(comment

  (let [conn (:rhinocratic.deltiologue/db (user/system))]
    (->> (references conn nil)
         tap>))

  #_())


;;   (defn fetch-all
;;     "Fetch all rows from the DB"
;;     [db table _req]
;;     (let [items (q/select-all db table)]
;;       (with-status items)))

;;   (defn fetch-one
;;     "Fetch a single item from the DB table"
;;     [db table req]
;;     (let [id (get-in req [:parameters :path :id])
;;           item (q/select-one db table id)]
;;       (with-status item)))

;;   (defn delete
;;     "Delete a single item from the DB"
;;     [db table req]
;;     (let [id (get-in req [:parameters :path :id])
;;           item (q/delete db table id)]
;;       (with-status item)))

;;   (defn location
;;     [table item router reverse-route-name]
;;     (->> item
;;          ((q/primary-key table))
;;          (assoc {} :id)
;;          (r/match-by-name router reverse-route-name)
;;          :path))

;;   (defn with-location
;;     [item location]
;;     (assoc item :headers {"Location" location}))

;;   (defn create
;;     "Create a new item in the DB"
;;     [db table reverse-route-name {:keys [body-params] ::r/keys [router]}]
;;     (let [row (table body-params)
;;           item (q/create db table row)
;;           location (location table item router reverse-route-name)]
;;       (-> item
;;           (with-status 201)
;;           (with-location location))))

;;   (defn edit
;;     "Update a single item in the DB"
;;     [db table {:keys [body-params] :as req}]
;;     (let [row (table body-params)
;;           id (get-in req [:parameters :path :id])
;;           item (q/edit db table id row)]
;;       (with-status item)))

;;   (defn fetch-all-suppliers-for-fud-item
;;     [db req]
;;     (let [fud-item-id (get-in req [:parameters :path :fud-item-id])
;;           items (q/fetch-all-suppliers-for-fud-item db fud-item-id)]
;;       (with-status items)))

;;   (defn add-supplier-for-fud-item
;;     [db {::r/keys [router] :as req}]
;;     (let [fud-item-id (get-in req [:parameters :path :fud-item-id])
;;           supplier-id (get-in req [:parameters :body :supplier-id])
;;           item-supplier (q/add-supplier-for-fud-item db fud-item-id supplier-id)
;;           location (r/match-by-name router :rhinocratic.fud.web.api.routes/fud_item_suppliers)]
;;       (->> item-supplier
;;            (with-status 201)
;;            (with-location location))))

;;   (defn delete-supplier-for-fud-item
;;     [db req]
;;     (let [fud-item-id (get-in req [:parameters :path :id])
;;           supplier-id (get-in req [:parameters :path :supplier-id])
;;           item-supplier (q/delete-supplier-for-fud-item db fud-item-id supplier-id)]
;;       (with-status item-supplier)))
