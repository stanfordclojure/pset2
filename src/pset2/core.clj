(ns pset2.core
  (:require [clojure.pprint :refer [pprint]])
  (:gen-class))


(comment
  ;; ==== p1: unless ==== ;;
  ;; Write a macro 'unless', such that
  (unless true :a :b) => :b
  (unless false :a :b) => :a
  )



(defmacro unless [& args])


(comment
  ;; ==== p2: LINQ inspired API ==== ;;
  ;; Write a macro 'from', such that

  (def names ["Burke", "Connor", "Frank", "Everett", "Albert", "George", "Harris", "David"])

  (from n in names
        (where (= (count n) 5))
        (orderby n)
        (select (.toUpperCase n))) => '("BURKE" "DAVID" "FRANK")
  )

(->> names
  (filter (fn [n] (= (count n) 5)))
  (sort-by (fn [n] n))
  (map (fn [n] (.toUpperCase n))))


(defmulti clause-transformer (fn [_ clause] (first clause)))

(defmethod clause-transformer 'where [sym clause]
  `(filter (fn [~sym] ~(last clause))))
(defmethod clause-transformer 'orderby [sym clause]
  `(sort-by (fn [~sym] ~(last clause))))
(defmethod clause-transformer 'select [sym clause]
  `(map (fn [~sym] ~(last clause))))

(defmacro from [sym _ coll & clauses]
  {:pre [(symbol? sym)]}
  (let [output `(->> ~coll)
        query-clauses (map (partial clause-transformer sym) clauses)]
    (concat output query-clauses)))

(pprint (macroexpand-1
  '(from n in names
        (where (= (count n) 5))
        (orderby n)
        (select (.toUpperCase n)))))





(comment
  "TRANFORM INTO"
  (->> names
    (filter (fn [n] (= (count n) 5)))
    (sort-by (fn [n] n))
    (map (fn [n] (.toUpperCase n))))
  )


(defmulti clause-transformer (fn [_ x] (first x)))

(defmethod clause-transformer 'where [sym clause]
  `(filter (fn [~sym] ~@(rest clause))))

(defmethod clause-transformer 'orderby [sym clause]
  `(sort-by (fn [~sym] ~@(rest clause))))

(defmethod clause-transformer 'select [sym clause]
  `(map (fn [~sym] ~@(rest clause))))

(defmethod clause-transformer :default [clause] clause)

(defmacro from [s _ coll & clauses]
  (let [output `(->> ~coll)
        transformed-clauses (map (partial clause-transformer s) clauses)]
    (concat output transformed-clauses)))


(macroexpand-1 '(from n in names
      (where (= (count n) 5))
      (orderby n)
      (select (.toUpperCase n))))


(from n in names
      (where (= (count n) 5))
      (orderby n)
      (select (.toUpperCase n)))




(defmacro from [& args])


;; ==== p3: Write a macro 'with-file': ==== ;;
;; Allows us to read the contents of a file and have the file
;; be automatically closed at the end.
(defmacro with-file [& args])


;; ==== p4: macro debugging ==== ;;
;; The macro below to square the input has a bug. Can you spot it?
(defmacro square [n]
  `(* ~n ~n))


(comment
  ;; HINT
  (let [seed (atom 9)
        next (fn [] (swap! seed inc))]
    (square (next)))
  )



;; ==== p5: moar debugging ==== ;;
;; Given the following macro:
(defmacro nif [val pos zero neg]
  "Numeric if. Executes pos, zero or neg depending
  if val is positive, zero or negative respectively"
  `(let [~'res ~val]
     (cond (pos? ~'res) ~pos
           (zero? ~'res) ~zero
           :else ~neg)))

(comment

  ;;The fact below HOLDS true as it is:
  (nif -1
       "positive"
       "zero"
       "negative") => "negative"

  ;; However this next fact doesn't. Can you spot the bug?
  ;; Now fix it.
  (let [res (java.util.Scanner. (java.io.FileInputStream. "project.clj"))]
    (do (nif 0
             "positive"
             (prn (.nextLine res))
             (prn "negative"))
        (.close res))) => nil
  )
