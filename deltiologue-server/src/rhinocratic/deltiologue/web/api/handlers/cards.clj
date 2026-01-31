(ns rhinocratic.deltiologue.web.api.handlers.cards
  (:require
   [clojure.string :as string]
   [reitit.core :as r]
   [rhinocratic.deltiologue.db.queries.cards :as q]
   ;;  [rhinocratic.deltiologue.web.api.routes :as rts]
   [rhinocratic.deltiologue.web.api.handlers.util :as u]))

(defn get-card
  "Fetch a card by ID"
  [conn req]
  (let [card-id (get-in req [:parameters :path :card-id])
        card (q/card conn card-id)]
    {:status 200 :body card}))

(defn new-card
  "Create a new card"
  [conn req]
  (let [card (get-in req [:body-params])
        saved-card (q/save-card conn card)
        saved-card-id (:postcard/id saved-card)
        route-name :rhinocratic.deltiologue.web.api.routes.cards/card
        path-params {:card-id saved-card-id}]
    {:status 201
     :body saved-card
     :headers (u/make-location req route-name path-params)}))

(defn update-card
  "Update an existing card"
  [conn req]
  (let [card (get-in req [:body-params])
        saved-card (q/save-card conn card)]
    {:status 200
     :body saved-card}))

(defn delete-card
  "Delete a card"
  [conn req]
  (let [card-id (get-in req [:parameters :path :card-id])
        deleted (q/delete-card conn card-id)]
    {:status 200
     :body deleted}))