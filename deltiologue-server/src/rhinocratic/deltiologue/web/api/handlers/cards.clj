(ns rhinocratic.deltiologue.web.api.handlers.cards
  (:require
   [clojure.string :as string]
   [reitit.core :as r]
   [rhinocratic.deltiologue.db.queries.cards :as q]
   ;;  [rhinocratic.deltiologue.web.api.routes :as rts]
   [rhinocratic.deltiologue.web.api.handlers.util :as u]))

(defn card
  "Fetch a postcard by number"
  [conn req]
  (let [card-id (get-in req [:parameters :path :card-no])
        card (q/card conn card-id)]
    {:status 200 :body card}))