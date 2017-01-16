(ns elm-toolkit.lambda-test
  (:require [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-lambda
  (let [input "(\\ (a, b) -> a + b)"
        actual (parser/parser input :start :lambda)
        expected [:lambda
                  [:destructure
                   [:tuple_destructure
                    [:destructure
                     [:variable_destructure [:name "a"]]]
                    [:destructure
                     [:variable_destructure [:name "b"]]]]]
                  [:expression
                   [:infix
                    [:expression [:value [:name "a"]]] [:symbol "+"] [:expression [:value [:name "b"]]]]]]]
    (is (= expected actual))))

(deftest test-lambda-ignored-arg
  (let [input "(\\ _ -> a )"
        actual (parser/parser input :start :lambda)
        expected  [:lambda
                   [:destructure [:ignore_arg]]
                   [:expression [:value [:name "a"]]]] ]
    (is (= expected actual))))

(cljs.test/run-tests)
