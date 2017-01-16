(ns elm-toolkit.render-test
  (:require [elm-toolkit.render :as r]
            [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

;;
;; Tests for import rendering
;;

(deftest test-render-import
  (let [input "import Native.Array exposing (..)"
        parse-tree (parser/parser input :start :import)
        render (r/render-import parse-tree)]
    (is (= input render))))

(deftest test-render-import-exposing
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


;;
;; Tests for module rendering
;;

(deftest test-render-module-simple
  (let [input "module Fish"
        parse-tree (parser/parser input :start :module_def)
        render (r/render-module parse-tree)]
    (is (= input render))))

(deftest test-render-module-namespace
  (let [input "module My.Project.Fish"
        parse-tree (parser/parser input :start :module_def)
        render (r/render-module parse-tree)]
    (is (= input render))))

(deftest test-render-module-exposing-all
  (let [input "module My.Project.Fish exposing (..)"
        parse-tree (parser/parser input :start :module_def)
        render (r/render-module parse-tree)]
    (is (= input render))))

(deftest test-render-module-exposing-stuff
  (let [input "module My.Project.Fish exposing (func, Type, otherFunc, OtherType)"
        parse-tree (parser/parser input :start :module_def)
        render (r/render-module parse-tree)]
    (is (= input render))))

(deftest test-render-module-exposing-stuff
  (let [input "A.B."
        parse-tree (parser/parser input :start :namespace)
        render (r/render-namespace parse-tree)]
    (is (= input render))))

(cljs.test/run-tests)
