(ns rhinocratic.deltiologue.web.api.handlers.search
  (:require
   [clojure.string :as string]
   [reitit.core :as r]
   [rhinocratic.deltiologue.db.queries.search :as q]
   [rhinocratic.deltiologue.web.api.handlers.util :as u]))

(defn search
  "Search for cards"
  [conn req]
  (let [terms (get-in req [:parameters :query :q])
        results (q/search conn terms)]
    {:status 200 :body results}))