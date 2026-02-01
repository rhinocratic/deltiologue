(ns rhinocratic.deltiologue.web.api.handlers.recipients
  (:require
   [clojure.string :as string]
   [reitit.core :as r]
   [rhinocratic.deltiologue.db.queries.recipients :as q]
   [rhinocratic.deltiologue.web.api.handlers.util :as u]))

(defn all-recipients
  "Fetch all recipients"
  [conn _req]
  (let [recipients (q/all-recipients conn)]
    {:status 200 :body recipients}))

(defn get-recipient
  "Fetch a recipient by ID"
  [conn req]
  (let [recipient-id (get-in req [:parameters :path :recipient-id])
        recipient (q/get-recipient conn recipient-id)]
    {:status 200 :body recipient}))

(defn new-recipient
  "Create a new recipient"
  [conn req]
  (let [recipient (get-in req [:body-params])
        saved-recipient (q/save-recipient conn recipient)
        saved-recipient-id (:recipient/id saved-recipient)
        route-name :rhinocratic.deltiologue.web.api.routes.recipients/recipient
        path-params {:recipient-id saved-recipient-id}]
    {:status 201
     :body saved-recipient
     :headers (u/make-location req route-name path-params)}))

(defn update-recipient
  "Update an existing recipient"
  [conn req]
  (let [recipient (get-in req [:body-params])
        saved-recipient (q/save-recipient conn recipient)]
    {:status 200
     :body saved-recipient}))

(defn delete-recipient
  "Delete a recipient"
  [conn req]
  (let [recipient-id (get-in req [:parameters :path :recipient-id])
        deleted (q/delete-recipient conn recipient-id)]
    {:status 200
     :body deleted}))