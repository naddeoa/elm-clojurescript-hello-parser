(ns elm-toolkit.comment-test
  (:require [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-comment-singleline
  (let [input "-- normal comment"
        actual (parser/parser input :start :comment)
        expected [:comment [:singleline_comment " normal comment"]]]
    (is (= expected actual))))

(deftest test-comment-singleline-space-in-front
  (let [input "    -- normal comment"
        actual (parser/parser input :start :comment)
        expected [:comment [:singleline_comment " normal comment"]]]
    (is (= expected actual))))

(deftest test-comment-multiline
  (let [input "{- bigger comment -}"
        actual (parser/parser input :start :comment)
        expected [:comment [:multiline_comment " bigger comment "]]]
    (is (= expected actual))))

(deftest test-comment-multiline-across-multiple-lines
  (let [input "{-
                 bigger comment
               -}"
        actual (parser/parser input :start :comment)
        expected [:comment
                  [:multiline_comment
                   "\n                 bigger comment\n               "]]]
    (is (= expected actual))))

(cljs.test/run-tests)
