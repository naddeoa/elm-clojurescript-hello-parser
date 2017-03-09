(ns elm-toolkit.annotation-test
  (:require [elm-toolkit.parser :as parser]
            [instaparse.core :as insta]
            #?(:clj [clojure.test :refer [deftest is testing run-tests]]
               :cljs [clojure.test :refer-macros [deftest is testing run-tests]])))

(def parse-count-message  "Number of possible parses is too high; the grammar is too ambiguous")

(deftest test-annotation
  (let [input "add: Int -> Int -> Int"
        expected (parser/parser input :start :annotation)
        parses (insta/parses parser/parser input)
        parse-count (count parses)
        actual [:annotation
                [:name "add"]
                [:signature
                 [:function_destructure
                  [:function_destructure_argument
                   [:destructure [:type_destructure [:Name "Int"]]]]
                  [:function_destructure_argument
                   [:destructure [:type_destructure [:Name "Int"]]]]
                  [:function_destructure_argument
                   [:destructure [:type_destructure [:Name "Int"]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1) parse-count-message)))

(deftest test-annotation-nested
  (let [input "map: (a -> b) -> List a -> List b"
        expected (parser/parser input :start :annotation)
        parses (insta/parses parser/parser input)
        parse-count (count parses)
        actual [:annotation
             [:name "map"]
             [:signature
              [:function_destructure
               [:function_destructure_argument
                [:destructure
                 [:function_destructure
                  [:function_destructure_argument
                   [:destructure [:variable_destructure [:name "a"]]]]
                  [:function_destructure_argument
                   [:destructure
                    [:variable_destructure [:name "b"]]]]]]]
               [:function_destructure_argument
                [:destructure
                 [:type_destructure
                  [:Name "List"]
                  [:type_destructure_argument
                   [:destructure
                    [:variable_destructure [:name "a"]]]]]]]
               [:function_destructure_argument
                [:destructure
                 [:type_destructure
                  [:Name "List"]
                  [:type_destructure_argument
                   [:destructure
                    [:variable_destructure [:name "b"]]]]]]]]]] ]
    (is (= expected actual))
    (is (= parse-count 1) parse-count-message)))

(deftest test-annotation-single
  (let [input "thing : String"
        expected (parser/parser input :start :annotation)
        parses (insta/parses parser/parser input)
        parse-count (count parses)
        actual [:annotation
                [:name "thing"]
                [:signature
                 [:destructure [:type_destructure [:Name "String"]]]]]]
    (is (= expected actual))
    (is (= parse-count 1) parse-count-message)))

(deftest test-annotation-tuple
  (let [input "zip : a -> b -> (a, b)"
        expected (parser/parser input :start :annotation)
        parses (insta/parses parser/parser input)
        parse-count (count parses)
        actual [:annotation
                [:name "zip"]
                [:signature
                 [:function_destructure
                  [:function_destructure_argument
                   [:destructure [:variable_destructure [:name "a"]]]]
                  [:function_destructure_argument
                   [:destructure [:variable_destructure [:name "b"]]]]
                  [:function_destructure_argument
                   [:destructure
                    [:tuple_destructure
                     [:destructure [:variable_destructure [:name "a"]]]
                     [:destructure
                      [:variable_destructure [:name "b"]]]]]]]]] ]
    (is (= expected actual))
    (is (= parse-count 1) parse-count-message)))

(deftest test-annotation-nested-types
  (let [input "add: Int -> List (List Int)"
        expected (parser/parser input :start :annotation)
        parses (insta/parses parser/parser input)
        parse-count (count parses)
        actual [:annotation
                [:name "add"]
                [:signature
                 [:function_destructure
                  [:function_destructure_argument
                   [:destructure [:type_destructure [:Name "Int"]]]]
                  [:function_destructure_argument
                   [:destructure
                    [:type_destructure
                     [:Name "List"]
                     [:type_destructure_argument
                      [:destructure
                       [:destructure
                        [:type_destructure
                         [:Name "List"]
                         [:type_destructure_argument
                          [:destructure
                           [:type_destructure [:Name "Int"]]]]]]]]]]]]]] ]
    (is (= expected actual))
    (is (= parse-count 1) parse-count-message)))

#?(:clj (clojure.test/run-tests)
   :cljs (cljs.test/run-tests))
