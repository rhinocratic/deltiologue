(ns rhinocratic.deltiologue.web.api.handlers.tags
  (:require
   [clojure.string :as string]
   [reitit.core :as r]
   [rhinocratic.deltiologue.db.queries.tags :as q]
   ;;  [rhinocratic.deltiologue.web.api.routes :as rts]
   [rhinocratic.deltiologue.web.api.handlers.util :as u]))

(defn tags
  [conn _req]
  "Fetch all tags"
  (let [tags (q/tags conn)]
    {:status 200 :body tags}))