(ns elm-toolkit.queries-test
  (:require [elm-toolkit.queries :as q]
            [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]
            [clojure.test :as t]))

(def parse-tree (parser/parse-file "./input/Simple/Module.elm"))


(deftest test-imports-query
  (let [imports (q/imports parse-tree)
        expected [[:import [:module_name [:Name "List"]]]
                  [:import [:module_name [:Name "String"]]]
                  [:import [:module_name [:Name "Array"]]]]]
    (is (= imports expected))))

(deftest test-imports-swap
  (let [imports (q/imports parse-tree)
        new-tree (q/swap-imports imports parse-tree)]
    (is (= new-tree parse-tree))))


(deftest test-module-def-query
  (let [module-def (q/module-def parse-tree)
        expected [[:module_def
                   [:namespace [:Name "Simple"]]
                   [:module_name [:Name "Module"]]
                   [:exposing [:expose_list [:expose_all]]]]] ]
    (is (= module-def expected))))

(deftest test-module-def-swap
  (let [module-def (q/module-def parse-tree)
        new-tree (q/swap-module-def module-def parse-tree)]
    (is (= new-tree parse-tree))))

(cljs.test/run-tests)
