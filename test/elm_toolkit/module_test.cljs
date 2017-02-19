(ns elm-toolkit.module_test
  (:require [elm-toolkit.parser :as parser]
            [instaparse.core :as insta]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-module
  (let [input "module Module"
        expected (parser/parser input :start :module_def)
        parses (insta/parses parser/parser input)
        parse-count (count parses)
        actual [:module_def [:module_name [:Name "Module"]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-module-namespace
  (let [input "module Some.Namespace.Module"
        expected (parser/parser input :start :module_def)
        parses (insta/parses parser/parser input)
        parse-count (count parses)
        actual [:module_def
                [:namespace [:Name "Some"] [:Name "Namespace"]]
                [:module_name [:Name "Module"]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-module-exposing
  (let [input "module Module exposing (someFunction, SomeType)"
        expected (parser/parser input :start :module_def)
        parses (insta/parses parser/parser input)
        parse-count (count parses)
        actual [:module_def
                [:module_name [:Name "Module"]]
                [:exposing
                 [:expose_list
                  [:fn_name [:name "someFunction"]]
                  [:type_name [:Name "SomeType"]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-module-multiline
  (let [input "module Module
                  exposing
                    ( someFunction
                    , SomeType
                    )"
        expected (parser/parser input :start :module_def)
        parses (insta/parses parser/parser input)
        parse-count (count parses)
        actual [:module_def
                [:module_name [:Name "Module"]]
                [:exposing
                 [:expose_list
                  [:fn_name [:name "someFunction"]]
                  [:type_name [:Name "SomeType"]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-module-exposing-all
  (let [input "module Module exposing (..)"
        expected (parser/parser input :start :module_def)
        parses (insta/parses parser/parser input)
        parse-count (count parses)
        actual [:module_def
                [:module_name [:Name "Module"]]
                [:exposing [:expose_list [:expose_all]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(cljs.test/run-tests)
