(ns elm-toolkit.refactor
  (:require [elm-toolkit.queries :as q]))

(defn organize-imports [parse-tree]
  (let [imports (q/imports parse-tree)]
    (sort imports)))
