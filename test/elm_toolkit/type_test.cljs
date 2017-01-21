(ns elm-toolkit.type-test
  (:require [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]))


(deftest test-definition-type-nested
  (let [input "type Fish a = Cod (List a)"
        actual (parser/parser input :start :definition)
        expected [:definition
                  [:type [:Name "Fish"]
                   [:type_parameters [:name "a"]]
                   [:options
                    [:option [:Name "Cod"]
                     [:destructure
                      [:destructure
                       [:type_destructure
                        [:Name "List"]
                        [:destructure
                         [:variable_destructure
                          [:name "a"]]]]]]]]]]]
    (is (= expected actual))))

(deftest test-definition-type-tuple
  (let [input "type Fish a = Cod (a, List b)"
        actual (parser/parser input :start :definition)
        expected  [:definition
                   [:type [:Name "Fish"]
                    [:type_parameters [:name "a"]]
                    [:options
                     [:option [:Name "Cod"]
                      [:destructure
                       [:tuple_destructure
                        [:destructure
                         [:variable_destructure [:name "a"]]]
                        [:destructure
                         [:type_destructure [:Name "List"]
                          [:destructure [:variable_destructure [:name "b"]]]]]]]]]]]]
    (is (= expected actual))))

(deftest test-definition-type-multiple
  (let [input "type Fish a
                      = Salmon
                      | Cod"
        actual (parser/parser input :start :definition)
        expected  [:definition
                   [:type [:Name "Fish"]
                    [:type_parameters [:name "a"]]
                    [:options
                     [:option [:Name "Salmon"]]
                     [:option [:Name "Cod"]]]]]]
    (is (= expected actual))))


(deftest test-definition-type-no-generics
  (let [input "type Fish = Cod (List Some.Module.Type)"
        actual (parser/parser input :start :definition)
        expected  [:definition
                   [:type [:Name "Fish"]
                    [:options
                     [:option
                      [:Name "Cod"]
                      [:destructure
                       [:destructure
                        [:type_destructure [:Name "List"]
                         [:destructure
                          [:type_destructure
                           [:namespace
                            [:Name "Some"]
                            [:Name "Module"]]
                           [:Name "Type"]]]]]]]]]]]
    (is (= expected actual))))

(cljs.test/run-tests)
