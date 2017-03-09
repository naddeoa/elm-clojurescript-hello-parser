(ns elm-toolkit.destructure-test
  (:require [elm-toolkit.parser :as parser]
            [instaparse.core :as insta]
            #?(:clj [clojure.test :refer [deftest is testing run-tests]]
               :cljs [clojure.test :refer-macros [deftest is testing run-tests]])))

(deftest test-destructure-type
  (let [input "Type a b c"
        actual (parser/parser input :start :destructure)
        parses (insta/parses parser/parser input :start :destructure)
        parse-count (count parses)
        expected [:destructure
                  [:type_destructure
                   [:Name "Type"]
                   [:type_destructure_argument
                    [:destructure [:variable_destructure [:name "a"]]]]
                   [:type_destructure_argument
                    [:destructure [:variable_destructure [:name "b"]]]]
                   [:type_destructure_argument
                    [:destructure [:variable_destructure [:name "c"]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-destructure-tuple
  (let [input "(a, b)"
        actual (parser/parser input :start :destructure)
        parses (insta/parses parser/parser input :start :destructure)
        parse-count (count parses)
        expected [:destructure
                  [:tuple_destructure
                   [:destructure [:variable_destructure [:name "a"]]]
                   [:destructure [:variable_destructure [:name "b"]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-destructure-threeple
  (let [input "(a, String, b)"
        actual (parser/parser input :start :destructure )
        parses (insta/parses parser/parser input :start :destructure)
        parse-count (count parses)
        expected [:destructure
                  [:tuple_destructure
                   [:destructure [:variable_destructure [:name "a"]]]
                   [:destructure [:type_destructure [:Name "String"]]]
                   [:destructure [:variable_destructure [:name "b"]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-destructure-nested-tuple
  (let [input "(a, (b, c))"
        actual (parser/parser input :start :destructure )
        parses (insta/parses parser/parser input :start :destructure)
        parse-count (count parses)
        expected [:destructure
                  [:tuple_destructure
                   [:destructure [:variable_destructure [:name "a"]]]
                   [:destructure
                    [:tuple_destructure
                     [:destructure [:variable_destructure [:name "b"]]]
                     [:destructure [:variable_destructure [:name "c"]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-destructure-nested-type
  (let [input "Type a (List b)"
        actual (parser/parser input :start :destructure )
        parses (insta/parses parser/parser input :start :destructure)
        parse-count (count parses)
        expected [:destructure
                  [:type_destructure
                   [:Name "Type"]
                   [:type_destructure_argument
                    [:destructure [:variable_destructure [:name "a"]]]]
                   [:type_destructure_argument
                    [:destructure
                     [:destructure
                      [:type_destructure
                       [:Name "List"]
                       [:type_destructure_argument
                        [:destructure
                         [:variable_destructure [:name "b"]]]]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-destructure-type-tuple
  (let [input "Type (a,b) (List (List (a,b)))"
        actual (parser/parser input :start :destructure )
        parses (insta/parses parser/parser input :start :destructure)
        parse-count (count parses)
        expected [:destructure
           [:type_destructure
            [:Name "Type"]
            [:type_destructure_argument
             [:destructure
              [:tuple_destructure
               [:destructure [:variable_destructure [:name "a"]]]
               [:destructure [:variable_destructure [:name "b"]]]]]]
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
                     [:destructure
                      [:tuple_destructure
                       [:destructure
                        [:variable_destructure [:name "a"]]]
                       [:destructure
                        [:variable_destructure
                         [:name "b"]]]]]]]]]]]]]]]] ]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-destructure-ignored-args
  (let [input "(_, a)"
        actual (parser/parser input :start :destructure)
        parses (insta/parses parser/parser input :start :destructure)
        parse-count (count parses)
        expected  [:destructure
                   [:tuple_destructure
                    [:destructure [:ignore_arg]]
                    [:destructure [:variable_destructure [:name "a"]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-destructure-ignored-args-with-vlaue
  (let [input "(_, 2)"
        actual (parser/parser input :start :destructure)
        parses (insta/parses parser/parser input :start :destructure)
        parse-count (count parses)
        expected  [:destructure
                   [:tuple_destructure
                    [:destructure [:ignore_arg]]
                    [:destructure
                     [:value_destructure [:literal [:int "2"]]]]]] ]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-destructure-list-empty
  (let [input "[]"
        actual (parser/parser input :start :destructure)
        parses (insta/parses parser/parser input :start :destructure)
        parse-count (count parses)
        expected [:destructure [:list_destructure]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-destructure-list-singleton
  (let [input "[ a ]"
        actual (parser/parser input :start :destructure)
        parses (insta/parses parser/parser input :start :destructure)
        parse-count (count parses)
        expected [:destructure
                  [:list_destructure
                   [:list_destructure_items
                    [:destructure [:variable_destructure [:name "a"]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-destructure-list-nested
  (let [input "[[a]]"
        actual (parser/parser input :start :destructure)
        parses (insta/parses parser/parser input :start :destructure)
        parse-count (count parses)
        expected [:destructure
                  [:list_destructure
                   [:list_destructure_items
                    [:destructure
                     [:list_destructure
                      [:list_destructure_items
                       [:destructure
                        [:variable_destructure [:name "a"]]]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-destructure-list-multiple
  (let [input "[a, b, c]"
        actual (parser/parser input :start :destructure)
        parses (insta/parses parser/parser input :start :destructure)
        parse-count (count parses)
        expected [:destructure
                  [:list_destructure
                   [:list_destructure_items
                    [:destructure [:variable_destructure [:name "a"]]]
                    [:destructure [:variable_destructure [:name "b"]]]
                    [:destructure [:variable_destructure [:name "c"]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

#?(:clj (clojure.test/run-tests)
   :cljs (cljs.test/run-tests))
