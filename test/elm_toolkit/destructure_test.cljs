(ns elm-toolkit.destructure-test
  (:require [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-destructure-type
  (let [input "Type a b c"
        actual (parser/parser input :start :destructure)
        expected [:destructure
                  [:type_destructure
                   [:Name "Type"]
                   [:destructure [:variable_destructure [:name "a"]]]
                   [:destructure [:variable_destructure [:name "b"]]]
                   [:destructure [:variable_destructure [:name "c"]]]]]]
    (is (= expected actual))))

(deftest test-destructure-tuple
  (let [input "(a, b)"
        actual (parser/parser input :start :destructure)
        expected [:destructure
                  [:tuple_destructure
                   [:destructure [:variable_destructure [:name "a"]]]
                   [:destructure [:variable_destructure [:name "b"]]]]]]
    (is (= expected actual))))

(deftest test-destructure-threeple
  (let [input "(a, String, b)"
        actual (parser/parser input :start :destructure )
        expected [:destructure
                  [:tuple_destructure
                   [:destructure [:variable_destructure [:name "a"]]]
                   [:destructure [:type_destructure [:Name "String"]]]
                   [:destructure [:variable_destructure [:name "b"]]]]]]
    (is (= expected actual))))

(deftest test-destructure-nested-tuple
  (let [input "(a, (b, c))"
        actual (parser/parser input :start :destructure )
        expected [:destructure
                  [:tuple_destructure
                   [:destructure [:variable_destructure [:name "a"]]]
                   [:destructure
                    [:tuple_destructure
                     [:destructure [:variable_destructure [:name "b"]]]
                     [:destructure [:variable_destructure [:name "c"]]]]]]]]
    (is (= expected actual))))

(deftest test-destructure-nested-type
  (let [input "Type a (List b)"
        actual (parser/parser input :start :destructure )
        expected [:destructure
                  [:type_destructure [:Name "Type"]
                   [:destructure [:variable_destructure [:name "a"]]]
                   [:destructure
                    [:destructure
                     [:type_destructure [:Name "List"]
                      [:destructure [:variable_destructure [:name "b"]]]]]]]]]
    (is (= expected actual))))

(deftest test-destructure-type-tuple
  (let [input "Type (a,b) (List (List (a,b)))"
        actual (parser/parser input :start :destructure )
        expected [:destructure
                  [:type_destructure [:Name "Type"]
                   [:destructure
                    [:tuple_destructure
                     [:destructure [:variable_destructure [:name "a"]]]
                     [:destructure [:variable_destructure [:name "b"]]]]]
                   [:destructure
                    [:destructure
                     [:type_destructure [:Name "List"]
                      [:destructure
                       [:destructure
                        [:type_destructure [:Name "List"]
                         [:destructure
                          [:tuple_destructure
                           [:destructure [:variable_destructure [:name "a"]]]
                           [:destructure [:variable_destructure [:name "b"]]]]]]]]]]]]]]
    (is (= expected actual))))

(deftest test-destructure-ignored-args
  (let [input "(_, a)"
        actual (parser/parser input :start :destructure)
        expected  [:destructure
                   [:tuple_destructure
                    [:destructure [:ignore_arg]]
                    [:destructure [:variable_destructure [:name "a"]]]]]]
    (is (= expected actual))))

(cljs.test/run-tests)
