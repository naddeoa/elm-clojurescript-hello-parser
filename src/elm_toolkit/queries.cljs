(ns elm-toolkit.queries
  (:require [com.rpl.specter :as specter]
            [instaparse.core :as insta]
            ))


;;
;; Selects
;;

(defn keyword? [keyw parse-tree]
  (and (vector? parse-tree) (= (first parse-tree) keyw)))

(defn find [predicate parse-tree]
  (specter/select (specter/walker predicate) parse-tree))

(defn imports [parse-tree]
  (find (partial keyword? :import) parse-tree))

(defn module-def [parse-tree]
  (find (partial keyword? :module_def) parse-tree))

(defn functions [parse-tree]
  (find (partial keyword? :function) parse-tree))

;;
;; Transforms
;;

(defn swap [keyword new-thing parse-tree]
  (insta/transform {keyword (fn [& _]  (into [keyword] new-thing) )} parse-tree))

(defn swap-imports [new-imports parse-tree]
  (swap :imports new-imports parse-tree))

(defn swap-module-def [new-module-def parse-tree]
  (insta/transform {:module_def (fn [& _] (first new-module-def))} parse-tree))
