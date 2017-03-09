(ns elm-toolkit.record-destructure-test
  (:require  [elm-toolkit.parser :as parser]
             [instaparse.core :as insta]
             #?(:clj [clojure.test :refer [deftest is testing run-tests]]
                :cljs [clojure.test :refer-macros [deftest is testing run-tests]])))

(deftest test-destructure-record-empty
  (let [input "{}"
        actual (parser/parser input :start :record_destructure)
        parses (insta/parses parser/parser input :start :record_destructure)
        parse-count (count parses)
        expected [:record_destructure]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-destructure-record
  (let [input "{a: String}"
        actual (parser/parser input :start :record_destructure)
        parses (insta/parses parser/parser input :start :record_destructure)
        parse-count (count parses)
        expected [:record_destructure
                  [:record_destructure_items
                   [:annotation
                    [:name "a"]
                    [:signature
                     [:destructure [:type_destructure [:Name "String"]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-destructure-record-multiple-simple
  (let [input "{ a: String
               , b: Int
               }"
        actual (parser/parser input :start :record_destructure)
        parses (insta/parses parser/parser input :start :record_destructure)
        parse-count (count parses)
        expected [:record_destructure
                  [:record_destructure_items
                   [:annotation
                    [:name "a"]
                    [:signature
                     [:destructure [:type_destructure [:Name "String"]]]]]
                   [:annotation
                    [:name "b"]
                    [:signature
                     [:destructure [:type_destructure [:Name "Int"]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-destructure-record-multiple-hard
  (let [input "{ a: List String
               , b: Int b c
               }"
        actual (parser/parser input :start :record_destructure)
        parses (insta/parses parser/parser input :start :record_destructure)
        parse-count (count parses)
        expected [:record_destructure
                  [:record_destructure_items
                   [:annotation
                    [:name "a"]
                    [:signature
                     [:destructure
                      [:type_destructure
                       [:Name "List"]
                       [:type_destructure_argument
                        [:destructure
                         [:type_destructure [:Name "String"]]]]]]]]
                   [:annotation
                    [:name "b"]
                    [:signature
                     [:destructure
                      [:type_destructure
                       [:Name "Int"]
                       [:type_destructure_argument
                        [:destructure [:variable_destructure [:name "b"]]]]
                       [:type_destructure_argument
                        [:destructure
                         [:variable_destructure [:name "c"]]]]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

#?(:clj (clojure.test/run-tests)
   :cljs (cljs.test/run-tests))
