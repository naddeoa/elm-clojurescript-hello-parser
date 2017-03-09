(ns elm-toolkit.definition-test
  (:require [elm-toolkit.parser :as parser]
            #?(:clj [clojure.test :refer [deftest is testing run-tests]]
               :cljs [clojure.test :refer-macros [deftest is testing run-tests]])))

(deftest test-definition-value
  (let [input "x = 1"
        actual (parser/parser input :start :definition)
        expected [:definition
                  [:value_definition
                   [:name "x"]
                   [:expression [:value [:int "1"]]]]]]
    (is (= expected actual))))

#?(:clj (clojure.test/run-tests)
   :cljs (cljs.test/run-tests))
