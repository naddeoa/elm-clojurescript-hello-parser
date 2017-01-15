(ns elm-toolkit.render-test
  (:require [elm-toolkit.render :as r]
            [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-render-import
  (let [input "import Native.Array exposing (..)"
        parse-tree (parser/parser input :start :import)
        render (r/render-import parse-tree)]
    (is (= input render))))

(deftest test-render-import
  (let [input "import Native.Array exposing (someFn, Fish)"
        parse-tree (parser/parser input :start :import)
        render (r/render-import parse-tree)]
    (is (= input render))))

(deftest test-render-import-simple
  (let [input "import Array"
        parse-tree (parser/parser input :start :import)
        render (r/render-import parse-tree)]
    (is (= input render))))

(deftest test-render-import-long-namespace
  (let [input "import A.B.C.EEE.Array"
        parse-tree (parser/parser input :start :import)
        render (r/render-import parse-tree)]
    (is (= input render))))

(cljs.test/run-tests)