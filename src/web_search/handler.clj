(ns web-search.handler
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [web-search.search :as search]
            [ring.middleware.params :as rparams]

            [ring.middleware.json :as rjson ]
            [ring.util.response :refer [response]])
  )

(defroutes routes
  (GET "/search" {{:strs [query plimit]} :query-params}
       (let [results (doall (search/parallel-domen-statistic query 1))]
         (let [pretty-html (->> (map (fn [[k v]] (format "<li>%s : %s</li>" k v)) results)
                                (reduce str)
                                )]
           (-> (response (format  "<h1>results:<ul>%s</ul></h1>" pretty-html))
               (header "json-result" results)))))
  (route/not-found "<h1>Nothing to look here :P</h1>"))

(def app (-> #'routes
             rparams/wrap-params
             rjson/wrap-json-response
             ))
