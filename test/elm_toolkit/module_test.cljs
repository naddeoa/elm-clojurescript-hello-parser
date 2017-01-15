(ns elm-toolkit.module_test
  (:require [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-module
  (let [input "module Module"
        expected (parser/parser input :start :module_def)
        actual [:module_def [:module_name [:Name "Module"]]]]
    (is (= expected actual))))

(deftest test-module-namespace
  (let [input "module Some.Namespace.Module"
        expected (parser/parser input :start :module_def)
        actual [:module_def
                [:namespace [:Name "Some"] [:Name "Namespace"]]
                [:module_name [:Name "Module"]]]]
    (is (= expected actual))))

(deftest test-module-exposing
  (let [input "module Module exposing (someFunction, SomeType)"
        expected (parser/parser input :start :module_def)
        actual [:module_def
                [:module_name  [:Name "Module"]]
                [:exposing
                 [:expose_list
                  [:fn_name  [:name "someFunction"]]
                  [:type_name  [:Name "SomeType"]]]]]]
    (is (= expected actual))))

(deftest test-module-multiline
  (let [input "module Module
                  exposing
                    ( someFunction
                    , SomeType
                    )"
        expected (parser/parser input :start :module_def)
        actual [:module_def
                [:module_name  [:Name "Module"]]
                [:exposing
                 [:expose_list
                  [:fn_name  [:name "someFunction"]]
                  [:type_name  [:Name "SomeType"]]]]]]
    (is (= expected actual))))

(cljs.test/run-tests)
