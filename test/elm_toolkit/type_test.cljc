(ns elm-toolkit.type-test
  (:require [elm-toolkit.parser :as parser]
            [instaparse.core :as insta]
            #?(:clj [clojure.test :refer [deftest is testing run-tests]]
               :cljs [clojure.test :refer-macros [deftest is testing run-tests]])))

(deftest test-definition-type-nested
  (let [input "type Fish a = Cod (List a)"
        actual (parser/parser input :start :definition)
        parses (insta/parses parser/parser input :start :definition)
        parse-count (count parses)
        expected [:definition
                  [:type
                   [:Name "Fish"]
                   [:type_parameters [:type_parameter [:name "a"]]]
                   [:options
                    [:option
                     [:Name "Cod"]
                     [:option_parameter
                      [:destructure
                       [:destructure
                        [:type_destructure
                         [:Name "List"]
                         [:type_destructure_argument
                          [:destructure
                           [:variable_destructure [:name "a"]]]]]]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-definition-type-tuple
  (let [input "type Fish a = Cod (a, List b)"
        actual (parser/parser input :start :definition)
        parses (insta/parses parser/parser input :start :definition)
        parse-count (count parses)
        expected [:definition
                  [:type
                   [:Name "Fish"]
                   [:type_parameters [:type_parameter [:name "a"]]]
                   [:options
                    [:option
                     [:Name "Cod"]
                     [:option_parameter
                      [:destructure
                       [:tuple_destructure
                        [:destructure [:variable_destructure [:name "a"]]]
                        [:destructure
                         [:type_destructure
                          [:Name "List"]
                          [:type_destructure_argument
                           [:destructure
                            [:variable_destructure [:name "b"]]]]]]]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-definition-type-multiple
  (let [input "type Fish a
                      = Salmon
                      | Cod"
        actual (parser/parser input :start :definition)
        parses (insta/parses parser/parser input :start :definition)
        parse-count (count parses)
        expected  [:definition
                   [:type [:Name "Fish"]
                    [:type_parameters
                     [:type_parameter [:name "a"]]]
                    [:options [:option [:Name "Salmon"]]
                     [:option [:Name "Cod"]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-definition-type-function-parameter
  (let [input "type A a = A (a -> a)"
        actual (parser/parser input :start :definition)
        parses (insta/parses parser/parser input :start :definition)
        parse-count (count parses)
        expected  [:definition
                   [:type
                    [:Name "A"]
                    [:type_parameters [:type_parameter [:name "a"]]]
                    [:options
                     [:option
                      [:Name "A"]
                      [:option_parameter
                       [:destructure
                        [:function_destructure
                         [:function_destructure_argument
                          [:destructure [:variable_destructure [:name "a"]]]]
                         [:function_destructure_argument
                          [:destructure
                           [:variable_destructure [:name "a"]]]]]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-definition-type-no-generics
  (let [input "type Fish = Cod (List Some.Module.Type)"
        actual (parser/parser input :start :definition)
        parses (insta/parses parser/parser input :start :definition)
        parse-count (count parses)
        expected [:definition
                  [:type
                   [:Name "Fish"]
                   [:options
                    [:option
                     [:Name "Cod"]
                     [:option_parameter
                      [:destructure
                       [:destructure
                        [:type_destructure
                         [:Name "List"]
                         [:type_destructure_argument
                          [:destructure
                           [:type_destructure
                            [:namespace [:Name "Some"] [:Name "Module"]]
                            [:Name "Type"]]]]]]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-definition-type-parens
  (let [input "type A a = A (a)"
        actual (parser/parser input :start :definition)
        parses (insta/parses parser/parser input :start :definition)
        parse-count (count parses)
        expected [:definition
                  [:type [:Name "A"]
                   [:type_parameters
                    [:type_parameter [:name "a"]]]
                   [:options
                    [:option [:Name "A"]
                     [:option_parameter
                      [:destructure
                       [:destructure
                        [:variable_destructure [:name "a"]]]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

#?(:clj (clojure.test/run-tests)
   :cljs (cljs.test/run-tests))
