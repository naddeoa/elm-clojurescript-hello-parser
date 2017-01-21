(ns elm-toolkit.parser
  (:require [instaparse.core :as insta]
            [cljs.nodejs :as nodejs]))

(def fs (nodejs/require "fs"))

(def grammar "
    start =
             <ws> module_def <ws> (doc <ws>)? imports? <ws> definitions <ws>


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

    identifier =
             namespace? (Name | name)

(* Rules for import statements *)

    imports =
             import (<break> import)*

    import =
             <'import'> <break> namespace? module_name (<break> import_as)? <break> exposing? <nl>?

    import_as =
             <'as'> <break> Name

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
             (doc <ws>)? annotation
             |
             (doc <ws>)? value_definition

    value_definition =
             name <break> <'='> <break> expression <nl>?

    type =
             <'type'> <break> Name <break> type_parameters* <break> <'='> <break> options <nl>?

    options =
             option (<break> <'|'> <break> option)*

    option =
             Name (<break> destructure)*

    type_parameters =
             name (<break> name)*
             |
             namespace? Name
             |
             <'('> type_parameters <')'>


(* Rules for function annotations *)

    annotation =
             name <break> <':'> <break> signature <nl>?

    signature =
             signature_part (<break> <'->'> <break> signature_part)*

    signature_part =
             destructure
             |
             <'('> <break> signature <break> <')'>


(* Rules for function definitions *)

    function =
             name <break> function_parameters <break> <'='> <break> expression <nl>?

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
             lambda
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
             destructure <break> <'='> <break> expression <nl>
             |
             function
             |
             annotation

    tuple =
             <'('> <break> (value | ignore_arg) <break> (<break> <','> <break> (value | ignore_arg))* <break> <')'>

    case =
             <'case'> <break> expression <break> <'of'> <break> match (<break> match)*

    match =
             destructure <break> <'->'> <break> expression <nl>

    destructure =
             type_destructure
             |
             variable_destructure
             |
             tuple_destructure
             |
             ignore_arg
             |
             <'('> <break> destructure <break> <')'>

    type_destructure =
             namespace? Name (<break> destructure)*

    variable_destructure =
             name

    tuple_destructure =
             <'('> <break> destructure (<break> <','> <break> destructure)+ <break> <')'>

    lambda =
             <'('> <'\\\\'> <break> destructure <break> <'->'> <break> expression <break> <')'>

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

    ignore_arg =
             <'_'>
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
    (parser content :partial true :total true)))

