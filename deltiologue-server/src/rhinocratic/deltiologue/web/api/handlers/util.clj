(ns rhinocratic.deltiologue.web.api.handlers.util)

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
  "Given an incoming POST request and a relative path to a newly-created resource,
   create a location header for the response."
  [req relative-path]
  (let [{:keys [scheme server-name server-port]} req]
    (->> relative-path
         (format "%s://%s:%s%s" (name scheme) server-name server-port)
         (hash-map "location"))))