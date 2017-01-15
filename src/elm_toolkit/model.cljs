(ns elm-toolkit.model
  (:require [instaparse.core :as insta]
            [clojure.zip :as zip :refer [down right node vector-zip]]
            [cljs.core.match :refer-macros [match]]
            [com.rpl.specter :as specter]
            [cljs.nodejs :as nodejs]))

(defn get-imports [parse-tree]
  (let [imports (-> parse-tree vector-zip down right right node)]
    imports))

(defn get-definitions [parse-tree]
  (let [definitions (-> parse-tree vector-zip down right right right node)]
    definitions))

(defn get-module-def [parse-tree]
  (let [module-def (-> parse-tree vector-zip down right node)]
    module-def))

(defn get-module-name [parse-tree]
  (let [[:module_def [:module_name [:Name n]]] (get-module-def parse-tree)] n))

(defn get-functions [parse-tree]
  (let [definitions (get-definitions parse-tree)]
    (filter (fn [definition]
              (match [definition]
                [([:definition [:function & r]] :seq)] true
                :else false))
            definitions)))

(defn get-function-annotations [parse-tree]
  (let [annotations (get-definitions parse-tree)]
    (filter (fn [annotation]
              (match [annotation]
                [([:definition [:function_annotation & r]] :seq)] true
                :else false))
            annotations)))

(println "fish")

(defn get-types [parse-tree]
  (let [types (get-definitions parse-tree)]
    (filter (fn [t]
              (match [t]
                [([:definition [:type & r]] :seq)] true
                :else false))
            types)))

(defn functions [parse-tree]
  (let [fns (get-functions parse-tree)]
    (map (fn [[:definition [:function [:name n] [:function_parameters & r] [:expression & e]]]] (Function. n (if (nil? e) nil r))) fns)))
