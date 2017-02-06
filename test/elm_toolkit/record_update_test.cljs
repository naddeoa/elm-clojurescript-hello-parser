(ns elm-toolkit.record-update-test
  (:require [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-record-update
  (let [input "{ model | field = Nothing }"
        actual (parser/parser input :start :expression)
        expected [:expression
                  [:record_update
                   [:record_update_target [:name "model"]]
                   [:record_items
                    [:record_item
                     [:name "field"]
                     [:expression [:value [:Name "Nothing"]]]]]]]]
    (is (= expected actual))))

(deftest test-record-update-multiple
  (let [input "{ model | field = Nothing, otherField = \"something\" }"
        actual (parser/parser input :start :expression)
        expected [:expression
                  [:record_update
                   [:record_update_target [:name "model"]]
                   [:record_items
                    [:record_item
                     [:name "field"]
                     [:expression [:value [:Name "Nothing"]]]]
                    [:record_item
                     [:name "otherField"]
                     [:expression [:value [:string "something"]]]]]]]]
    (is (= expected actual))))

(cljs.test/run-tests)
