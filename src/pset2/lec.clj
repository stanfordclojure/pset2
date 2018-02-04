(ns pset2.lec)

(defmacro backwards
  [form]
  (reverse form))

(macroexpand '(backwards (" backwards" " am" "I" str)))
; => "I am backwards"

(eval (list + 1 2))



(def addition-list (list + 1 2))
(eval addition-list)
; => 3
(eval (concat addition-list [10]))
; => 13
(eval (list 'def 'lucky-number
            (concat addition-list [10])))
(print lucky-number)

(print (eval (read-string "(+ 1 2)")))

(read-string "#(+ 1 2)")
(read-string "@my-atom")
(read-string "'(1 2 3)")

(if true :a (fire-missiles!))
(fn? (read-string "+"))
(fn? (eval (read-string "+")))

(def string-world "(+ 1 2)")
;; string->data read-string
;; data->string str

(def data-world (list '+ 1 2))
(println data-world)

;; data->code eval
;; code->data quote '() (list)

(def code-world (+ 1 2))

(defmacro ignore-last-operand
  [function-call]
  (butlast function-call))
(macroexpand '(ignore-last-operand (+ 1 2 10)))


(defn ignore-last-op
  [function-call]
  (print "function-call is this:" function-call)
  (butlast function-call))
(ignore-last-operand (+ 1 2 (println "look at me!!!")))
(ignore-last-op (+ 1 2 (do (println "look at me!!!")
                           3)))

(defn read-resource
  [path]
  (macroexpand '(-> path
      clojure.java.io/resource
      slurp
      read-string)))


(comment
  (+ 1 2)
  (println "HELLO") 
  )

