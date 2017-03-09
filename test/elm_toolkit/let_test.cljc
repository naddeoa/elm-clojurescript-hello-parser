(ns elm-toolkit.let-test
  (:require [elm-toolkit.parser :as parser]
            [instaparse.core :as insta]
            #?(:clj [clojure.test :refer [deftest is testing run-tests]]
               :cljs [clojure.test :refer-macros [deftest is testing run-tests]])))

(deftest test-let
  (let [input "let
                   x = 1
               in
                   x"
        actual (parser/parser input :start :let)
        parses (insta/parses parser/parser input :start :let)
        parse-count (count parses)
        expected [:let
                  [:assignment
                   [:destructure [:variable_destructure [:name "x"]]]
                   [:expression [:value [:int "1"]]]]
                  [:in_expression
                   [:expression [:value [:name "x"]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(deftest test-let-tuple
  (let [input "let
                   (a,b) = someTuple
               in
                   a"
        actual (parser/parser input :start :let)
        parses (insta/parses parser/parser input :start :let)
        parse-count (count parses)
        expected [:let
                  [:assignment
                   [:destructure
                    [:tuple_destructure
                     [:destructure [:variable_destructure [:name "a"]]]
                     [:destructure [:variable_destructure [:name "b"]]]]]
                   [:expression
                    [:value [:name "someTuple"]]]]
                  [:in_expression
                   [:expression [:value [:name "a"]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))


(deftest test-let-type-destructure
  (let [input "let
                   Msg string = msg
               in
                   string"
        actual (parser/parser input :start :let)
        parses (insta/parses parser/parser input :start :let)
        parse-count (count parses)
        expected [:let
                  [:assignment
                   [:destructure
                    [:type_destructure
                     [:Name "Msg"]
                     [:type_destructure_argument
                      [:destructure
                       [:variable_destructure [:name "string"]]]]]]
                   [:expression [:value [:name "msg"]]]]
                  [:in_expression [:expression [:value [:name "string"]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

#?(:clj (clojure.test/run-tests)
   :cljs (cljs.test/run-tests))
