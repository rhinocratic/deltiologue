(ns rhinocratic.deltiologue.web.api.routes.images
  (:require
   [reitit.ring.middleware.multipart :as multipart]
   [rhinocratic.deltiologue.web.api.handlers.images :as h]))

(defn routes
  [conn]
  ["/images" {:swagger {:tags ["images"]}}
   ["/upload" {:post {:summary "upload an image to a temporary location"
                      :parameters {:multipart {:file multipart/temp-file-part}}
                      :responses {201 {:body {:name string?, :size int?}}}
                      :handler #'h/upload-temp-image}}]])