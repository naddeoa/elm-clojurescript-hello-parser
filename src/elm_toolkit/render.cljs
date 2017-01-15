(ns elm-toolkit.render
  (:require [cljs.core.match :refer-macros [match]]
            [clojure.string :as string]
            [instaparse.core :as insta]
            [elm-toolkit.queries :as q]))


(def import-transform {:Name        (fn [name] name)
                       :name        (fn [name] name)
                       :fn_name     (fn [name] name)
                       :type_name   (fn [name] name)
                       :namespace   (fn [& names] (str (string/join "." names) "."))
                       :module_name (fn [name] name)
                       :expose_all  (fn [_] "..")
                       :expose_list (fn [& expose] (string/join ", " expose))
                       :exposing    (fn [expose_list] (str " exposing (" expose_list ")"))
                       :import      (fn [& import] (str "import " (string/join import)))})

(defn render-import [import-vector]
  (insta/transform import-transform import-vector))

