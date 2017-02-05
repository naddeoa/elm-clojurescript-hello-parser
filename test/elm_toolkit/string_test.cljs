(ns elm-toolkit.string-test
  (:require [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-string
  (let [input "\"fish\""
        actual (parser/parser input :start :string)
        expected  [:string "fish"]]
    (is (= expected actual))))

(deftest test-string-empty
  (let [input "\"\""
        actual (parser/parser input :start :string)
        expected  [:string ""]]
    (is (= expected actual))))

;; TODO add test for escaped quotes in a string

(cljs.test/run-tests)
