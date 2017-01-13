(ns elm-toolkit.core
  (:require [clojure.browser.repl :as repl]
            [instaparse.core :as insta]
            [cljs.pprint :as pprint]
            [clojure.zip :as zip]
            [cljs.core.match :refer-macros [match]]
            [elm-toolkit.suggestions :as suggestions]
            [elm-toolkit.parser :as parser]
            [elm-toolkit.model :as model]
            [clojure.string :as string]
            [cognitect.transit :as transit]
            [cljs.nodejs :as nodejs]))




(nodejs/enable-util-print!)

(defn compact [tree]
  (insta/transform {
                    :Name (fn [n] :name)
                    :module_name (fn [[:name a]  ] [:module_name a])
                    :fn_name (fn [[:name a]  ]
                               (match a
                                      ".." [:all]
                                      :else [:fn_name a]))
                    :type_name (fn [[:name a]  ] [:type_name a])
                    :exposing (fn [[:expose_list & r]  ] (into []  (concat [:exposing ] r))   )
                    :namespace (fn  [& r] [:namespace (string/join "." (map last r)) ]    )
                    } tree))

(defn index-tree [tree]
  (index-tree-recur (zip/vector-zip tree) {} ))

(defn- index-tree-recur [zip-tree accumulator]
  (if (zip/end? zip-tree)
    accumulator
    (recur (zip/next zip-tree) (index (zip/node zip-tree) accumulator)  )))

(defn- index [node, accumulator]
  (match [node]
         [( [:module_def [:module_name [:Name a ]] & r] :seq)] (update-in accumulator [:modules a] (fn [val] node))
         :else  accumulator))

(defn- debug [thing]
  (let [a (println thing)]
    thing))

(defn -main [& args]
  (let [path (first args)
        parse-tree (parser/parse-file path)]
    (pprint/pprint parse-tree)))

(set! *main-cli-fn* -main)
