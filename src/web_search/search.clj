(ns web-search.search
  (:require [clj-http.client :as http]
            [clojure.pprint :refer :all]
            [clojure.xml :as xml]
            [clojurewerkz.urly.core :as url]
            [clojure.string :as str]))

(def protocol "https://")
(def hostname "blogs.yandex.ru/search.rss?")
(def params "text=%s&numdoc=%d")
(def url (str protocol hostname params))

(defn xml->clj [string]
  #_(zip/xml-zip) 
  (xml/parse (java.io.ByteArrayInputStream. (.getBytes string))))

;; `some` part is bad, and the whole stuff messy 
(defn seek-items [xml-map]
  (let [items (->> xml-map
                   :content
                   (some #(if (= :channel (:tag %)) % false))
                   :content
                   (filter #(= :item (:tag %)))
                   )]
    items))

(defn seek-link [item]
  (->> item
       :content
       (some #(if (= :link (:tag %)) % false))
       :content
       first))

(defn take-2domen [link]
  (-> (url/url-like link)
      url/host-of))

;; hardcoded amount of records per search
(defn perform-get-links [text]
  (let [complete-url (format url (str/replace text #" " "+") 10)
        resp (http/get complete-url)
        items (seek-items (xml->clj (:body resp)))]
    (map seek-link items)))


(defn parallel-domen-statistic [texts plimit]
  (let [bunched-texts (partition plimit texts)]
    (->> (map #(pmap perform-get-links %) bunched-texts)
         flatten
         distinct
         (map take-2domen)
         frequencies
         )))
