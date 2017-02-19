(ns elm-toolkit.import_test
  (:require [elm-toolkit.parser :as parser]
            [instaparse.core :as insta]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-import
  (let [input "import Array"
        expected (parser/parser input :start :import)
        parses (insta/parses parser/parser input)
        parse-count (count parses)
        actual [:import
                [:module_name  [:Name "Array"]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-import-as
  (let [input "import Array as A"
        expected (parser/parser input :start :import)
        parses (insta/parses parser/parser input)
        parse-count (count parses)
        actual [:import [:module_name [:Name "Array"]] [:import_as [:Name "A"]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-import-namespace
  (let [input "import Something.Else.Array"
        expected (parser/parser input :start :import)
        parses (insta/parses parser/parser input)
        parse-count (count parses)
        actual [:import
                [:namespace [:Name "Something"] [:Name "Else"]]
                [:module_name [:Name "Array"]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-import-exposes-stuff
  (let [input "import Array exposing (someFn, SomeType)"
        expected (parser/parser input :start :import)
        parses (insta/parses parser/parser input)
        parse-count (count parses)
        actual [:import
                [:module_name  [:Name "Array"]]
                [:exposing
                 [:expose_list
                  [:fn_name  [:name "someFn"]]
                  [:type_name  [:Name "SomeType"]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-import-exposes-all
  (let [input "import Array exposing (..)"
        expected (parser/parser input :start :import)
        parses (insta/parses parser/parser input)
        parse-count (count parses)
        actual [:import
                [:module_name  [:Name "Array"]]
                [:exposing
                 [:expose_list [:expose_all]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-import-multiple
  (let [input "import Array
               import List
               import Lazy.List"
        expected (parser/parser input)
        parses (insta/parses parser/parser input)
        parse-count (count parses)
        actual [:start
                [:block [:import [:module_name [:Name "Array"]]]]
                [:block [:import [:module_name [:Name "List"]]]]
                [:block
                 [:import
                  [:namespace [:Name "Lazy"]]
                  [:module_name [:Name "List"]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(cljs.test/run-tests)

