(ns elm-toolkit.type-alias-test
  (:require [elm-toolkit.parser :as parser]
            [instaparse.core :as insta]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-type-alias-simple
  (let [input "type alias Fish = Int"
        actual (parser/parser input :start :definition)
        parses (insta/parses parser/parser input :start :definition)
        parse-count (count parses)
        expected [:definition
                  [:type_alias
                   [:Name "Fish"]
                   [:destructure [:type_destructure [:Name "Int"]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-type-alias-with-parameters
  (let [input "type alias Fish a = List a"
        actual (parser/parser input :start :definition)
        parses (insta/parses parser/parser input :start :definition)
        parse-count (count parses)
        expected [:definition
                  [:type_alias
                   [:Name "Fish"]
                   [:type_parameters [:type_parameter [:name "a"]]]
                   [:destructure
                    [:type_destructure
                     [:Name "List"]
                     [:type_destructure_argument
                      [:destructure [:variable_destructure [:name "a"]]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(cljs.test/run-tests)
