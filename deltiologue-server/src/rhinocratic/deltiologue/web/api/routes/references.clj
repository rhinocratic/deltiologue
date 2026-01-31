(ns rhinocratic.deltiologue.web.api.routes.references
  (:require
   [rhinocratic.deltiologue.web.api.handlers.references :as h]))

(defn routes
  [conn]
  ["/references" {:swagger {:tags ["references"]}}
   ["" {:name ::references
        :get {:handler (partial #'h/references conn)
              :summary "Fetch all references"}}]])