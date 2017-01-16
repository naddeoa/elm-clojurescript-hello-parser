(ns elm-toolkit.definition-test
  (:require [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-definition-value
  (let [input "x = 1"
        actual (parser/parser input :start :definition)
        expected [:definition
                  [:value_definition
                   [:name "x"]
                   [:expression [:value [:int "1"]]]]]]
    (is (= expected actual))))

(deftest test-definition-function
  (let [input "function arg1 arg2 = arg1 + arg2"
        actual (parser/parser input :start :definition)
        expected [:definition
                  [:function
                   [:name "function"]
                   [:function_parameters [:name "arg1"] [:name "arg2"]]
                   [:expression
                    [:infix
                     [:expression
                      [:value [:name "arg1"]]]
                     [:symbol "+"]
                     [:expression
                      [:value [:name "arg2"]]]]]]]]
    (is (= expected actual))))

(deftest test-definition-annotation
  (let [input "function : Int -> Int"
        actual (parser/parser input :start :definition)
        expected [:definition
                  [:annotation
                   [:name "function"]
                   [:signature
                    [:signature_part [:Name "Int"]]
                    [:signature_part [:Name "Int"]]]]]]
    (is (= expected actual))))

(deftest test-definition-type
  (let [input "type Fish a = Salmon a | Cod"
        actual (parser/parser input :start :definition)
        expected [:definition
                  [:type
                   [:Name "Fish"]
                   [:type_parameters [:name "a"]]
                   [:options
                    [:option [:Name "Salmon"] [:type_parameters [:name "a"]]]
                    [:option [:Name "Cod"]]]]]]
    (is (= expected actual))))

(cljs.test/run-tests)
