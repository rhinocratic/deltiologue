(ns rhinocratic.deltiologue-migration.old-version.images
  "Functions for reading from the legacy images folder."
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [clojure.java.shell :as shell]))

(def postcard-image-source-folder "/home/vlad/gdrive/vlad/postcard-data/Postcards/")
(def postcard-image-dest-folder "../deltiologue-server/data/images/postcards/")
(def postcard-images "../deltiologue-server/data/images/postcards")
(def note-images "../deltiologue-server/data/images/notes")
(def volname "deltiologue_deltiologue_app_data")

(defn source-image-files
  []
  (->> (io/as-file postcard-image-source-folder)
       (.listFiles)
       (filter #(.isFile %))
       (map #(.getName %))))

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

(defn front?
  [fname]
  (string/includes? fname "front"))

(defn rear?
  [fname]
  (string/includes? fname "back"))

(defn copy-image
  [src-fname]
  (let [idx (->> src-fname
                 (re-find #"^\d+")
                 parse-long
                 str)
        src-file (io/as-file (str postcard-image-source-folder src-fname))
        dest-file (cond
                    (front? src-fname) (io/as-file (str postcard-image-dest-folder "/" idx "/front.jpg"))
                    (rear? src-fname) (io/as-file (str postcard-image-dest-folder "/" idx "/rear.jpg")))]
    (io/make-parents dest-file)
    (io/copy src-file dest-file)
    (.getPath dest-file)))

(defn copy-images
  []
  (->> (source-image-files)
       (map copy-image)))

(defn make-thumbnail
  [fname]
  (let [idx (->> fname
                 (re-find #"^\d+")
                 parse-long
                 str)
        source-path (->> postcard-image-source-folder
                         io/as-file
                         (.getCanonicalPath))
        dest-path (->> postcard-image-dest-folder
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
                "--rm"
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

(defn dest-indexes
  []
  (->> (io/as-file postcard-image-dest-folder)
       (.listFiles)
       (filter #(.isDirectory %))
       (map #(.getName %))
       (map parse-long)
       sort
       set))

(defn new-images
  []
  (let [dest-index-exists? (dest-indexes)
        new? (fn [file] (-> file
                            image-index
                            dest-index-exists?
                            not))]
    (->> (source-image-files)
         (filter new?))))

(defn delete-volume
  []
  (println "Deleting volume")
  (shell/sh
   "docker"
   "volume"
   "rm"
   volname
   "-f"))

(defn create-volume
  []
  (println "Creating volume")
  (shell/sh
   "docker"
   "run"
   "-v"
   (str volname ":/images")
   "--name"
   "helper"
   "busybox"
   "true"))

(defn populate-volume
  []
  (println "Populating volume")
  (shell/sh
   "docker"
   "cp"
   postcard-images
   "helper:/images")
  (shell/sh
   "docker"
   "cp"
   note-images
   "helper:/images"))

(defn delete-helper
  []
  (println "Deleting helper container")
  (shell/sh
   "docker"
   "rm"
   "helper"))

(defn update-images
  []
  (let [new (new-images)
        fronts (->> new (filter front?))]
    (println "Found" (count fronts) "new images.")
    (doall (map copy-image new))
    (doall (map make-thumbnail fronts))))

;; NB - stop application containers before running this
(defn recreate-volume
  []
  (delete-volume)
  (create-volume)
  (populate-volume)
  (delete-helper))