(ns elm-toolkit.queries
  (:require [com.rpl.specter :as specter]))


(defn keyword? [keyw parse-tree]
  (and (vector? parse-tree) (= (first parse-tree) keyw)))

(defn find [predicate parse-tree]
  (specter/select (specter/walker predicate) parse-tree))

(defn imports [parse-tree]
  (find (partial keyword? :import) parse-tree))
