(ns elm-toolkit.core
  (:gen-class
   :methods [^:static [handler [String] String]])
  (:require [instaparse.core :as insta]
            [elm-toolkit.parser :as parser]
            [clojure.data.json :as json]
            [clojure.core.async :as async]))

(def tmp (parser/parser "functionDef : Int -> Int"))

(defn getCode [jsonInput]
  (let [cljMap (json/read-str jsonInput)]
    (get cljMap "code")))

(defn -main [& args]
  (println tmp))

(defn -handler [input]
  (let [parse (parser/parser input)]
    (str parse)))

