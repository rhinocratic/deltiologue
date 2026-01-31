(ns rhinocratic.deltiologue.web.api.handlers.references
  (:require
   [clojure.string :as string]
   [reitit.core :as r]
   [rhinocratic.deltiologue.db.queries.references :as q]
   ;;  [rhinocratic.deltiologue.web.api.routes :as rts]
   [rhinocratic.deltiologue.web.api.handlers.util :as u]))

(defn references
  "Fetch all references"
  [conn _req]
  (let [references (q/references conn)]
    {:status 200
     :body references}))