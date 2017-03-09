(ns elm-toolkit.value_test
  (:require [elm-toolkit.parser :as parser]
            [instaparse.core :as insta]
            #?(:clj [clojure.test :refer [deftest is testing run-tests]]
               :cljs [clojure.test :refer-macros [deftest is testing run-tests]])))

(deftest test-value-Name
  (let [input "Name"
        expected (parser/parser input :start :value)
        parses (insta/parses parser/parser input :start :value)
        parse-count (count parses)
        actual [:value  [:Name "Name"]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-value-name
  (let [input "name"
        expected (parser/parser input :start :value)
        parses (insta/parses parser/parser input :start :value)
        parse-count (count parses)
        actual [:value  [:name "name"]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-value-namespace
  (let [input "Name.With.Namespace"
        expected (parser/parser input :start :value)
        parses (insta/parses parser/parser input :start :value)
        parse-count (count parses)
        actual [:value
                [:namespace  [:Name "Name"]  [:Name "With"]]
                [:Name "Namespace"]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-value-int
  (let [input "22"
        expected (parser/parser input :start :value)
        parses (insta/parses parser/parser input :start :value)
        parse-count (count parses)
        actual [:value  [:int "22"]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-value-int-negative
  (let [input "-22"
        expected (parser/parser input :start :value)
        parses (insta/parses parser/parser input :start :value)
        parse-count (count parses)
        actual [:value  [:int "-22"]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-value-float
  (let [input "22.44"
        expected (parser/parser input :start :value)
        parses (insta/parses parser/parser input :start :value)
        parse-count (count parses)
        actual [:value  [:float "22.44"]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-value-float-negative
  (let [input "-22.44"
        expected (parser/parser input :start :value)
        parses (insta/parses parser/parser input :start :value)
        parse-count (count parses)
        actual [:value  [:float "-22.44"]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-value-list-empty
  (let [input "[]"
        expected (parser/parser input :start :value)
        parses (insta/parses parser/parser input :start :value)
        parse-count (count parses)
        actual [:value [:list]] ]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-value-list-values
  (let [input "[1, 2, 3]"
        expected (parser/parser input :start :value)
        parses (insta/parses parser/parser input :start :value)
        parse-count (count parses)
        actual [:value
                [:list
                 [:list_items
                  [:value [:int "1"]]
                  [:value [:int "2"]] 
                  [:value [:int "3"]]]]] ]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-value-list-variables
  (let [input "[a, b]"
        expected (parser/parser input :start :value)
        parses (insta/parses parser/parser input :start :value)
        parse-count (count parses)
        actual [:value
                [:list
                 [:list_items [:value [:name "a"]] [:value [:name "b"]]]]] ]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-value-list-with-namespace
  (let [input "[ Some.Module.constant ]"
        expected (parser/parser input :start :value)
        parses (insta/parses parser/parser input :start :value)
        parse-count (count parses)
        actual [:value
                [:list
                 [:list_items
                  [:value
                   [:namespace [:Name "Some"] [:Name "Module"]]
                   [:name "constant"]]]]] ]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-value-list-nested
  (let [input "[[1, 2], [3, 4]]"
        expected (parser/parser input :start :value)
        parses (insta/parses parser/parser input :start :value)
        parse-count (count parses)
        actual [:value
                [:list
                 [:list_items
                  [:value
                   [:list
                    [:list_items
                     [:value [:int "1"]]
                     [:value [:int "2"]]]]]
                  [:value
                   [:list
                    [:list_items
                     [:value [:int "3"]]
                     [:value [:int "4"]]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

#?(:clj (clojure.test/run-tests)
   :cljs (cljs.test/run-tests))
