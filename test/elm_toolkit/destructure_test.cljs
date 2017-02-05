(ns elm-toolkit.destructure-test
  (:require [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-destructure-type
  (let [input "Type a b c"
        actual (parser/parser input :start :destructure)
        expected [:destructure
                  [:type_destructure
                   [:Name "Type"]
                   [:type_destructure_argument
                    [:variable_destructure [:name "a"]]]
                   [:type_destructure_argument
                    [:variable_destructure [:name "b"]]]
                   [:type_destructure_argument
                    [:variable_destructure [:name "c"]]]]]]
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
                  [:type_destructure
                   [:Name "Type"]
                   [:type_destructure_argument
                    [:variable_destructure [:name "a"]]]
                   [:type_destructure_argument
                    [:destructure
                     [:destructure
                      [:type_destructure
                       [:Name "List"]
                       [:type_destructure_argument
                        [:variable_destructure [:name "b"]]]]]]]]]]
    (is (= expected actual))))

(deftest test-destructure-type-tuple
  (let [input "Type (a,b) (List (List (a,b)))"
        actual (parser/parser input :start :destructure )
        expected [:destructure
                  [:type_destructure
                   [:Name "Type"]
                   [:type_destructure_argument
                    [:tuple_destructure
                     [:destructure [:variable_destructure [:name "a"]]]
                     [:destructure [:variable_destructure [:name "b"]]]]]
                   [:type_destructure_argument
                    [:destructure
                     [:destructure
                      [:type_destructure
                       [:Name "List"]
                       [:type_destructure_argument
                        [:destructure
                         [:destructure
                          [:type_destructure
                           [:Name "List"]
                           [:type_destructure_argument
                            [:tuple_destructure
                             [:destructure
                              [:variable_destructure [:name "a"]]]
                             [:destructure
                              [:variable_destructure [:name "b"]]]]]]]]]]]]]]] ]
    (is (= expected actual))))

(deftest test-destructure-ignored-args
  (let [input "(_, a)"
        actual (parser/parser input :start :destructure)
        expected  [:destructure
                   [:tuple_destructure
                    [:destructure [:ignore_arg]]
                    [:destructure [:variable_destructure [:name "a"]]]]]]
    (is (= expected actual))))

(deftest test-destructure-ignored-args-with-vlaue
  (let [input "(_, 2)"
        actual (parser/parser input :start :destructure)
        expected  [:destructure
                   [:tuple_destructure
                    [:destructure [:ignore_arg]]
                    [:destructure
                     [:value_destructure [:literal [:int "2"]]]]]] ]
    (is (= expected actual))))

(deftest test-destructure-list-empty
  (let [input "[]"
        actual (parser/parser input :start :destructure)
        expected [:destructure [:list_destructure]]]
    (is (= expected actual))))

(deftest test-destructure-list-singleton
  (let [input "[ a ]"
        actual (parser/parser input :start :destructure)
        expected [:destructure
                  [:list_destructure
                   [:list_destructure_items
                    [:destructure [:variable_destructure [:name "a"]]]]]]]
    (is (= expected actual))))

(deftest test-destructure-list-nested
  (let [input "[[a]]"
        actual (parser/parser input :start :destructure)
        expected [:destructure
                  [:list_destructure
                   [:list_destructure_items
                    [:destructure
                     [:list_destructure
                      [:list_destructure_items
                       [:destructure
                        [:variable_destructure [:name "a"]]]]]]]]]]
    (is (= expected actual))))

(deftest test-destructure-list-multiple
  (let [input "[a, b, c]"
        actual (parser/parser input :start :destructure)
        expected [:destructure
                  [:list_destructure
                   [:list_destructure_items
                    [:destructure [:variable_destructure [:name "a"]]]
                    [:destructure [:variable_destructure [:name "b"]]]
                    [:destructure [:variable_destructure [:name "c"]]]]]]]
    (is (= expected actual))))

(cljs.test/run-tests)
