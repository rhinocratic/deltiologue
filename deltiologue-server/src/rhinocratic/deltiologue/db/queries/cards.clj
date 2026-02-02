(ns rhinocratic.deltiologue.db.queries.cards
  (:require
   [clojure.set :as set]
   [clojure.string :as string]
   [next.jdbc :as jdbc]
   [next.jdbc.result-set :as rs]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]
   [clojure.datafy :as dat]
   [clojure.core.protocols :as prot]))

(def opts {:builder-fn rs/as-unqualified-maps})

;; Extend the Datafiable protocol to the objects returned in Postgres' geospatial columns
(extend-protocol prot/Datafiable
  net.postgis.jdbc.geometry.Point
  (datafy [p]
    {:lat (.y p)
     :lng (.x p)})
  net.postgis.jdbc.PGgeography
  (datafy [g]
    (dat/datafy (.getGeometry g))))

(defn transform-summary
  "Rearrange the content of a card summary"
  [summary]
  (-> summary
      (set/rename-keys {:collection_index :index
                        :subject_description :description
                        :image_thumb :image})
      (#(update % :image (fn [idx {:keys [description]}] {:id idx :alt description}) %))))

(defn all-card-summaries
  "Fetch all card summaries"
  [conn]
  (let [sql (-> (h/select :p/id
                          :p/collection-index
                          :p/subject-description
                          :p/image-thumb)
                (h/from [:postcard :p])
                (sql/format))]
    (->> (jdbc/execute! conn sql opts)
         (map transform-summary))))

(defn- card-tags
  "Fetch all tags associated with a card"
  [conn card-id]
  (let [sql (-> (h/select [:t/id :tag_id]
                          [:t/display-text :tag_text]
                          :t/tag-name
                          [:tc/id :category_id]
                          [:tc/display-text :category_text]
                          :tc/category-name
                          :tc/colour)
                (h/from [:postcard-tag :pt])
                (h/left-join [:tag :t] [:= :pt.tag-id :t.id])
                (h/left-join [:tag-category :tc] [:= :pt.tag-category-id :tc.id])
                (h/where [:= :pt.postcard-id card-id])
                (sql/format))]
    (jdbc/execute! conn sql opts)))

(defn- transform-stamp
  "Transform a stamp"
  [stamp]
  (-> stamp
      (set/rename-keys {:stamp_description :description
                        :stamp_condition :condition})))

(defn- card-stamps
  "Fetch all stamps associated with a card"
  [conn card-id]
  (let [sql (-> (h/select :s/id
                          :s/stamp-description
                          :ps/stamp-condition)
                (h/from [:postcard-stamp :ps])
                (h/left-join [:stamp :s] [:= :ps.stamp-id :s.id])
                (h/where [:= :ps.postcard-id card-id])
                (sql/format))]
    (->> (jdbc/execute! conn sql opts)
         (map transform-stamp))))

(defn transform-card
  "Reorganize the card details into a hierarchical structure"
  [card]
  (-> card
      (assoc :index (:collection_index card))
      (assoc :posted_date {:year (:posted_year card)
                           :month (:posted_month card)
                           :day (:posted_day card)
                           :date (:posted_date card)
                           :approximate (:posted_date_approximate card)})
      (assoc :publication_date {:year (:publication_year card)
                                :month (:publication_month card)
                                :day (:publication_day card)
                                :date (:publication_date card)
                                :approximate (:publication_date_approximate card)})
      (assoc :recipient (when (some? (:recipient card))
                          {:id (:recipient card)
                           :name (:recipient_name card)
                           :address (:recipient_address card)
                           :location (:recipient_location card)}))
      (assoc :publisher {:id (:publisher card)
                         :name (:publisher_name card)})
      (assoc :series {:id (:series card)
                      :name (:series_name card)
                      :entry (:series_entry card)})
      (assoc :images {:front {:id (:image_front card)
                              :alt (:image_front_alt card)}
                      :rear {:id (:image_rear card)
                             :alt (:image_rear_alt card)}
                      :thumb {:id (:image_thumb card)
                              :alt (:subject_description card)}})
      (assoc :flags {:rp (:rp card)
                     :used (:used card)
                     :posted (:posted card)
                     :franked (:franked card)
                     :divided_back (:divided_back card)})
      (assoc :subject {:description (:subject_description card)
                       :location (:subject_location card)
                       :current_view (:subject_current_view card)})
      (dissoc :collection_index
              :posted_year
              :posted_month
              :posted_day
              :posted_date
              :posted_date_approximate
              :publication_year
              :publication_month
              :publication_day
              :publication_date
              :publication_date_approximate
              :recipient_name
              :recipient_address
              :recipient_location
              :publisher_name
              :series_name
              :series_entry
              :image_front
              :image_front_alt
              :image_rear
              :image_rear_alt
              :image_thumb
              :rp
              :used
              :posted
              :franked
              :divided_back
              :subject_description
              :subject_location
              :subject_current_view)))

(defn get-card
  "Fetch a card by ID"
  [conn card-id]
  (let [sql (-> (h/select :p/id
                          :p/collection-index
                          :p/divided-back
                          :p/rp
                          :p/used
                          :p/posted
                          :p/franked
                          :p/image-front
                          :p/image-front-alt
                          :p/image-rear
                          :p/image-rear_alt
                          :p/image-thumb
                          :p/publication-year
                          :p/publication-month
                          :p/publication-day
                          :p/publication-date
                          :p/publication-date-approximate
                          :p/posted-year
                          :p/posted-month
                          :p/posted-day
                          :p/posted-date
                          :p/posted-date-approximate
                          :p/subject-description
                          :p/subject-location
                          :p/subject-current-view
                          :p/notes
                          :p/transcript
                          :p/publisher
                          :p/recipient
                          :p/series
                          :pb/publisher-name
                          :r/recipient-name
                          :r/recipient-address
                          :r/recipient-location
                          :s/series-name
                          :p/series-entry)
                (h/from [:postcard :p])
                (h/left-join [:publisher :pb] [:= :p.publisher :pb.id])
                (h/left-join [:recipient :r] [:= :p.recipient :r.id])
                (h/left-join [:series :s] [:= :p.series :s.id])
                (h/where [:= :p/id card-id])
                (sql/format))
        card-detail (-> (jdbc/execute-one! conn sql opts)
                        (update :subject_location dat/datafy)
                        (update :recipient_location dat/datafy))
        tags (card-tags conn card-id)
        stamps (card-stamps conn card-id)]
    (-> card-detail
        (assoc :tags tags
               :stamps stamps)
        transform-card)))

(comment

  (let [conn (:rhinocratic.deltiologue/db (user/system))]
    (tap> (get-card conn 42)))

  #_())

(defn- new-card
  "Create a new card"
  [conn card]
  (let [sql (-> (h/insert-into :note)
                (h/values [card])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql opts)))

(defn- update-card
  "Update an existing card"
  [conn card]
  (let [sql (-> (h/update [:postcard :p])
                (h/set (dissoc card :id))
                (h/where [:= :p/id (:id card)])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql opts)))

(defn save-card
  "Save a card"
  [conn card]
  (if (:id card)
    (update-card card)
    (new-card card)))

(defn delete-card
  "Delete a card"
  [conn card-id]
  (let [sql (-> (h/delete-from :postcard)
                (h/where [:= :id card-id])
                (h/returning :*)
                (sql/format))]
    (jdbc/execute-one! conn sql opts)))