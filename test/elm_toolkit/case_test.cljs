(ns elm-toolkit.case-test
  (:require  [elm-toolkit.parser :as parser]
             [cljs.test :refer-macros  [deftest is testing run-tests]]))

(deftest test-case-catchall
  (let [input "case a of
                    _ ->
                        1"
        actual (parser/parser input :start :case)
        expected [:case
                  [:case_on [:expression [:value [:name "a"]]]]
                  [:match
                   [:destructure [:ignore_arg]]
                   [:expression [:value [:int "1"]]]]]]
    (is (= expected actual))))

(deftest test-case-tuple
  (let [input "case a of
                    (1, 2) ->
                        b"
        actual (parser/parser input :start :case)
        expected [:case
                  [:case_on [:expression [:value [:name "a"]]]]
                  [:match
                   [:destructure
                    [:tuple_destructure
                     [:destructure
                      [:value_destructure [:literal [:int "1"]]]]
                     [:destructure
                      [:value_destructure [:literal [:int "2"]]]]]]
                   [:expression [:value [:name "b"]]]]]]
    (is (= expected actual))))

(deftest test-case-alias
  (let [input "case a of
                    _ as b->
                        b"
        actual (parser/parser input :start :case)
        expected [:case
                  [:case_on [:expression [:value [:name "a"]]]]
                  [:match
                   [:destructure [:ignore_arg]]
                   [:match_alias [:name "b"]]
                   [:expression [:value [:name "b"]]]]]]
    (is (= expected actual))))

(deftest test-case-type-destructure
  (let [input "case thing of
                    StringThing string ->
                        Just string
                    IntThing int ->
                        Just int
                    OtherThing x ->
                        Nothing"
        actual (parser/parser input :start :case)
        expected [:case
             [:case_on [:expression [:value [:name "thing"]]]]
             [:match
              [:destructure
               [:type_destructure
                [:Name "StringThing"]
                [:type_destructure_argument
                 [:variable_destructure [:name "string"]]]]]
              [:expression
               [:function_call
                [:Name "Just"]
                [:arguments
                 [:argument
                  [:expression [:value [:name "string"]]]]]]]]
             [:match
              [:destructure
               [:type_destructure
                [:Name "IntThing"]
                [:type_destructure_argument
                 [:variable_destructure [:name "int"]]]]]
              [:expression
               [:function_call
                [:Name "Just"]
                [:arguments
                 [:argument [:expression [:value [:name "int"]]]]]]]]
             [:match
              [:destructure
               [:type_destructure
                [:Name "OtherThing"]
                [:type_destructure_argument
                 [:variable_destructure [:name "x"]]]]]
              [:expression [:value [:Name "Nothing"]]]]]]
    (is (= expected actual))))

(deftest test-case-type-destructure-optional-newlines
  (let [input1 "case thing of
                    StringThing string ->
                        Just string
                    IntThing int ->
                        Just int
                    OtherThing x ->
                        Nothing"
        input2 "case thing of
                    StringThing string ->
                        Just string

                    IntThing int ->
                        Just int

                    OtherThing x ->
                        Nothing"
        parse1 (parser/parser input1 :start :case)
        parse2 (parser/parser input2 :start :case)  ]
    (is (= parse1 parse2))))


(cljs.test/run-tests)
