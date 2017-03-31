(defproject web-search "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]

                 [clj-http "3.4.1"]
                 [http-kit "2.2.0"]

                 [ring "1.6.0-RC1"]
                 [compojure "1.5.2"]
                 [clojurewerkz/urly "1.0.0"]]
  :main web-search.server)
