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

(cljs.test/run-tests)
