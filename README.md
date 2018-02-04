# CS43: PSET 2

Practice with Macros!

## Instructions

1. Clone this repo
1. submit your `src/pset2` folder as a tarball or zip file to
   `jiangts@stanford.edu` by February 13, 2018

## Problems

1. Write the macro `unless`

the syntax for `if` is
`(if <condition> <true-expr> <false-exp>)`
where the true-expr is evaluated only if condition is truthy,
and the false-expr is evaluated only if condition is falsey
the syntax for `unless` is
`(unless <condition> <false-expr> <true-expr>)`

2. Write the macro `from` to implement a [LINQ](linq) inspired syntax.


```clojure
(def names ["Burke", "Connor", "Frank", "Everett", "Albert", "George", "Harris", "David"])

(from n in names
      (where (= (count n) 5))
      (orderby n)
      (select (.toUpperCase n))) => '("BURKE" "DAVID" "FRANK")
```

As you see, you can bind the symbol `n` to a value from runtime (names), and you
can pass it an arbitrary set of query clauses that transform the inputs.
Some transformations, like `select`, can specify function-esque arguments to
further transform the collection.
You only need to implement `where`, `orderby` and `select` for this assignment.

3. Write the macro `with-file`, with usage:

```clojure
(with-file "project.clj"
  (if (.hasNextLine file)
    (.nextLine file)))
```

where the file object is bound to the `file` symbol within the macro body, and
is automatically closed when control flow exits the expression.

4. What's the bug in the `square` macro, intended to pre-compute the square of
   the input?

```clojure
(defmacro square [n]
  `(* ~n ~n))
```

5. Find the bug and fix the following numeric-if "`nif`" macro:

```clojure
(defmacro nif [val pos zero neg]
  "Numeric if. Executes pos, zero or neg depending
  if val is positive, zero or negative respectively"
  `(let [~'res ~val]
     (cond (pos? ~'res) ~pos
           (zero? ~'res) ~zero
           :else ~neg)))
```

## Bonus Project

We introduced `clojure.spec` in class.
We talked about how it dovetails nicely with macros to help validate macro
inputs and give robust error messages.

We can also use specs to create alternative interfaces to an API via `conform`
and `unform`! Wouldn't it be nice if we created a configuration-driven API to
WebRTC?

Let's do that, by creating specs for WebRTC!
Sounds cumbersome?


... Macros to the rescue!

Macros transform input data, which may or may not be code, into output data,
which is interpreted as code. It turns out that the input data need not be
Clojure forms, it can just be data from the w3 specs website!

In `src/pset2/rtc.clj` you will find some starter code that scrapes all of the
Interface Definition Language (IDL) specification data out of the official
WebRTC spec and parses into simple data structures you can use to output specs
for WebRTC. These specs will help us create a more data-driven and
Clojure-friendly interface to WebRTC.


#### Example IDL and corresponding spec:
See the comment on the bottom of `src/pset2/rtc.clj`

[linq]: https://en.wikipedia.org/wiki/Language_Integrated_Query



## References
https://github.com/leonardoborges/clojure-macros-workshop
