(ns rhinocratic.deltiologue.web.api.routes.cards
  (:require
   [rhinocratic.deltiologue.web.api.handlers.cards :as h]))

(defn routes
  [conn]
  ["/cards" {:swagger {:tags ["cards"]}}
   #_["" {:name ::card-summary
          :get {:handler (partial #'h/card-summaries conn)
                :summary "Fetch a summary of all cards"}}]
   ["/:card-no" {:name ::card
                 :parameters {:path {:card-no int?}}
                 :get {:handler (partial #'h/card conn)}
                 :summary "Fetch a card by number"}]])