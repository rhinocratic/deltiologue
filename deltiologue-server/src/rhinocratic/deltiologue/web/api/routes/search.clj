(ns rhinocratic.deltiologue.web.api.routes.search
  (:require
   [rhinocratic.deltiologue.web.api.handlers.search :as h]))

(defn routes
  [conn]
  ["/search" {:swagger {:tags ["search"]}}
   ["" {:name ::search
        :parameters {:query {:q string?}}
        :get {:summary "Search for cards"
              :handler (partial #'h/search conn)}}]])