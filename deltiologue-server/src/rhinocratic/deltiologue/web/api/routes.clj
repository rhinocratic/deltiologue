(ns rhinocratic.deltiologue.web.api.routes
  (:require
   [com.brunobonacci.mulog :as u]
   [rhinocratic.deltiologue.web.api.routes.images :as images]
   [rhinocratic.deltiologue.web.api.routes.cards :as cards]
   [rhinocratic.deltiologue.web.api.routes.notes :as notes]
   [rhinocratic.deltiologue.web.api.routes.references :as references]
   [rhinocratic.deltiologue.web.api.routes.tags :as tags]
   [rhinocratic.deltiologue.web.api.routes.tag-categories :as tag-categories]
   [rhinocratic.deltiologue.web.api.routes.stamps :as stamps]
   [rhinocratic.deltiologue.web.api.routes.publishers :as publishers]
   [rhinocratic.deltiologue.web.api.routes.recipients :as recipients]
   [rhinocratic.deltiologue.web.api.routes.series :as series]
   [rhinocratic.deltiologue.web.api.routes.content :as content]
   [rhinocratic.deltiologue.web.api.routes.search :as search]))

(defn routes
  [conn]
  [(images/routes conn)
   (cards/routes conn)
   (notes/routes conn)
   (references/routes conn)
   (tags/routes conn)
   (tag-categories/routes conn)
   (stamps/routes conn)
   (publishers/routes conn)
   (recipients/routes conn)
   (series/routes conn)
   (content/routes conn)
   (search/routes conn)])
