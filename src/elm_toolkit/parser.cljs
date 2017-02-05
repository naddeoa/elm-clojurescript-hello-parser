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
             <'type'> <break> Name <break> type_parameters? <break> <'='> <break> options <nl>?

    options =
             option (<break> <'|'> <break> option)*

    option =
             Name (<break> option_parameter)*

    option_parameter =
             destructure

    type_parameters =
             type_parameter+

    type_parameter =
             name

(* Rules for function annotations *)

    annotation =
             name <break> <':'> <break> signature <nl>?

    signature =
             destructure

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
             namespace? (Name | name) <break> arguments <nl>?

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
             |
             list

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

    list =
             <'['> <break> list_items? <break> <']'>

    list_items =
             value (<break> <','> <break> value)*

    tuple =
             <'('> <break> expression <break> (<break> <','> <break> expression)* <break> <')'>

    case =
             <'case'> <break> case_on <break> <'of'> <break> match (<break> match)* 

    case_on =
             expression

    match =
             destructure <break> (match_alias <break>)? <'->'> <break> expression <nl>?

    match_alias =
             <'as'> <break> name

    destructure =
             type_destructure
             |
             function_destructure
             |
             tuple_destructure
             |
             ignore_arg
             |
             variable_destructure
             |
             singleline_comment
             |
             list_destructure
             |
             value_destructure
             |
             <'('> <break> destructure <break> <')'>

    list_destructure =
             <'['> <break> list_destructure_items? <break> <']'>

    list_destructure_items =
             destructure (<break> <','> <break> destructure)*

    function_destructure =
             destructure (<break> <'->'> <break> destructure)+

    type_destructure =
             namespace? Name (<break> type_destructure_argument)*

    type_destructure_argument =
             variable_destructure / tuple_destructure / destructure

    variable_destructure =
             name

    value_destructure =
             literal

    tuple_destructure =
             <'('> <break> destructure (<break> <','> <break> destructure)+ <break> <')'>

    lambda =
             <'('> <'\\\\'> <break> destructure <break> <'->'> <break> expression <break> <')'>

(* Rules for comments *)

    doc =
             <'{-|'> <ws> doc_part* <ws> '-}'

    doc_part =
             doc_header
             |
             doc_list
             |
             text

    doc_header =
             <'#'> #'.*'

    doc_list =
             <'@docs'> <break> (Name | name) <break> ( <break> <','> <break> (Name | name) )*

    text =
             !('@docs'|'#'|'-}') #'.*' <nl>?


(* Building blocks *)

    Name =
             !(#'\\bif\\b'|#'\\bthen\\b'|#'\\belse\\b'|#'\\bin\\b'|#'\\blet\\b'|'case'|'of') #'[A-Z][a-zA-Z0-9]*'

    name =
             !(#'\\bif\\b'|#'\\bthen\\b'|#'\\belse\\b'|#'\\bin\\b'|#'\\blet\\b'|'case'|'of') #'[a-z][a-zA-Z0-9]*'

    int =
             #'-?[0-9]+'

    float =
             #'-?[0-9]+\\.[0-9]*'

    string =
             <'\"'>  #'[^\"]*'  <'\"'>

    literal =
             string
             |
             float
             |
             int

    ignore_arg =
             <'_'>
    symbol =
             !(#'\\bif\\b'|#'\\bthen\\b'|#'\\belse\\b'|#'\\bin\\b'|#'\\blet\\b'|'case'|'of') #'[+-/*=.<>:&|^?%#~!]+'

    comment =
             singleline_comment | multiline_comment

    multiline_comment =
             <'{-'> (#'[^}-]*' <nl>?)* <'-}'>

    singleline_comment =
             <break> <'--'> #'.*'

    nl =
             #'\\n'

    ws =
             #'\\s*'

    break =
             #'[ \\t]*' | #'\\n[ \\t]+' | #'--' #'.*' nl

  ")

(def parser (insta/parser grammar))

(defn parses [input]
  (insta/parses parser input))

(defn debug [path]
  (let [content (.readFileSync fs path "UTF-8" )]
    (insta/parses parser content)))

(defn parse-file [path]
  (let [content (.readFileSync fs path "UTF-8" )]
    (parser content )))

