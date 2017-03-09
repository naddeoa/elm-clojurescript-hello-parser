(ns elm-toolkit.comment-test
  (:require [elm-toolkit.parser :as parser]
            [instaparse.core :as insta]
            #?(:clj [clojure.test :refer [deftest is testing run-tests]]
               :cljs [clojure.test :refer-macros [deftest is testing run-tests]])))

(deftest test-comment-singleline
  (let [input "-- normal comment"
        actual (parser/parser input :start :comment)
        parses (insta/parses parser/parser input :start :comment)
        parse-count (count parses)
        expected [:comment [:singleline_comment " normal comment"]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-comment-singleline-space-in-front
  (let [input "    -- normal comment"
        actual (parser/parser input :start :comment)
        parses (insta/parses parser/parser input :start :comment)
        parse-count (count parses)
        expected [:comment [:singleline_comment " normal comment"]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-comment-multiline
  (let [input "{- bigger comment -}"
        actual (parser/parser input :start :comment)
        parses (insta/parses parser/parser input :start :comment)
        parse-count (count parses)
        expected [:comment [:multiline_comment " bigger comment "]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-comment-multiline-across-multiple-lines
  (let [input "{-
                 bigger comment
               -}"
        actual (parser/parser input :start :comment)
        parses (insta/parses parser/parser input :start :comment)
        parse-count (count parses)
        expected [:comment
                  [:multiline_comment
                   "\n                 bigger comment\n               "]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-doc-multiline
  (let [input "{-| normal comment
-}"
        actual (parser/parser input :start :doc)
        parses (insta/parses parser/parser input :start :doc)
        parse-count (count parses)
        expected [:doc [:doc_part [:text " normal comment"]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-doc-multiline-header
  (let [input "{-|
This module exists mostly to support infinite streams in the `Stream` module.

@docs Source

# Getting things out
@docs next, nextN, peek, current

# Special sources
@docs value, naturalNumbers, fibonocci

# Creating sources
@docs iterate
-}"
        actual (parser/parser input :start :doc)
        parses (insta/parses parser/parser input :start :doc)
        parse-count (count parses)
        expected []]
    (is (= expected actual))
    (is (= parse-count 1))))

#?(:clj (clojure.test/run-tests)
   :cljs (cljs.test/run-tests))
