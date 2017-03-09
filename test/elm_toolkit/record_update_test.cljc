(ns elm-toolkit.record-update-test
  (:require [elm-toolkit.parser :as parser]
            [instaparse.core :as insta]
            #?(:clj [clojure.test :refer [deftest is testing run-tests]]
               :cljs [clojure.test :refer-macros [deftest is testing run-tests]])))

(deftest test-record-update
  (let [input "{ model | field = Nothing }"
        actual (parser/parser input :start :expression)
        parses (insta/parses parser/parser input :start :expression)
        parse-count (count parses)
        expected [:expression
                  [:record_update
                   [:record_update_target [:name "model"]]
                   [:record_items
                    [:record_item
                     [:name "field"]
                     [:expression [:value [:Name "Nothing"]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-record-update-multiple
  (let [input "{ model | field = Nothing, otherField = \"something\" }"
        actual (parser/parser input :start :expression)
        parses (insta/parses parser/parser input :start :expression)
        parse-count (count parses)
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
    (is (= expected actual))
    (is (= parse-count 1))))

#?(:clj (clojure.test/run-tests)
   :cljs (cljs.test/run-tests))
