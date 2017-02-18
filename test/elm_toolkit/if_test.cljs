(ns elm-toolkit.if_test
  (:require [elm-toolkit.parser :as parser]
            [instaparse.core :as insta]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-module-if
  (let [input "if True then
                   1
               else
                   0"
        expected (parser/parser input :start :if)
        parses (insta/parses parser/parser input :start :if)
        parse-count (count parses)
        actual [:if
                [:test
                 [:function_or_expression
                  [:expression [:value [:Name "True"]]]]]
                [:true_expression
                 [:function_or_expression
                  [:expression [:value [:int "1"]]]]]
                [:else_expression
                 [:function_or_expression
                  [:expression [:value [:int "0"]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-module-if-expressions
  (let [input "if b && c then
                   SomeFunction a b
               else
                   a || b"
        expected (parser/parser input :start :if)
        parses (insta/parses parser/parser input :start :if)
        parse-count (count parses)
        actual  [:if
                 [:test
                  [:function_or_expression
                   [:expression
                    [:infix
                     [:expression [:value [:name "b"]]]
                     [:symbol "&&"]
                     [:expression [:value [:name "c"]]]]]]]
                 [:true_expression
                  [:function_or_expression
                   [:function_call
                    [:function_name [:Name "SomeFunction"]]
                    [:arguments
                     [:expression [:value [:name "a"]]]
                     [:expression [:value [:name "b"]]]]]]]
                 [:else_expression
                  [:function_or_expression
                   [:expression
                    [:infix
                     [:expression [:value [:name "a"]]]
                     [:symbol "||"] 
                     [:expression [:value [:name "b"]]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-module-nested
  (let [input "if a > b then
                   1
               else if a == b then
                   0
               else
                   -1"
        expected (parser/parser input :start :if)
        parses (insta/parses parser/parser input :start :if)
        parse-count (count parses)
        actual [:if
                [:test
                 [:function_or_expression
                  [:expression
                   [:infix
                    [:expression [:value [:name "a"]]]
                    [:symbol ">"]
                    [:expression [:value [:name "b"]]]]]]]
                [:true_expression
                 [:function_or_expression
                  [:expression [:value [:int "1"]]]]]
                [:else_expression
                 [:function_or_expression
                  [:expression
                   [:if
                    [:test
                     [:function_or_expression
                      [:expression
                       [:infix
                        [:expression [:value [:name "a"]]]
                        [:symbol "=="]
                        [:expression [:value [:name "b"]]]]]]]
                    [:true_expression
                     [:function_or_expression
                      [:expression [:value [:int "0"]]]]]
                    [:else_expression
                     [:function_or_expression
                      [:expression [:value [:int "-1"]]]]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(cljs.test/run-tests)
