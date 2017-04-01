(ns web-search.search
  (:require [clj-http.client :as http]
            [clojure.pprint :refer :all]
            [clojure.xml :as xml]
            [clojurewerkz.urly.core :as url]))

(def protocol "https://")
(def hostname "blogs.yandex.ru/search.rss?")
(def params "text=%s&numdoc=%d")
(def url (str protocol hostname params))

(defn xml->clj [string]
  #_(zip/xml-zip) 
  (xml/parse (java.io.ByteArrayInputStream. (.getBytes string))))

;; `some` part is bad, and the whole stuff messy 
(defn get-items [xml-map]
  (let [items (->> xml-map
                   :content
                   (some #(if (= :channel (:tag %)) % false))
                   :content
                   (filter #(= :item (:tag %)))
                   )]
    items))

(defn get-link [item]
  (->> item
       :content
       (some #(if (= :link (:tag %)) % false))
       :content
       first))

(defn get-2domen [link]
  (-> (url/url-like link)
      url/host-of))

;; hardcoded amount of records per search
(defn perform-get-links [text]
  (let [complete-url (format url text 10)
        resp (http/get complete-url)
        items (get-items (xml->clj (:body resp)))]
    (map get-link items)))


(defn parallel-domen-statistic [texts plimit]
  (let [bunched-texts (partition plimit texts)
        to-perform (comp frequencies (partial map get-2domen) perform-get-links)]
    (->> (map #(pmap to-perform %) bunched-texts)
         flatten
         (merge-with +)
         first)))
