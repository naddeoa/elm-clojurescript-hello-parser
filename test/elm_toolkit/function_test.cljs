(ns elm-toolkit.function-test
  (:require [elm-toolkit.parser :as parser]
            [instaparse.core :as insta]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-function-definition
  (let [input "function arg1 arg2 = arg1 + arg2"
        actual (parser/parser input :start :definition)
        parses (insta/parses parser/parser input :start :definition)
        parse-count (count parses)
        expected [:definition
                  [:function
                   [:name "function"]
                   [:function_parameters
                    [:destructure [:variable_destructure [:name "arg1"]]]
                    [:destructure [:variable_destructure [:name "arg2"]]]]
                   [:function_body
                    [:expression
                     [:infix
                      [:expression [:value [:name "arg1"]]]
                      [:symbol "+"]
                      [:expression [:value [:name "arg2"]]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-function-definition-destructure-arg
  (let [input "peek (Source a f) = f"
        actual (parser/parser input :start :definition)
        parses (insta/parses parser/parser input :start :definition)
        parse-count (count parses)
        expected [:definition
                  [:function
                   [:name "peek"]
                   [:function_parameters
                    [:destructure
                     [:destructure
                      [:type_destructure
                       [:Name "Source"]
                       [:type_destructure_argument
                        [:destructure [:variable_destructure [:name "a"]]]]
                       [:type_destructure_argument
                        [:destructure
                         [:variable_destructure [:name "f"]]]]]]]]
                   [:function_body [:expression [:value [:name "f"]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-function-call
  (let [input "Just \"string\""
        actual (parser/parser input :start :function_call)
        parses (insta/parses parser/parser input :start :function_call)
        parse-count (count parses)
        expected [:function_call
                  [:function_name [:Name "Just"]]
                  [:arguments [:expression [:value [:string "string"]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-function-call-multiline
  (let [input "funcName
                  a
                  b
                  c"
        actual (parser/parser input :start :function_call)
        parses (insta/parses parser/parser input :start :function_call)
        parse-count (count parses)
        expected [:function_call
                  [:function_name [:name "funcName"]]
                  [:arguments
                   [:expression [:value [:name "a"]]]
                   [:expression [:value [:name "b"]]]
                   [:expression [:value [:name "c"]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(cljs.test/run-tests)
