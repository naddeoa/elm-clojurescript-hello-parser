(ns elm-toolkit.annotation-test
  (:require [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-annotation
  (let [input "add: Int -> Int -> Int"
        expected (parser/parser input :start :annotation)
        actual [:annotation
                [:name "add"]
                [:signature
                 [:signature_part
                  [:destructure
                   [:type_destructure [:Name "Int"]]]]
                 [:signature_part
                  [:destructure
                   [:type_destructure [:Name "Int"]]]]
                 [:signature_part
                  [:destructure
                   [:type_destructure [:Name "Int"]]]]]]]
    (is (= expected actual))))

(deftest test-annotation-nested
  (let [input "map: (a -> b) -> List a -> List b"
        expected (parser/parser input :start :annotation)
        actual [:annotation
                [:name "map"]
                [:signature
                 [:signature_part
                  [:signature
                   [:signature_part [:destructure [:variable_destructure [:name "a"]]]]
                   [:signature_part [:destructure [:variable_destructure [:name "b"]]]]]]
                 [:signature_part
                  [:destructure
                   [:type_destructure
                    [:Name "List"]
                    [:destructure
                     [:variable_destructure [:name "a"]]]]]]
                 [:signature_part
                  [:destructure
                   [:type_destructure
                    [:Name "List"]
                    [:destructure
                     [:variable_destructure [:name "b"]]]]]]]]]
    (is (= expected actual))))

(deftest test-annotation-value
  (let [input "thing : String"
        expected (parser/parser input :start :annotation)
        actual [:annotation [:name "thing"] [:signature [:signature_part [:destructure [:type_destructure [:Name "String"]]]]]]]
    (is (= expected actual))))

(deftest test-annotation-tuple
  (let [input "zip : a -> b -> (a, b)"
        expected (parser/parser input :start :annotation)
        actual [:annotation
                [:name "zip"]
                [:signature
                 [:signature_part
                  [:destructure [:variable_destructure [:name "a"]]]]
                 [:signature_part
                  [:destructure [:variable_destructure [:name "b"]]]]
                 [:signature_part
                  [:destructure
                   [:tuple_destructure
                    [:destructure [:variable_destructure [:name "a"]]]
                    [:destructure [:variable_destructure [:name "b"]]]]]]]]]
    (is (= expected actual))))

(deftest test-annotation-nested-types
  (let [input "add: Int -> List (List Int)"
        expected (parser/parser input :start :annotation)
        actual [:annotation
                [:name "add"]
                [:signature
                 [:signature_part
                  [:destructure
                   [:type_destructure [:Name "Int"]]]]
                 [:signature_part
                  [:destructure
                   [:type_destructure [:Name "List"]
                    [:destructure
                     [:destructure
                      [:type_destructure [:Name "List"]
                       [:destructure
                        [:type_destructure [:Name "Int"]]]]]]]]]]]]
    (is (= expected actual))))

(cljs.test/run-tests)
