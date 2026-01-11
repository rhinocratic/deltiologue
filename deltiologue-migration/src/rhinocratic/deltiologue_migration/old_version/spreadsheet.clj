(ns rhinocratic.deltiologue-migration.old-version.spreadsheet
  "Load the spreadsheet content into a map"
  (:require
   [clojure.string :as string]
   [dk.ative.docjure.spreadsheet :as xl])
  (:import
   [org.apache.poi.openxml4j.util ZipSecureFile]))

(defn column-key
  "Create a keyword from a spreadsheet column name"
  [col-name]
  (-> col-name
      string/lower-case
      (string/replace #"[^a-z0-9]+" "-")
      (string/replace #"-$" "")
      keyword))

(defn header-keys
  "Create keywords from the postcard spreadsheet header row"
  [header-row]
  (->> header-row
       xl/cell-seq
       (map xl/read-cell)
       (take-while #(not (string/blank? %)))
       (map column-key)))

(defn load-row
  "Load a row from a spreadsheet tab"
  [headers row]
  (->> row
       xl/cell-seq
       (map xl/read-cell)
       (#(concat % (repeat nil)))
       (zipmap headers)))

(defn keywordize-tab-name
  "Create a keyword from a spreadsheet tab name"
  [sheet]
  (-> sheet
      xl/sheet-name
      string/lower-case
      (string/replace #"\s+" "-")
      keyword))

(defmulti load-tab-fields
  "Load the tab with the given key from the spreadsheet document"
  keywordize-tab-name)

(defmethod load-tab-fields :postcards
  [tab]
  (let [[header-row content-rows] ((juxt first rest) (xl/row-seq tab))
        headers (header-keys header-row)]
    (->> content-rows
         (map (partial load-row headers))
         (take-while #(some? (:no %)))
         (map #(dissoc % :front :back))
         (into []))))

(defmethod load-tab-fields :default
  [_sheet]
  {})

(defn load-tab
  "Load the top sheet of the spreadsheet into a vector of maps"
  [fname]
  (ZipSecureFile/setMaxFileCount 10000)
  (let [tab (-> (xl/load-workbook-from-resource fname)
                (xl/sheet-seq)
                (nth 0))]
    (load-tab-fields tab)))

(defn parse
  []
  {:card (load-tab "legacy/Postcard_catalogue.xlsx")})
