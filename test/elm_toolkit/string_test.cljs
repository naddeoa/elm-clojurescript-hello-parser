(ns elm-toolkit.string-test
  (:require [elm-toolkit.parser :as parser]
            [instaparse.core :as insta]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-string
  (let [input "\"fish\""
        actual (parser/parser input :start :string)
        parses (insta/parses parser/parser input :start :string)
        parse-count (count parses)
        expected  [:string "fish"]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-string-empty
  (let [input "\"\""
        actual (parser/parser input :start :string)
        parses (insta/parses parser/parser input :start :string)
        parse-count (count parses)
        expected  [:string ""]]
    (is (= expected actual))
    (is (= parse-count 1))))

;; TODO add test for escaped quotes in a string

(cljs.test/run-tests)
