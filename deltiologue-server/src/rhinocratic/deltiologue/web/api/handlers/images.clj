(ns rhinocratic.deltiologue.web.api.handlers.images
  (:require
   [clojure.string :as string]
   [reitit.core :as r]
   [rhinocratic.deltiologue.db.queries.cards :as q]
   ;;  [rhinocratic.deltiologue.web.api.routes :as rts]
   [rhinocratic.deltiologue.web.api.handlers.util :as u]))

(defn upload-temp-image
  "Upload an image to a temporary location"
  [{{{:keys [file]} :multipart} :parameters}]
  {:status 200
   :body {:name (:filename file)
          :size (:size file)}})