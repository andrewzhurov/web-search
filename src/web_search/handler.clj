(ns web-search.handler
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [web-search.search :as search]
            [ring.middleware.params :as rparams]
            ))

(defroutes routes
  (GET "/search" {{:strs [query plimit]} :query-params}
       (let [results (doall (search/parallel-domen-statistic query (Integer. plimit)))
             pretty-html (->> (map (fn [[k v]] (format "<li>%s : %s</li>" k v)) results)
                              (reduce str)
                              )]
         (format  "<h1>results:<ul>%s</ul></h1>" pretty-html)))
  (route/not-found "<h1>Nothing to look here :P</h1>"))

(def app (-> #'routes
             rparams/wrap-params))
