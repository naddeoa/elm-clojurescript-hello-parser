(ns elm-toolkit.infix-test
  (:require  [elm-toolkit.parser :as parser]
             [instaparse.core :as insta]
             [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-infix
  (let [input "1 % 5"
        actual (parser/parser input :start :infix)
        parses (insta/parses parser/parser input :start :infix)
        parse-count (count parses)
        expected [:infix
                  [:infix_argument [:expression [:value [:int "1"]]]]
                  [:symbol "%"]
                  [:infix_argument [:expression [:value [:int "5"]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))


(deftest test-infix-series
  (let [input "1 % 5 % 10"
        actual (parser/parser input :start :infix)
        parses (insta/parses parser/parser input :start :infix)
        parse-count (count parses)
        expected  [:infix
                   [:infix_argument [:expression [:value [:int "1"]]]]
                   [:symbol "%"]
                   [:infix_argument [:expression [:value [:int "5"]]]]
                   [:symbol "%"]
                   [:infix_argument [:expression [:value [:int "10"]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-infix-series-parens-left
  (let [input "(1 % 5) % 10"
        actual (parser/parser input :start :infix)
        parses (insta/parses parser/parser input :start :infix)
        parse-count (count parses)
        expected [:infix
                  [:infix_argument
                   [:expression
                    [:infix
                     [:infix_argument [:expression [:value [:int "1"]]]]
                     [:symbol "%"]
                     [:infix_argument [:expression [:value [:int "5"]]]]]]]
                  [:symbol "%"]
                  [:infix_argument [:expression [:value [:int "10"]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-infix-series-parens-right
  (let [input "1 % (5 % 10)"
        actual (parser/parser input :start :infix)
        parses (insta/parses parser/parser input :start :infix)
        parse-count (count parses)
        expected [:infix
                  [:infix_argument [:expression [:value [:int "1"]]]]
                  [:symbol "%"]
                  [:infix_argument
                   [:expression
                    [:infix
                     [:infix_argument [:expression [:value [:int "5"]]]]
                     [:symbol "%"]
                     [:infix_argument [:expression [:value [:int "10"]]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(cljs.test/run-tests)
