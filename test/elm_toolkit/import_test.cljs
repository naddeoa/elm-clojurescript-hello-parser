(ns elm-toolkit.import_test
  (:require [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-import
  (let [input "import Array"
        expected (parser/parser input :start :import)
        actual [:import
                [:module_name  [:Name "Array"]]]]
    (is (= expected actual))))

(deftest test-import-as
  (let [input "import Array as A"
        expected (parser/parser input :start :import)
        actual [:import [:module_name [:Name "Array"]] [:import_as [:Name "A"]]]]
    (is (= expected actual))))

(deftest test-import-namespace
  (let [input "import Something.Else.Array"
        expected (parser/parser input :start :import)
        actual [:import
                [:namespace [:Name "Something"] [:Name "Else"]]
                [:module_name [:Name "Array"]]]]
    (is (= expected actual))))

(deftest test-import-exposes-stuff
  (let [input "import Array exposing (someFn, SomeType)"
        expected (parser/parser input :start :import)
        actual [:import
                [:module_name  [:Name "Array"]]
                [:exposing
                 [:expose_list
                  [:fn_name  [:name "someFn"]]
                  [:type_name  [:Name "SomeType"]]]]]]
    (is (= expected actual))))

(deftest test-import-exposes-all
  (let [input "import Array exposing (..)"
        expected (parser/parser input :start :import)
        actual [:import
                [:module_name  [:Name "Array"]]
                [:exposing
                 [:expose_list [:expose_all]]]]]
    (is (= expected actual))))

(deftest test-import-multiple
  (let [input "import Array
               import List
               import Lazy.List"
        expected (parser/parser input :start :imports)
        actual [:imports
                [:import  [:module_name  [:Name "Array"]]]
                [:import  [:module_name  [:Name "List"]]]
                [:import  [:namespace  [:Name "Lazy"]]  [:module_name  [:Name "List"]]]]]
    (is (= expected actual))))

(deftest test-import-multiple-hard
  (let [input "import Array exposing (head, tail)
               import List exposing (..)"
        expected (parser/parser input :start :imports)
        actual [:imports
                [:import  [:module_name  [:Name "Array"]]
                 [:exposing
                  [:expose_list
                   [:fn_name  [:name "head"]]
                   [:fn_name  [:name "tail"]]]]]
                [:import  [:module_name  [:Name "List"]]
                 [:exposing  [:expose_list  [:expose_all]]]]]]
    (is (= expected actual))))

(cljs.test/run-tests)

