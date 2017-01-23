(ns elm-toolkit.tuple-test
  (:require  [elm-toolkit.parser :as parser]
             [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-tuple
  (let [input "(a , b)"
        actual (parser/parser input :start :tuple)
        expected [:tuple
                  [:expression [:value [:name "a"]]]
                  [:expression [:value [:name "b"]]]]]
    (is (= expected actual))))

(deftest test-tuple-function
  (let [input "(fn 1 , b)"
        actual (parser/parser input :start :tuple)
        expected [:tuple
                  [:expression
                   [:function_call
                    [:name "fn"]
                    [:arguments
                     [:argument [:expression [:value [:int "1"]]]]]]]
                  [:expression [:value [:name "b"]]]]]
    (is (= expected actual))))

(deftest test-tuple-nested
  (let [input "(1, (2,3))"
        actual (parser/parser input :start :tuple)
        expected [:tuple
                  [:expression [:value [:int "1"]]]
                  [:expression
                   [:value
                    [:tuple
                     [:expression [:value [:int "2"]]]
                     [:expression [:value [:int "3"]]]]]]]]
    (is (= expected actual))))

(deftest test-value-tuple
  (let [input "(1,2,3)"
        expected (parser/parser input :start :value)
        actual [:value
                [:tuple
                 [:expression [:value [:int "1"]]]
                 [:expression [:value [:int "2"]]]
                 [:expression [:value [:int "3"]]]]]]
    (is (= expected actual))))

(cljs.test/run-tests)
