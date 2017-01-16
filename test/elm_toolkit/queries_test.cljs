(ns elm-toolkit.queries-test
  (:require [elm-toolkit.queries :as q]
            [elm-toolkit.parser :as parser]
            [cljs.test :refer-macros  [deftest is testing run-tests]]
            [clojure.test :as t]))

(def parse-tree (parser/parse-file "./input/Simple/Module.elm"))

(deftest test-imports-query
  (let [imports (q/imports parse-tree)
        expected [[:import [:module_name [:Name "List"]]]
                  [:import [:module_name [:Name "String"]]]
                  [:import [:module_name [:Name "Array"]]]]]
    (is (= imports expected))))

(deftest test-imports-swap
  (let [imports (q/imports parse-tree)
        new-tree (q/swap-imports imports parse-tree)]
    (is (= new-tree parse-tree))))


(deftest test-module-def-query
  (let [module-def (q/module-def parse-tree)
        expected [[:module_def
                   [:namespace [:Name "Simple"]]
                   [:module_name [:Name "Module"]]
                   [:exposing [:expose_list [:expose_all]]]]] ]
    (is (= module-def expected))))

(deftest test-module-def-swap
  (let [module-def (q/module-def parse-tree)
        new-tree (q/swap-module-def module-def parse-tree)]
    (is (= new-tree parse-tree))))

(deftest test-functions-query
  (let [functions (q/functions parse-tree)
        expected [[:function
                   [:name "myFunction"]
                   [:function_parameters [:name "m"]]
                   [:expression [:value [:name "m"]]]]
                  [:function
                   [:name "otherFunction"]
                   [:function_parameters [:name "a"] [:name "b"]]
                   [:expression
                    [:infix
                     [:expression [:value [:name "a"]]]
                     [:symbol "+"]
                     [:expression [:value [:name "b"]]]]]]] ]
    (is (= functions expected))))

(deftest test-definitions-query
  (let [definitions (q/definitions parse-tree)
        expected [[:definition
                   [:type [:Name "MyType"]
                    [:options
                     [:option [:Name "A"]]
                     [:option [:Name "B"]]]]]
                  [:definition
                   [:type [:Name "Fish"]
                    [:options
                     [:option [:Name "Sticks"]]
                     [:option [:Name "SomethingElse"]]]]]
                  [:definition
                   [:annotation [:name "myFunction"]
                    [:signature
                     [:signature_part [:Name "MyType"]]
                     [:signature_part [:Name "MyType"]]]]]
                  [:definition
                   [:function [:name "myFunction"]
                    [:function_parameters [:name "m"]]
                    [:expression [:value [:name "m"]]]]]
                  [:definition
                   [:annotation [:name "otherFunction"]
                    [:signature
                     [:signature_part [:Name "Int"]]
                     [:signature_part [:Name "Int"]]
                     [:signature_part [:Name "Int"]]]]]
                  [:definition
                   [:function [:name "otherFunction"]
                    [:function_parameters [:name "a"] [:name "b"]]
                    [:expression
                     [:infix
                      [:expression [:value [:name "a"]]]
                      [:symbol "+"]
                      [:expression [:value [:name "b"]]]]]]]] ]
    (is (= definitions expected))))

(deftest test-definition-name
  (let [definitions (q/definitions parse-tree)
        actual (map #(q/definition-name %) definitions)
        expected ["MyType" "Fish" "myFunction" "myFunction" "otherFunction" "otherFunction"] ]
    (is (= actual expected))))

(deftest test-references?-no-namespace
  (let [definition (parser/parser "myFunction a = List.empty" :start :definition)
        name "myFunction"
        refers (q/references? name definition)]
    (is refers)))

(cljs.test/run-tests)
