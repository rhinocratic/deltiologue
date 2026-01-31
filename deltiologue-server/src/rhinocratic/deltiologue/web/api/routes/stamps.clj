(ns rhinocratic.deltiologue.web.api.routes.stamps
  (:require
   [rhinocratic.deltiologue.web.api.handlers.stamps :as h]))

(defn routes
  [conn]
  ["/stamps" {:swagger {:tags ["stamps"]}}
   ["" {:name ::stamps
        :get {:handler (partial #'h/stamps conn)
              :summary "Fetch all stamps"}}]])