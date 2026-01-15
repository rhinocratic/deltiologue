(ns rhinocratic.deltiologue-migration.old-version.manual
  "Manually inserted data."
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]))

(defn tables
  []
  {:note-image
   [{:filename "Billy McClain in 1902.jpg"
     :caption "Billy McClain in 1902"
     :alt-text "Billy McClain in 1902"}
    {:filename "Long Tak Sam and the Tan Kwai Troupe in 1936.jpg"
     :caption "Long Tak Sam and the Tan Kwai Troupe in 1936"
     :alt-text "Long Tak Sam and the Tan Kwai Troupe in 1936"}
    {:filename "Marie Hall in 1907.jpg"
     :caption "Marie Hall in 1907"
     :alt-text "Marie Hall in 1907"}
    {:filename "Morecambe Carnival Flyer.jpg"
     :caption "Morecambe Carnival Flyer"
     :alt-text "Morecambe Carnival Flyer"}
    {:filename "Otora San of the Lukushima Troupe during their British tour in 1904.jpg"
     :caption "Otora San of the Lukushima Troupe during their British tour in 1904"
     :alt-text "Otora San of the Lukushima Troupe during their British tour in 1904"}
    {:filename "The Riogoku Troupe of Japanese Artistes who performed in Morecambe in 1908.jpg"
     :caption "The Riogoku Troupe of Japanese Artistes who performed in Morecambe in 1908"
     :alt-text "The Riogoku Troupe of Japanese Artistes who performed in Morecambe in 1908"}
    {:filename "Watching the troupe at Morecambe.jpg"
     :caption "Watching the troupe at Morecambe"
     :alt-text "Watching the troupe at Morecambe"}]})
