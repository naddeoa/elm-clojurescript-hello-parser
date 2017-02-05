(ns elm-toolkit.value_test
  (:require [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-value-Name
  (let [input "Name"
        expected (parser/parser input :start :value)
        actual [:value  [:Name "Name"]]]
    (is (= expected actual))))

(deftest test-value-name
  (let [input "name"
        expected (parser/parser input :start :value)
        actual [:value  [:name "name"]]]
    (is (= expected actual))))

(deftest test-value-namespace
  (let [input "Name.With.Namespace"
        expected (parser/parser input :start :value)
        actual [:value
                [:namespace  [:Name "Name"]  [:Name "With"]]
                [:Name "Namespace"]]]
    (is (= expected actual))))

(deftest test-value-int
  (let [input "22"
        expected (parser/parser input :start :value)
        actual [:value  [:int "22"]]]
    (is (= expected actual))))

(deftest test-value-int-negative
  (let [input "-22"
        expected (parser/parser input :start :value)
        actual [:value  [:int "-22"]]]
    (is (= expected actual))))

(deftest test-value-float
  (let [input "22.44"
        expected (parser/parser input :start :value)
        actual [:value  [:float "22.44"]]]
    (is (= expected actual))))

(deftest test-value-float-negative
  (let [input "-22.44"
        expected (parser/parser input :start :value)
        actual [:value  [:float "-22.44"]]]
    (is (= expected actual))))

(deftest test-value-list-empty
  (let [input "[]"
        expected (parser/parser input :start :value)
        actual [:value [:list]] ]
    (is (= expected actual))))

(deftest test-value-list-values
  (let [input "[1, 2, 3]"
        expected (parser/parser input :start :value)
        actual [:value
                [:list
                 [:list_items
                  [:value [:int "1"]]
                  [:value [:int "2"]] 
                  [:value [:int "3"]]]]] ]
    (is (= expected actual))))

(deftest test-value-list-variables
  (let [input "[a, b]"
        expected (parser/parser input :start :value)
        actual [:value
                [:list
                 [:list_items [:value [:name "a"]] [:value [:name "b"]]]]] ]
    (is (= expected actual))))

(deftest test-value-list-with-namespace
  (let [input "[ Some.Module.constant ]"
        expected (parser/parser input :start :value)
        actual [:value
                [:list
                 [:list_items
                  [:value
                   [:namespace [:Name "Some"] [:Name "Module"]]
                   [:name "constant"]]]]] ]
    (is (= expected actual))))

(cljs.test/run-tests)
