(ns rhinocratic.deltiologue.web.server
  (:require
   [ring.adapter.jetty :as jetty]))

(defn start
  [{:keys [port join app]}]
  (jetty/run-jetty app {:port port
                        :join? join}))