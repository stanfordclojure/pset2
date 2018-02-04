(ns pset2.rtc
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as string]
            [instaparse.core :as insta]
            [clojure.spec.alpha :as s]))


(defn- delete-last [s]
  (string/join "" (drop-last s)))
(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))


(defn header [b]
  (->>
    (-> (re-find #"(.*)\{" b)
      last
      string/trim
      (string/split #":"))
    (map string/trim)
    (map #(string/split % #"\s"))))
(defn fields [b]
  (->>
    (-> (re-find #"\{(.*)\}" b)
      last
      string/trim
      (string/split #";"))
    (map string/trim)
    (map #(string/split % #"\s"))))
(defn process-raw-spec [raw-spec]
  (let [normalized (-> raw-spec
                     (string/replace #"[\n\t]" "")
                     (string/replace #"\s+" " ")
                     (delete-last))]
    {:header (header normalized)
     :fields (fields normalized)}))


(comment
  ;; validate www.w3.org
  (def url "https://www.w3.org/TR/webrtc/")
  ;; (def w3c-spec-string (slurp url))
  ;; (println w3c-spec-string)
  (def w3c-html (fetch-url url))
  (println w3c-html)
  (def text-specs (map html/text (html/select w3c-html [:.def.idl :> html/any-node])))
  (process-raw-spec (nth text-specs 4))

  (read-string "[(dictionary RTCErrorEventInit) (EventInit) [ (RTCError? error = null) ]]")



  (def parser (insta/parser (slurp "resources/spec-parser.txt")))


  (def transform-options
    {})

  (defn parse [s]
    (->> (parser s)
      (insta/transform transform-options)))

  (parse (first text-specs))



  (defn choose-operator [op]
    (case op
      "+" +
      "-" -
      "*" *
      "/" /))


  (def transform-options
    {:number read-string
     :vector vector
     :operator choose-operator
     :operation apply
     :expr identity})
  )



