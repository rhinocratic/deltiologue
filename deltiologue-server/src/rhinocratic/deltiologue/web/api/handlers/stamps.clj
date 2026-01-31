(ns rhinocratic.deltiologue.web.api.handlers.stamps
  (:require
   [clojure.string :as string]
   [reitit.core :as r]
   [rhinocratic.deltiologue.db.queries.stamps :as q]
   ;;  [rhinocratic.deltiologue.web.api.routes :as rts]
   [rhinocratic.deltiologue.web.api.handlers.util :as u]))

(defn stamps
  "Fetch all stamps"
  [conn _req]
  (let [stamps (q/stamps conn)]
    {:status 200 :body stamps}))