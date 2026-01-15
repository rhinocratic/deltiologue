(ns rhinocratic.deltiologue-migration.old-version.notes
  "Functions for reading from the legacy notes page."
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string])
  (:import
   [org.apache.poi.xwpf.usermodel XWPFDocument XWPFParagraph]
   [org.apache.poi.xwpf.extractor XWPFWordExtractor]
   [java.time LocalDate]
   [java.time.format DateTimeFormatter]))

(defn load-xml
  [fname]
  (let [doc (->> fname
                 io/resource
                 io/input-stream
                 (XWPFDocument.))]
    doc))

(defn title?
  [^XWPFParagraph para]
  (= (.getStyle para) "Heading2"))

(defn reference?
  [^XWPFParagraph para]
  (some? (re-matches #"^\d+\..*" (.getText para))))

(defn split-by-title
  [^XWPFParagraph paras]
  (->> paras
       (partition-by title?)
       (partition 2)
       (map #(apply concat %))))

(defn blank?
  [^XWPFParagraph para]
  (-> para
      (.getText)
      (string/blank?)))

(defn ref-index
  [ref-txt]
  (->> ref-txt
       (re-find #"^(\d+)\.")
       last
       parse-long))

(defn ref-medium
  [ref-txt]
  (->> ref-txt
       (re-seq #"\[(.*?)\]")
       (map last)
       (filter #(not (string/starts-with? % "Accessed")))
       first))

(defn parse-long-date
  [date-str]
  (when (and (some? date-str)
             (not (string/blank? date-str)))
    (let [fmt (DateTimeFormatter/ofPattern "d MMMM uuuu")]
      (try
        (LocalDate/parse date-str fmt)
        (catch Exception ex
          (println "Trouble parsing" date-str))))))

(defn ref-accessed
  [ref-txt]
  (let [accessed (->> ref-txt
                      (re-seq #"\[(.*?)\]")
                      (map last)
                      (filter #(string/starts-with? % "Accessed"))
                      first)]
    (when accessed
      (->> accessed
           (re-find #"Accessed\s+(?:on\s+)?(\d+\s+\S+\s+\d+)")
           last
           parse-long-date))))

(defn ref-title
  [ref-runs]
  (let [title-run (->> ref-runs
                       (filter #(.isItalic %))
                       first)]
    (when title-run
      (.text title-run))))

(defn ref-source
  [ref-runs title]
  (let [butfirst-runs (->> ref-runs
                           rest)
        source-runs (if title
                      (take-while #(not (.isItalic %)) butfirst-runs)
                      (take-while #(not (string/starts-with? (.text %) "[")) butfirst-runs))]
    (->> source-runs
         (map #(.text %))
         (apply str)
         (string/trim))))

(defn issue-text
  [ref-txt]
  (->> ref-txt
       (re-seq #"\](.*?)\[")
       (map last)
       (map #(string/replace % "." ""))
       (map string/trim)
       first))

(defn ref-issue-date
  [ref-txt]
  (when-let [issue-str (issue-text ref-txt)]
    (let [date-str (re-find #"\d+\s+[^\d]+\s+\d+" issue-str)]
      (when (some? date-str)
        (parse-long-date date-str)))))


(defn ref-issue-note
  [ref-txt]
  (when-let [issue-str (issue-text ref-txt)]
    (let [without-date (string/replace issue-str #"\d+\s+[^\d]+\s+\d+" "")]
      (when (and (some? without-date)
                 (not (string/blank? without-date)))
        without-date))))

(defn ref-available
  [ref-txt]
  (let [available-str (->> ref-txt
                           (re-seq #"(?:Available from:\s*)(.*)")
                           (map last)
                           first)]
    (when (and
           available-str
           (not (string/blank? available-str)))
      (string/trim available-str))))

(defn make-reference
  [^XWPFParagraph para]
  (let [txt (.getText para)
        runs (.getRuns para)
        title (ref-title runs)
        source (ref-source runs title)]
    {:idx (ref-index txt)
     :medium (ref-medium txt)
     :accessed (ref-accessed txt)
     :source (ref-source runs title)
     :title (ref-title runs)
     :issue-date (ref-issue-date txt)
     :issue-note (ref-issue-note txt)
     :available (ref-available txt)}))

(defn superscript?
  [run]
  (= "superscript" (str (.getVerticalAlignment run))))

(defn superscript-runs?
  [runs]
  (= "superscript" (str (.getVerticalAlignment (first runs)))))

(defn markdown-run-dispatch
  [runs]
  (cond
    (superscript-runs? runs) ::superscript))

(defmulti markdown-run #'markdown-run-dispatch)

(defmethod markdown-run ::superscript
  [runs]
  (let [txt (->> runs
                 (map #(.text %))
                 (apply str)
                 (string/trim))]
    (if (string/starts-with? txt "[")
      (let [content (re-find #"\d+" txt)]
        (str "<sup>[<a href=\"/api/references/" content "\">" content "]</a></sup>"))
      (str "<sup>" txt "</sup>"))))

(defmethod markdown-run :default
  [runs]
  (->> runs
       (map #(.text %))
       (apply str)))

(defn coalesce-superscripts
  [runs]
  (->> runs
       (partition-by superscript?)))

(defn markdown-para
  [^XWPFParagraph para]
  (let [runs (.getRuns para)]
    (->> runs
         (coalesce-superscripts)
         (map markdown-run)
         (apply str))))

(defn make-note
  [paras]
  (let [[heading & others] paras
        title (.getText ^XWPFParagraph heading)
        body (->> others
                  (map markdown-para)
                  (string/join "\n\n"))]
    {:title title
     :body body}))

(defn parse-body
  [^XWPFDocument doc]
  (let [paras (->> doc
                   (.getBodyElements)
                   (remove blank?)
                   rest)
        references (->> paras
                        (filter reference?)
                        (map make-reference)
                        (group-by :idx)
                        (map #(first (val %)))
                        (sort-by :idx))
        notes (->> paras
                   (remove reference?)
                   split-by-title
                   (map make-note))
        ref-indexes (->> references
                         (map-indexed #(vector (:idx %2) %1))
                         (into {}))
        resolve-index (fn [para]
                        (let [para-idx (ref-index (.getText para))]
                          (ref-indexes para-idx)))
        note-references (->> paras
                             split-by-title
                             (map #(filter reference? %))
                             (map #(map resolve-index %))
                             (map-indexed (fn [pos indexes]
                                            (map (partial vector pos) indexes)))
                             (apply concat)
                             (map #(zipmap [:note-id :reference-id] %))
                             distinct)]
    {:note notes
     :reference references
     :note-reference note-references}))

(defn load-doc
  [fname]
  (load-xml fname))

(defn parse
  []
  (let [doc (load-doc "legacy/Text_with_references_Revised_220724.docx")]
    (parse-body doc)))
