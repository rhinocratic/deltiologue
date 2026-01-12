(ns rhinocratic.deltiologue.web.api.auth
  (:require
   [buddy.core.keys :as keys]
   [buddy.sign.jws :as jws]
   [buddy.auth :as buddy-auth]
   [buddy.auth.backends :as backends]
   [buddy.auth.middleware :as buddy-auth-middleware]))

(def default-backend
  (let [backend-opts {:options    {:alg :rs256}
                      :token-name "Bearer"
                      :secret     (keys/public-key "resources/ZmxY_91hvXPwBFY9u5FH6.pem")}]
    (backends/jws backend-opts)))

(defn make-token-auth-middleware
  "Return a middleware for use on routes requiring token authentication."
  ([]
   (make-auth-middleware default-backend))
  ([backend]
   (fn [handler]
     (buddy-auth-middleware/wrap-authentication handler backend))))

(defn require-auth
  "Middleware used in routes that require authentication. If the request is
  not authenticated a 401 response will be returned."
  [handler]
  (fn [request]
    (if (buddy-auth/authenticated? request)
      (handler request)
      {:status 401
       :body {:error "Unauthorized"
              :req (str request)}})))

(defn require-edit-permission
  "Middleware used on routes requiring edit permission."
  [handler]
  (fn [request]
    (if (-> request :identity :permissions set (contains? "edit"))
      (handler request)
      {:status 403 :body {:error "Edit permission required" :req (:identity request)}})))

(def admin-middleware (comp (make-token-auth-middleware)
                            require-auth
                            require-edit-permission))
