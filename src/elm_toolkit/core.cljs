(ns elm-toolkit.core
  (:require [clojure.browser.repl :as repl]
            [instaparse.core :as insta]
            [cljs.pprint :as pprint]
            [elm-toolkit.parser :as parser]
            [elm-toolkit.model :as model]
            [clojure.core.async :as async]
            [cljs.nodejs :as nodejs]))

(nodejs/enable-util-print!)

(defn -lambda-handler [event context callback]
  (println "working"))

(set! (.-handler js/exports) -lambda-handler)

(defn -main [& args]
  (println "just set up the handler"))

(set! *main-cli-fn* -main)
