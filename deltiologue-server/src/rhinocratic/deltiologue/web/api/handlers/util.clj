(ns rhinocratic.deltiologue.web.api.handlers.util
  (:require
   [reitit.core :as r]))

(defn error?
  "True if the given value represents an error, false otherwise"
  [value]
  (contains? value :error))

(defn with-status
  "Attach a status code to the result of a handler operation"
  ([item]
   (with-status item 200))
  ([item status]
   (cond
     (error? item) (:error item)
     (nil? item) {:status 404}
     :else {:status status :body item})))

(defn make-location
  "Given an incoming POST request, a route name and the path parameters
   for a newly-created resource, create a location header for the response."
  [req route-name path-params]
  (tap> [route-name path-params])
  (let [{:keys [scheme server-name server-port]} req
        router (::r/router req)
        path (-> router
                 (r/match-by-name route-name path-params)
                 :path)]
    (->> path
         (format "%s://%s:%s%s" (name scheme) server-name server-port)
         (hash-map "location"))))

(comment

  (require '[reitit.ring :as ring])

  (let [handler (:rhinocratic.deltiologue/app (user/system))
        req {:scheme "https"
             :server-name "localhost"
             :server-port 8080
             ::r/router handler}]
    (tap> (ring/get-router (handler {:request-method :get :uri "/favicon.ico"}))))


  #_())