(ns elm-toolkit.annotation-test
  (:require [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-annotation
  (let [input "add: Int -> Int -> Int"
        expected (parser/parser input :start :annotation)
        actual [:annotation
                [:name "add"]
                [:signature
                 [:destructure
                  [:function_destructure
                   [:destructure [:type_destructure [:Name "Int"]]]
                   [:destructure [:type_destructure [:Name "Int"]]]
                   [:destructure [:type_destructure [:Name "Int"]]]]]]]]
    (is (= expected actual))))

(deftest test-annotation-nested
  (let [input "map: (a -> b) -> List a -> List b"
        expected (parser/parser input :start :annotation)
        actual [:annotation
                [:name "map"]
                [:signature
                 [:destructure
                  [:function_destructure
                   [:destructure
                    [:destructure
                     [:function_destructure
                      [:destructure [:variable_destructure [:name "a"]]]
                      [:destructure [:variable_destructure [:name "b"]]]]]]
                   [:destructure
                    [:type_destructure
                     [:Name "List"]
                     [:type_destructure_argument
                      [:variable_destructure [:name "a"]]]]]
                   [:destructure
                    [:type_destructure
                     [:Name "List"]
                     [:type_destructure_argument
                      [:variable_destructure [:name "b"]]]]]]]]]]
    (is (= expected actual))))

(deftest test-annotation-value
  (let [input "thing : String"
        expected (parser/parser input :start :annotation)
        actual [:annotation
                [:name "thing"]
                [:signature
                 [:destructure [:type_destructure [:Name "String"]]]]]]
    (is (= expected actual))))

(deftest test-annotation-tuple
  (let [input "zip : a -> b -> (a, b)"
        expected (parser/parser input :start :annotation)
        actual [:annotation
                [:name "zip"]
                [:signature
                 [:destructure
                  [:function_destructure
                   [:destructure [:variable_destructure [:name "a"]]]
                   [:destructure [:variable_destructure [:name "b"]]]
                   [:destructure
                    [:tuple_destructure
                     [:destructure [:variable_destructure [:name "a"]]]
                     [:destructure [:variable_destructure [:name "b"]]]]]]]]]]
    (is (= expected actual))))

(deftest test-annotation-nested-types
  (let [input "add: Int -> List (List Int)"
        actual (parser/parser input :start :annotation)
        expected [:annotation
                  [:name "add"]
                  [:signature
                   [:destructure
                    [:function_destructure
                     [:destructure [:type_destructure [:Name "Int"]]]
                     [:destructure
                      [:type_destructure
                       [:Name "List"]
                       [:type_destructure_argument
                        [:destructure
                         [:destructure
                          [:type_destructure
                           [:Name "List"]
                           [:type_destructure_argument
                            [:destructure
                             [:type_destructure [:Name "Int"]]]]]]]]]]]]]]]
    (is (= expected actual))))

(cljs.test/run-tests)
