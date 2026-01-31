(ns rhinocratic.deltiologue.web.api.routes
  (:require
   [reitit.ring.middleware.multipart :as multipart]
   [clojure.spec.alpha :as s]
   [com.brunobonacci.mulog :as u]
   [rhinocratic.deltiologue.web.api.handlers :as h]))

(defn routes
  [conn]
  [["/images" {:swagger {:tags ["files"]}}
    ["/upload" {:post {:summary "upload an image to a temporary location"
                       :parameters {:multipart {:file multipart/temp-file-part}}
                       :responses {200 {:body {:name string?, :size int?}}}
                       :handler #'h/upload-temp-image}}]]
   ["/cards" {:swagger {:tags ["cards"]}}
    ["" {:name ::card-summary
         :get {:handler (partial #'h/card-summary conn)
               :summary "Fetch a summary of all cards"}}]
    ["/:card-no" {:name ::card
                  :parameters {:path {:card-no int?}}
                  :get {:handler (partial #'h/card conn)}
                  :summary "Fetch a card by number"}]]
   ["/notes" {:swagger {:tags ["notes"]}}
    ["" {:name ::note-summaries
         :get {:handler (partial #'h/note-summaries conn)
               :summary "Fetch summaries of all notes"}}]
    ["/:note-id" {:name (keyword (str *ns*) "note")
                  :parameters {:path {:note-id int?}}
                  :get {:handler (partial #'h/note conn)
                        :summary "Fetch a note"}}]]
   ["/references" {:swagger {:tags ["references"]}}
    ["" {:name ::references
         :get {:handler (partial #'h/references conn)
               :summary "Fetch all references"}}]]
   ["/tags" {:swagger {:tags ["tags"]}}
    ["" {:name ::tags
         :get {:handler (partial #'h/tags conn)
               :summary "Fetch all tags"}}]
    ["/categories"
     ["" {:name ::tag-categories
          :get {:handler (partial #'h/tag-categories conn)
                :summary "Fetch all tag categories"}
          :post {:handler (partial #'h/new-tag-category conn)
                 :summary "Create a tag category"
                 :parameters {:body {:display-text string?}}
                 :responses {201 {:body {:tag_category/id int?
                                         :tag_category/display_text string?
                                         :tag_category/category_name string?}}}}}]
     ["/:category-id" {:name ::tag-category
                       :parameters {:path {:category-id int?}}
                       :get {:handler (partial #'h/tag-category conn)
                             :summary "Fetch a single tag category by ID"}
                       :delete {:handler (partial #'h/delete-tag-category conn)
                                :summary "Delete a single tag category by ID"}}]]]
   ["/stamps" {:swagger {:tags ["stamps"]}}
    ["" {:name ::stamps
         :get {:handler (partial #'h/stamps conn)
               :summary "Fetch all stamps"}}]]
   ["/search" {:swagger {:tags ["search"]}}
    ["" {:name ::search
         :parameters {:query {:q string?}}
         :get {:handler (partial #'h/search conn)}
         :summary "Search for cards"}]]])


{:post {:summary "upload an image to a temporary location"
        :parameters {:multipart {:file multipart/temp-file-part}}
        :responses {200 {:body {:name string?, :size int?}}}
        :handler #'h/upload-temp-image}}