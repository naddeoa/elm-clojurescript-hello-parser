(ns elm-toolkit.lambda-test
  (:require [elm-toolkit.parser :as parser]
            [instaparse.core :as insta]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-lambda
  (let [input "(\\ (a, b) -> a + b)"
        actual (parser/parser input :start :lambda)
        parses (insta/parses parser/parser input :start :lambda)
        parse-count (count parses)
        expected [:lambda
                  [:destructure
                   [:tuple_destructure
                    [:destructure [:variable_destructure [:name "a"]]]
                    [:destructure [:variable_destructure [:name "b"]]]]]
                  [:lambda_body
                   [:function_or_expression
                    [:infix
                     [:expression [:value [:name "a"]]]
                     [:symbol "+"] 
                     [:expression [:value [:name "b"]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-lambda-ignored-arg
  (let [input "(\\ _ -> a )"
        actual (parser/parser input :start :lambda)
        parses (insta/parses parser/parser input :start :lambda)
        parse-count (count parses)
        expected  [:lambda
                   [:destructure [:ignore_arg]]
                   [:lambda_body
                    [:function_or_expression
                     [:expression [:value [:name "a"]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(cljs.test/run-tests)
