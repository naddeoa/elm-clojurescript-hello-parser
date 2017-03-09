(ns elm-toolkit.tuple-test
  (:require  [elm-toolkit.parser :as parser]
             [instaparse.core :as insta]
             #?(:clj [clojure.test :refer [deftest is testing run-tests]]
                :cljs [clojure.test :refer-macros [deftest is testing run-tests]])))

(deftest test-tuple
  (let [input "(a , b)"
        actual (parser/parser input :start :tuple)
        parses (insta/parses parser/parser input :start :tuple)
        parse-count (count parses)
        expected [:tuple
                  [:function_or_expression [:expression [:value [:name "a"]]]]
                  [:function_or_expression [:expression [:value [:name "b"]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-tuple-function
  (let [input "(fn 1 , b)"
        actual (parser/parser input :start :tuple)
        parses (insta/parses parser/parser input :start :tuple)
        parse-count (count parses)
        expected [:tuple
                  [:function_or_expression
                   [:function_call
                    [:function_name [:name "fn"]]
                    [:arguments [:expression [:value [:int "1"]]]]]]
                  [:function_or_expression [:expression [:value [:name "b"]]]]] ]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-tuple-nested
  (let [input "(1, (2,3))"
        actual (parser/parser input :start :tuple)
        parses (insta/parses parser/parser input :start :tuple)
        parse-count (count parses)
        expected [:tuple
                  [:function_or_expression
                   [:expression [:value [:int "1"]]]]
                  [:function_or_expression
                   [:expression
                    [:value
                     [:tuple
                      [:function_or_expression
                       [:expression [:value [:int "2"]]]]
                      [:function_or_expression
                       [:expression [:value [:int "3"]]]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-value-tuple
  (let [input "(1,2,3)"
        expected (parser/parser input :start :tuple)
        parses (insta/parses parser/parser input :start :tuple)
        parse-count (count parses)
        actual [:tuple
                [:function_or_expression [:expression [:value [:int "1"]]]]
                [:function_or_expression [:expression [:value [:int "2"]]]]
                [:function_or_expression [:expression [:value [:int "3"]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

#?(:clj (clojure.test/run-tests)
   :cljs (cljs.test/run-tests))
