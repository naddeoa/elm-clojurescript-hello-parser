(ns elm-toolkit.let-test
  (:require [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))


(deftest test-let
  (let [input "let
                   x = 1
               in
                   x"
        actual (parser/parser input :start :let)
        expected [:let
                  [:assignment
                   [:destructure [:variable_destructure [:name "x"]]]
                   [:expression [:value [:int "1"]]]]
                  [:in_expression
                   [:expression [:value [:name "x"]]]]]]
    (is (= expected actual))))

(deftest test-let-tuple
  (let [input "let
                   (a,b) = someTuple
               in
                   a"
        actual (parser/parser input :start :let)
        expected [:let
                  [:assignment
                   [:destructure
                    [:tuple_destructure
                     [:destructure [:variable_destructure [:name "a"]]]
                     [:destructure [:variable_destructure [:name "b"]]]]]
                   [:expression
                    [:value [:name "someTuple"]]]]
                  [:in_expression
                   [:expression [:value [:name "a"]]]]]]
    (is (= expected actual))))


(deftest test-let-type-destructure
  (let [input "let
                   Msg string = msg
               in
                   string"
        actual (parser/parser input :start :let)
        expected [:let
                  [:assignment
                   [:destructure
                    [:type_destructure
                     [:Name "Msg"]
                     [:type_destructure_argument
                      [:variable_destructure [:name "string"]]]]]
                   [:expression [:value [:name "msg"]]]]
                  [:in_expression [:expression [:value [:name "string"]]]]]]
    (is (= expected actual))))

(cljs.test/run-tests)
