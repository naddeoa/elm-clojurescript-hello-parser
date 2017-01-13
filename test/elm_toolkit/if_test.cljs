(ns elm-toolkit.if_test
  (:require [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-module-if
  (let [input "if True then
                   1
               else
                   0"
        expected (parser/parser input :start :if)
        actual [:if
                [:test  [:expression  [:value  [:Name "True"]]]]
                [:true_expression  [:expression  [:value  [:int "1"]]]]
                [:else_expression  [:expression  [:value  [:int "0"]]]]]]
    (is (= expected actual))))

(deftest test-module-if-expressions
  (let [input "if b && c then
                   SomeFunction a b
               else
                   a || b"
        expected (parser/parser input :start :if)
        actual  [:if
                 [:test
                  [:expression
                   [:infix
                    [:expression [:value [:name "b"]]]
                    [:symbol "&&"]
                    [:expression [:value [:name "c"]]]]]]
                 [:true_expression
                  [:expression
                   [:function_call [:Name "SomeFunction"]
                    [:arguments
                     [:argument [:expression [:value [:name "a"]]]]
                     [:argument [:expression [:value [:name "b"]]]]]]]]
                 [:else_expression
                  [:expression
                   [:infix
                    [:expression [:value [:name "a"]]]
                    [:symbol "||"]
                    [:expression [:value [:name "b"]]]]]]]]
    (is (= expected actual))))

(deftest test-module-nested
  (let [input "if a > b then
                   1
               else if a == b then
                   0
               else
                   -1"
        expected (parser/parser input :start :if)
        actual [:if
                [:test
                 [:expression
                  [:infix
                   [:expression [:value [:name "a"]]]
                   [:symbol ">"]
                   [:expression [:value [:name "b"]]]]]]
                [:true_expression [:expression [:value [:int "1"]]]]
                [:else_expression
                 [:expression
                  [:if
                   [:test
                    [:expression
                     [:infix
                      [:expression [:value [:name "a"]]]
                      [:symbol "=="]
                      [:expression [:value [:name "b"]]]]]]
                   [:true_expression [:expression [:value [:int "0"]]]]
                   [:else_expression [:expression [:value [:int "-1"]]]]]]]]]
    (is (= expected actual))))

(cljs.test/run-tests)
