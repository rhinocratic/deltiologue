(ns rhinocratic.deltiologue-migration.old-version.content
  (:require
   [clojure.java.io :as io]))

(defn home-content
  []
  (-> "legacy/home.md"
      io/resource
      slurp))

(defn links-content
  []
  (-> "legacy/links.md"
      io/resource
      slurp))

(defn technical-content
  []
  (-> "legacy/technical.md"
      io/resource
      slurp))

(defn parse
  []
  [{:title "About"
    :content (home-content)
    :draft false}
   {:title "Links"
    :content (links-content)
    :draft false}
   {:title "Technical"
    :content (technical-content)
    :draft false}])