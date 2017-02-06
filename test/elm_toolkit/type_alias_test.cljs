(ns elm-toolkit.type-alias-test
  (:require [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-type-alias-simple
  (let [input "type alias Fish = Int"
        actual (parser/parser input :start :definition)
        expected [:definition
                  [:type_alias
                   [:Name "Fish"]
                   [:destructure [:type_destructure [:Name "Int"]]]]]]
    (is (= expected actual))))

(deftest test-type-alias-with-parameters
  (let [input "type alias Fish a = List a"
        actual (parser/parser input :start :definition)
        expected [:definition
                  [:type_alias
                   [:Name "Fish"]
                   [:type_parameters [:type_parameter [:name "a"]]]
                   [:destructure
                    [:type_destructure
                     [:Name "List"]
                     [:type_destructure_argument
                      [:variable_destructure [:name "a"]]]]]]]]
    (is (= expected actual))))

(cljs.test/run-tests)
