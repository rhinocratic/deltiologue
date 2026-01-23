(ns rhinocratic.deltiologue.web.api.routes
  (:require
   [clojure.spec.alpha :as s]
   [com.brunobonacci.mulog :as u]
   [rhinocratic.deltiologue.web.api.handlers :as h]))

(defn routes
  [conn]
  [["/card/summary" {:name (keyword (str *ns*) "card-summary")
                     :get {:handler (partial #'h/fetch-all-card-summaries conn)
                           :summary "Fetch all card summaries"}}]
   ["/card/:card-no/detail" {:name (keyword (str *ns*) "card-detail")
                             :parameters {:path {:card-no int?}}
                             :get {:handler (partial #'h/fetch-card-by-number conn)}
                             :summary "Fetch a card by number"}]
   ["/search/:q" {:name (keyword (str *ns*) "search")
                  :parameters {:path {:q string?}}
                  :get {:handler (partial #'h/search conn)}
                  :summary "Search for cards"}]])
