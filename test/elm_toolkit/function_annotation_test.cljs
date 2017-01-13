(ns elm-toolkit.function_annotation_test
  (:require [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-module
  (let [input "add: Int -> Int -> Int"
        expected (parser/parser input :start :function_annotation)
        actual [:function_annotation
                [:name "add"]
                [:signature
                 [:signature_part [:Name "Int"]]
                 [:signature_part [:Name "Int"]]
                 [:signature_part [:Name "Int"]]]]]
    (is (= expected actual))))

(deftest test-module-nested
  (let [input "map: (a -> b) -> List a -> List b"
        expected (parser/parser input :start :function_annotation)
        actual [:function_annotation
                [:name "map"]
                [:signature
                 [:signature_part
                  [:signature
                   [:signature_part  [:name "a"]]
                   [:signature_part  [:name "b"]]]]
                 [:signature_part  [:Name "List"]  [:type_parameters  [:name "a"]]]
                 [:signature_part  [:Name "List"]  [:type_parameters  [:name "b"]]]]]]
    (is (= expected actual))))

(deftest test-module-value
  (let [input "thing : String"
        expected (parser/parser input :start :function_annotation)
        actual [:function_annotation
                [:name "thing"]
                [:signature  [:signature_part  [:Name "String"]]]]]
    (is (= expected actual))))

(deftest test-module-tuple
  (let [input "zip : a -> b -> (a, b)"
        expected (parser/parser input :start :function_annotation)
        actual  [:function_annotation
                 [:name "zip"]
                 [:signature
                  [:signature_part  [:name "a"]]
                  [:signature_part  [:name "b"]]
                  [:signature_part  [:tuple  [:value  [:name "a"]]  [:value  [:name "b"]]]]]]]
    (is (= expected actual))))

(cljs.test/run-tests)
