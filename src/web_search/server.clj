(ns web-search.server
  (:require [web-search.handler :refer [app]]
            [org.httpkit.server :as kit]
            ))

(defn -main []
  (kit/run-server app {:port 8080}))
