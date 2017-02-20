(ns elm-toolkit.snippet-test
  (:require  [elm-toolkit.parser :as parser]
             [instaparse.core :as insta]
             [cljs.test :refer-macros  [deftest is testing run-tests]]))


(deftest test-definition-parse
  (let [input "factorsOf n =
    Stream.range (Basics.floor ((Basics.toFloat n) / 2)) 2 1
        |> Stream.filter (\\m -> n % m == 0)"
        actual (parser/parser input :start :definition)
        parses (insta/parses parser/parser input :start :definition)
        parse-count (count parses)
        expected [:definition
             [:function
              [:name "factorsOf"]
              [:function_parameters
               [:destructure [:variable_destructure [:name "n"]]]]
              [:function_body
               [:function_or_expression
                [:infix
                 [:infix_argument
                  [:function_call
                   [:function_name
                    [:namespace [:Name "Stream"]]
                    [:name "range"]]
                   [:arguments
                    [:expression
                     [:function_call
                      [:function_name
                       [:namespace [:Name "Basics"]]
                       [:name "floor"]]
                      [:arguments
                       [:expression
                        [:infix
                         [:infix_argument
                          [:expression
                           [:function_call
                            [:function_name
                             [:namespace [:Name "Basics"]]
                             [:name "toFloat"]]
                            [:arguments
                             [:expression [:value [:name "n"]]]]]]]
                         [:symbol "/"]
                         [:infix_argument
                          [:expression [:value [:int "2"]]]]]]]]]
                    [:expression [:value [:int "2"]]]
                    [:expression [:value [:int "1"]]]]]]
                 [:symbol "|>"]
                 [:infix_argument
                  [:function_call
                   [:function_name
                    [:namespace [:Name "Stream"]]
                    [:name "filter"]]
                   [:arguments
                    [:expression
                     [:lambda
                      [:destructure
                       [:variable_destructure [:name "m"]]]
                      [:lambda_body
                       [:function_or_expression
                        [:infix
                         [:infix_argument
                          [:expression [:value [:name "n"]]]]
                         [:symbol "%"]
                         [:infix_argument
                          [:expression [:value [:name "m"]]]]
                         [:symbol "=="]
                         [:infix_argument
                          [:expression
                           [:value [:int "0"]]]]]]]]]]]]]]]]]]
    (is (= expected actual))
    (is (= parse-count 1))))

(cljs.test/run-tests)

