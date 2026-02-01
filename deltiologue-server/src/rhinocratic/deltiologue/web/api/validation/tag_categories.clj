(ns rhinocratic.deltiologue.web.api.validation.tag-categories
  (:require
   [clojure.spec.alpha :as s]
   [clojure.string :as str]))

(defn colour?
  [s]
  (re-matches #"^[a-f0-9]{6}$" s))

(s/def ::display-text string?)
(s/def ::category-name string?)
(s/def ::colour (s/and string? colour?))
(s/def ::tag-category
  (s/keys :req [::display-text
                ::category-name
                ::colour]))

