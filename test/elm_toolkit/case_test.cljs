(ns elm-toolkit.case-test
  (:require  [elm-toolkit.parser :as parser]
             [instaparse.core :as insta]
             [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-case-catchall
  (let [input "case a of
                    _ ->
                        1"
        actual (parser/parser input :start :case)
        parses (insta/parses parser/parser input :start :case)
        parse-count (count parses)
        expected [:case
                  [:case_on
                   [:function_or_expression
                    [:expression [:value [:name "a"]]]]]
                  [:match
                   [:match_assignment [:destructure [:ignore_arg]]]
                   [:match_expression
                    [:function_or_expression
                     [:expression [:value [:int "1"]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-case-tuple
  (let [input "case a of
                    (1, 2) ->
                        b"
        actual (parser/parser input :start :case)
        parses (insta/parses parser/parser input :start :case)
        parse-count (count parses)
        expected [:case
                  [:case_on
                   [:function_or_expression
                    [:expression [:value [:name "a"]]]]]
                  [:match
                   [:match_assignment
                    [:destructure
                     [:tuple_destructure
                      [:destructure
                       [:value_destructure [:literal [:int "1"]]]]
                      [:destructure
                       [:value_destructure [:literal [:int "2"]]]]]]]
                   [:match_expression
                    [:function_or_expression
                     [:expression [:value [:name "b"]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-case-alias
  (let [input "case a of
                    _ as b->
                        b"
        actual (parser/parser input :start :case)
        parses (insta/parses parser/parser input :start :case)
        parse-count (count parses)
        expected [:case
                  [:case_on
                   [:function_or_expression
                    [:expression [:value [:name "a"]]]]]
                  [:match
                   [:match_assignment [:destructure [:ignore_arg]]]
                   [:match_alias [:name "b"]]
                   [:match_expression
                    [:function_or_expression
                     [:expression [:value [:name "b"]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-case-type-destructure
  (let [input "case thing of
                    StringThing string ->
                        Just string

                    IntThing int ->
                        Just int

                    OtherThing x ->
                        Nothing"
        actual (parser/parser input :start :case)
        parses (insta/parses parser/parser input :start :case)
        parse-count (count parses)
        expected [:case
             [:case_on
              [:function_or_expression
               [:expression [:value [:name "thing"]]]]]
             [:match
              [:match_assignment
               [:destructure
                [:type_destructure
                 [:Name "StringThing"]
                 [:type_destructure_argument
                  [:destructure
                   [:variable_destructure [:name "string"]]]]]]]
              [:match_expression
               [:function_or_expression
                [:function_call
                 [:function_name [:Name "Just"]]
                 [:arguments
                  [:expression [:value [:name "string"]]]]]]]]
             [:match
              [:match_assignment
               [:destructure
                [:type_destructure
                 [:Name "IntThing"]
                 [:type_destructure_argument
                  [:destructure
                   [:variable_destructure [:name "int"]]]]]]]
              [:match_expression
               [:function_or_expression
                [:function_call
                 [:function_name [:Name "Just"]]
                 [:arguments [:expression [:value [:name "int"]]]]]]]]
             [:match
              [:match_assignment
               [:destructure
                [:type_destructure
                 [:Name "OtherThing"]
                 [:type_destructure_argument
                  [:destructure
                   [:variable_destructure [:name "x"]]]]]]]
              [:match_expression
               [:function_or_expression
                [:expression [:value [:Name "Nothing"]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))


(cljs.test/run-tests)
