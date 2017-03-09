(ns elm-toolkit.render
  (:require #?(:clj [clojure.core.match :refer [match]]
               :cljs [cljs.core.match :refer-macros [match]])
            [clojure.string :as string]
            [instaparse.core :as insta]))

(def name-transform {:Name      (fn [name] name)
                     :name      (fn [name] name)
                     :fn_name   (fn [name] name)
                     :module_name (fn [name] name)
                     :type_name (fn [name] name)})

(def namespace-transform (merge name-transform {:namespace (fn [& names] (str (string/join "." names) "."))}))

(def expose-transform (merge name-transform
                             {:expose_all (fn [_] "..")
                              :expose_list (fn [& expose] (string/join ", " expose))
                              :exposing (fn [expose_list] (str " exposing (" expose_list ")"))}))

(def module-transform (merge expose-transform
                             name-transform
                             namespace-transform
                             {:module_def (fn [& module] (str "module " (string/join module)))}))

(def import-transform (merge name-transform
                             namespace-transform
                             expose-transform
                             {:import (fn [& import] (str "import " (string/join import)))}))

(def namespace-transform (merge name-transform {:namespace (fn [& names] (str (string/join "." names) ".") ) }))


(defn render-import [import-vector]
  (insta/transform import-transform import-vector))

(defn render-module [module-vector]
  (insta/transform module-transform module-vector))

(defn render-namespace [namespace]
  (insta/transform namespace-transform namespace))
