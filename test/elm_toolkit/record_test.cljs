(ns elm-toolkit.record-test
  (:require [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-record-empty
  (let [input "{}"
        actual (parser/parser input :start :value)
        expected [:value [:record]]]
    (is (= expected actual))))

(deftest test-record-simple
  (let [input "{a = 1}"
        actual (parser/parser input :start :value)
        expected [:value
                  [:record
                   [:record_items
                    [:record_item
                     [:name "a"] 
                     [:expression [:value [:int "1"]]]]]]]]
    (is (= expected actual))))

(deftest test-record-multiple
  (let [input "{ a = 1
               , b = var
               , c = Some.Module.constant
               , d = \"string\"}"
        actual (parser/parser input :start :value)
        expected [:value
                  [:record
                   [:record_items
                    [:record_item
                     [:name "a"]
                     [:expression [:value [:int "1"]]]]
                    [:record_item
                     [:name "b"]
                     [:expression [:value [:name "var"]]]]
                    [:record_item
                     [:name "c"]
                     [:expression
                      [:value
                       [:namespace [:Name "Some"] [:Name "Module"]]
                       [:name "constant"]]]]
                    [:record_item
                     [:name "d"]
                     [:expression [:value [:string "string"]]]]]]]]
    (is (= expected actual))))

(cljs.test/run-tests)
