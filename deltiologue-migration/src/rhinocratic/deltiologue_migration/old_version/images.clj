(ns rhinocratic.deltiologue-migration.old-version.images
  "Functions for reading from the legacy images folder."
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [clojure.java.shell :as shell]))

(def image-source-folder "resources/legacy/postcards/")
(def image-dest-folder "../deltiologue/data/images/postcards/")

(defn source-image-files
  []
  (.list (io/as-file image-source-folder)))

(defn create-urls
  [file]
  (let [rgx #"(\d+)[^(]+\(([^\)]+)\)"
        fname (.getName file)
        [_ idx face] (re-find rgx fname)]
    {:front (str "/images/postcards/" idx "/front.jpg")
     :rear (str "/images/postcards/" idx "/rear.jpg")
     :thumb (str "/images/postcards/" idx "/thumb.jpg")}))

(defn image-index
  [fname]
  (->> fname
       (re-find #"^\d+")
       parse-long))

(defn image-rows
  [idx fname]
  [{:title fname :alt-text "" :url (str "/images/postcards/" idx "/front.jpg")}
   {:title fname :alt-text "" :url (str "/images/postcards/" idx "/rear.jpg")}
   {:title fname :alt-text "" :url (str "/images/postcards/" idx "/thumb.jpg")}])

(defn image-table
  []
  (->> (source-image-files)
       (sort-by image-index)
       (take-nth 2)
       (map #(string/replace % #"\.jp(e?)g" ""))
       (map #(string/replace % #"\s+\([^\)]+\)$" ""))
       (map #(string/replace % #"^\d+\s+" ""))
       (map-indexed image-rows)
       (apply concat)))

(defn front?
  [fname]
  (string/includes? fname "front"))

(defn rear?
  [fname]
  (string/includes? fname "back"))

(defn copy-image
  [src-fname]
  (let [idx (re-find #"^\d+" src-fname)
        src-file (io/as-file (str image-source-folder src-fname))
        dest-file (cond
                    (front? src-fname) (io/as-file (str image-dest-folder "/" idx "/front.jpg"))
                    (rear? src-fname) (io/as-file (str image-dest-folder "/" idx "/rear.jpg")))]
    (io/make-parents dest-file)
    (io/copy src-file dest-file)
    (.getPath dest-file)))

(defn copy-images
  []
  (->> (source-image-files)
       (map copy-image)))

(defn make-thumbnail
  [fname]
  (let [idx (re-find #"^\d+" fname)
        source-path (->> image-source-folder
                         io/as-file
                         (.getCanonicalPath))
        dest-path (->> image-dest-folder
                       io/as-file
                       (.getCanonicalPath))
        source-vol (str source-path ":/sources")
        dest-vol (str dest-path ":/dests")
        source-file (str "/sources/" fname)
        dest-file (str "/dests/" idx "/thumb.jpg")
        result (shell/sh
                "docker"
                "run"
                "--entrypoint=magick"
                "-v"
                source-vol
                "-v"
                dest-vol
                "dpokidov/imagemagick"
                source-file
                "-resize"
                "100x100"
                dest-file)]
    (assoc result :idx idx)))

(defn make-thumbnails
  []
  (let [fronts (->> (source-image-files)
                    (filter front?))]
    (doall (map make-thumbnail fronts))))

(comment

  (tap> (make-thumbnails))


  #_())