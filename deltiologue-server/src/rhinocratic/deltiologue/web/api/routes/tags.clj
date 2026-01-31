(ns rhinocratic.deltiologue.web.api.routes.tags
  (:require
   [rhinocratic.deltiologue.web.api.handlers.tags :as h]))

(defn routes
  [conn]
  ["/tags" {:swagger {:tags ["tags"]}}
   ["" {:name ::tags
        :get {:handler (partial #'h/tags conn)
              :summary "Fetch all tags"}}]])