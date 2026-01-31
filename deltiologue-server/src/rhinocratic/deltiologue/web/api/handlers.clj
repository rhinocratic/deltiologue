(ns rhinocratic.deltiologue.web.api.handlers
  (:require
   [clojure.string :as string]
   [reitit.core :as r]
   [cheshire.core :as json]
   [rhinocratic.deltiologue.db.queries :as q]
   [rhinocratic.deltiologue.web.api.routes :as rts]))

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

(defn make-location
  [req relative-path]
  (let [{:keys [scheme server-name server-port]} req]
    (->> relative-path
         (format "%s://%s:%s%s" (name scheme) server-name server-port)
         (hash-map "location"))))

(defn card-summary
  "Fetch all card summaries"
  [conn _req]
  (let [summaries (q/card-summary conn)]
    (with-status summaries)))

(defn card
  "Fetch a postcard by number"
  [conn req]
  (let [card-id (get-in req [:parameters :path :card-no])
        card (q/card conn card-id)]
    (with-status card)))

(defn card-tags
  [conn req]
  "Fetch tags for a single card"
  (let [card-id (get-in req [:parameters :path :card-no])
        tags (q/card-tags conn card-id)]
    (with-status tags)))

(defn tags
  [conn _req]
  "Fetch all tags"
  (let [tags (q/tags conn)]
    (with-status tags)))

(defn tag-categories
  [conn _req]
  "Fetch all tag categories"
  (let [categories (q/tag-categories conn)]
    (with-status categories)))

(defn tag-category
  [conn req]
  "Fetch a single tag category"
  (let [category-id (get-in req [:parameters :path :category-id])
        category (q/tag-category conn category-id)]
    (with-status category)))

(defn new-tag-category
  [conn req]
  "Create a new tag category"
  (let [category (get-in req [:body-params])
        router (::r/router req)
        saved-category (q/new-tag-category conn category)
        id (:tag_category/id saved-category)
        path (-> router
                 (r/match-by-name ::rts/tag-category {:category-id id})
                 :path)]
    {:status 201
     :body saved-category
     :headers (make-location req path)}))

(defn delete-tag-category
  [conn req]
  "Delete a tag category"
  (let [category-id (get-in req [:parameters :path :category-id])
        deleted (q/delete-tag-category conn category-id)]
    {:status 200
     :body deleted}))

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

(defn stamps
  "Fetch all stamps"
  [conn _req]
  (let [stamps (q/stamps conn)]
    (with-status stamps)))

(defn search
  "Search for cards"
  [conn req]
  (let [terms (get-in req [:parameters :query :q])
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
