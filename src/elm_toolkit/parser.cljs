(ns elm-toolkit.parser
  (:require [instaparse.core :as insta]
            [cljs.nodejs :as nodejs]))

(def fs (nodejs/require "fs"))

(def grammar "
    start =
             <ws> module_def <ws> (doc <ws>)? imports <ws> definitions <ws>


(* Rules for the initial module definition *)

    module_def =
             <'module'> <break> namespace? module_name <break> exposing? <nl>?

    exposing =
             <'exposing'> <break> <'('> <break> expose_list <break> <')'>

    namespace =
             (Name <'.'>)+

    fn_name =
             name

    module_name =
             Name


(* Rules for import statements *)

    imports =
             import (<break> import)*

    import =
             <'import'> <break> namespace? module_name <break> exposing? <nl>?

    expose_list =
             expose_all | (type_name | fn_name) (<break> <','> <break> (type_name | fn_name))*

    type_name =
             Name

    expose_all =
             <'..'>


(* Rules for the the bulk of the elm file, defining things *)

    definitions =
             definition (<ws> definition)*

    definition =
             (doc <ws>)? type
             |
             (doc <ws>)? function
             |
             (doc <ws>)? function_annotation

    type =
             <'type'> <break> Name <break> type_parameters* <break> <'='> <break> options <nl>

    options =
             option (<break> <'|'> <break> option)*

    option =
             Name <break> type_parameters*

    type_parameters =
             name (<break> name)*


(* Rules for function annotations *)

    function_annotation =
             name <break> <':'> <break> signature <nl>?

    signature =
             signature_part (<break> <'->'> <break> signature_part)*

    signature_part =
             (Name | name) <break> (type_parameters*)
             |
             <'('> <break> signature <break> <')'>
             | tuple


(* Rules for function definitions *)

    function =
             name <break> function_parameters* <break> <'='> <break> expression <nl>

    function_parameters =
             name (<break> name)*


(* Rules for expressions *)

    expression =
             if
             |
             function_call
             |
             value
             |
             infix
             |
             let
             |
             case
             |
             <'('> expression <')'>

    if =
             <'if'> <break> test <break> <'then'> <break> true_expression <break> <'else'> <break> else_expression (<break> if)*

    test =
             expression

    true_expression =
             expression

    else_expression =
             expression

    function_call =
             namespace? (Name | name) <break> arguments

    arguments =
             argument (<break> argument)*

    argument =
             expression

    value =
             namespace? (Name | name)
             |
             int
             |
             float
             |
             tuple

    infix =
             expression <break> symbol ( (<break> expression) | <break> infix )

    let =
             <'let'> <break> assignment (<break> assignment)* <break> <'in'> <break> in_expression

    in_expression =
             expression

    assignment =
             lvalue <break> <'='> <break> expression <nl>
             |
             function
             |
             function_annotation

    lvalue =
             name
             |
             tuple

    tuple =
             <'('> <break> value <break> (<break> <','> <break> value)* <break> <')'>

    case =
             <'case'> <break> expression <break> <'of'> <break> match (<break> match)*

    match =
             destructure <break> <'->'> <break> expression <nl>

    destructure =
             Name (<break> name)*
             |
             name


(* Rules for comments *)

    doc =
             <'{-|'> <ws> doc_part* <ws> <'-}'>

    doc_part =
             doc_header
             |
             doc_list
             |
             text

    doc_header =
             <'#'> #'.*'

    doc_list =
             <'@docs'> <break> (Name | name) <break> ( <break> ',' <break> (Name | name) )*

    text =
             !('@docs'|'#') #'.*' <nl>


(* Building blocks *)

    Name =
             !(#'\\bif\\b'|#'\\bthen\\b'|#'\\belse\\b'|#'\\bin\\b'|#'\\blet\\b'|'case'|'of') #'[A-Z][a-zA-Z0-9]*'

    name =
             !(#'\\bif\\b'|#'\\bthen\\b'|#'\\belse\\b'|#'\\bin\\b'|#'\\blet\\b'|'case'|'of') #'[a-z][a-zA-Z0-9]*'

    int =
             #'-?[0-9]+'

    float =
             #'-?[0-9]+\\.[0-9]*'

    symbol =
             !(#'\\bif\\b'|#'\\bthen\\b'|#'\\belse\\b'|#'\\bin\\b'|#'\\blet\\b'|'case'|'of') #'[+-/*=.<>:&|^?%#~!]+'

    multiline_comment =
             <break> #'\\{-.*-\\}' <break>

    singleline_comment =
             <break> <'--'> #'.*' <'\\n'>

    nl =
             #'\\n'

    ws =
             #'\\s*'

    break =
             #'[ \\t]*' | #'\\n[ \\t]+' | #'--' #'.*' nl

  ")

(def parser (insta/parser grammar))

(defn parse-file [path]
  (let [content (.readFileSync fs path "UTF-8" )]
    (parser content)))

