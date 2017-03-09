(ns elm-toolkit.string-test
  (:require [elm-toolkit.parser :as parser]
            [instaparse.core :as insta]
            #?(:clj [clojure.test :refer [deftest is testing run-tests]]
               :cljs [clojure.test :refer-macros [deftest is testing run-tests]])))

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

#?(:clj (clojure.test/run-tests)
   :cljs (cljs.test/run-tests))
