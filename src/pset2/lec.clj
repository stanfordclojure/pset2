(ns pset2.lec
  (:require [clojure.walk :refer [macroexpand-all]]
            [clojure.core.async :refer [chan <! >! <!! go close!]]
            [clojure.pprint :refer [pprint]]))

(defmacro infix
  "Use this macro when you pine for the notation of your childhood"
  [infixed]
  (list (second infixed) (first infixed) (last infixed)))


(let [c (chan)]
  (pprint (macroexpand '(go (>! (chan) "hello"))))
  (println (= "hello" (<!! (go (<! c)))))
  (close! c))

(defmacro spy-whoopsie
  [expression]
  (list let [result expression]
        (list println result)
        result))

(defmacro spy
  [expression]
  (list 'let ['result expression]
        (list 'println 'result)
        'result))

(macroexpand-1 '(spy (+ 38 10)))



(defn spy-fn
  [expression]
  (list 'let ['result expression]
        (list 'println 'result)
        'result))

(spy-fn (+ 38 10))


(println `+)

`(+ 1 ~(inc 1))
`(+ 1 2)
`(+ 1 ~(inc 1))


(defn criticize-code
  [criticism code]
  `(println ~criticism ~(quote code)))
(defmacro code-critic
  [bad good]
  `(do ~(criticize-code "Cursed bacteria of Liberia, this is bad code:" bad)
       ~(criticize-code "Sweet sacred boa of Western and Eastern Samoa, this is good code:" good)))

(code-critic (+ 1 1) (+ 2 3))

(do nil nil)

`(+ ~@(list 1 2 3))

(defmacro with-mischief
  [& stuff-to-do]
  (concat (list 'let ['message "Oh, big deal!"])
          stuff-to-do))





(def message "Good job!")
(def message++ "Good job!")

(with-mischief
  (println "Here's how I feel about that thing you did: " message))


(defmacro with-mischief
  [& stuff-to-do]
  `(let [message "Oh, big deal!"]
     ~@stuff-to-do))

(with-mischief )

(pprint `(let [abc# 1]
  (+ 1 abc#)))

(defmacro report
  [to-try]
  `(if ~to-try
     (println (quote ~to-try) "was successful:" ~to-try)
     (println (quote ~to-try) "was not successful:" ~to-try)))
;; Thread/sleep takes a number of milliseconds to sleep for
(report (do (Thread/sleep 1000) (+ 1 1)))


(let [foo 3]
  (* foo 390))
