(ns elm-toolkit.queries
  (:require [com.rpl.specter :as specter]
            #?(:clj [clojure.core.match :refer [match]]
               :cljs [cljs.core.match :refer-macros [match]])
            [instaparse.core :as insta]
            [elm-toolkit.render :as render]))

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

(defn definitions [parse-tree]
  (find (partial keyword? :definition) parse-tree))

(defn namespace [parse-tree]
  (find (partial keyword? :namespace) parse-tree))

(defn definition-name [definition]
  (match [definition]
         [[:definition [:function [:name name] & rest] ] ] name
         [[:definition [:annotation [:name name] & rest] ] ] name
         [[:definition [:type [:Name name] & rest] ] ] name
         :else nil
         ))

(defn references? [name definition]
  (= name (definition-name definition)))

;;
;; Transforms
;;

(defn swap [keyword new-thing parse-tree]
  (insta/transform {keyword (fn [& _]  (into [keyword] new-thing) )} parse-tree))

(defn swap-imports [new-imports parse-tree]
  (swap :imports new-imports parse-tree))

(defn swap-module-def [new-module-def parse-tree]
  (insta/transform {:module_def (fn [& _] (first new-module-def))} parse-tree))
