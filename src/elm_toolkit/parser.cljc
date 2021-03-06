(ns elm-toolkit.parser
  (:require [instaparse.core :as insta]))


(def grammar "
    start =
             (<ws> block <ws>)+

    block =
             module_def
             |
             doc
             |
             import
             |
             definition

(* Rules for the initial module definition *)

    module_def =
             <'module'> <break> namespace? module_name <break> exposing?

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

    definition =
             type
             |
             type_alias
             |
             function
             |
             annotation
             |
             value_definition

    value_definition =
             name <break> <'='> <break> expression <nl>?

    type_alias =
             <'type'> <break> <'alias'> <break> Name <break> type_parameters? <break> <'='> <break> destructure <nl>?

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
             name <break> <':'> <break> signature

    signature =
             function_destructure
             |
             destructure

(* Rules for function definitions *)

    function =
             name <break> function_parameters <break> <'='> <break> function_body

    function_body =
             function_or_expression

    function_parameters =
             destructure (<break> destructure)*


(* Rules for expressions *)

    expression =
             if
             |
             value
             |
             let
             |
             case
             |
             lambda
             |
             record_update
             |
             <'('> <break> infix <break> <')'>
             |
             <'('> <break> function_call <break> <')'>
             |
             <'('> <break> expression <break> <')'>

    function_or_expression =
             expression
             |
             function_call
             |
             infix

    if =
             <'if'> <break> test <break> <'then'> <break> true_expression <break> <'else'> <break> else_expression (<break> if)*

    test =
             function_or_expression

    true_expression =
             function_or_expression

    else_expression =
             function_or_expression

    function_call =
             function_name <break> arguments

    function_name =
             namespace? (Name | name)

    arguments =
             expression (<break> expression)*

    value =
             namespace? (Name | name)
             |
             int
             |
             float
             |
             string
             |
             tuple
             |
             record
             |
             list

    record_update =
             <'{'> <break> record_update_target <break> <'|'> <break> record_items <break> <'}'>

    record_update_target =
             namespace? (Name|name)

    infix =
             infix_argument (<break> symbol <break> infix_argument)+

    infix_argument =
             expression
             |
             function_call

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
             <'('> <break> function_or_expression (<break> <','> <break> function_or_expression)+ <break> <')'>

    record =
             <'{'> <break> record_items? <break> <'}'>

    record_items =
             record_item (<break> <','> <break> record_item)*

    record_item =
             name <break> <'='> <break> expression

    case =
             <'case'> <break> case_on <break> <'of'> <break> match (<break> match)*

    case_on =
             function_or_expression

    match =
             match_assignment <break> (match_alias <break>)? <'->'> <break> match_expression  <nl>?

    match_assignment =
             destructure

    match_expression =
             function_or_expression

    match_alias =
             <'as'> <break> name

    destructure =
             type_destructure
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
             record_destructure
             |
             <'('> <break> function_destructure <break>  <')'>
             |
             <'('> <break> destructure <break> <')'>

    record_destructure =
             <'{'> <break> record_destructure_items? <break> <'}'>

    record_destructure_items =
             annotation (<break> <','> <break> annotation)*

    list_destructure =
             <'['> <break> list_destructure_items? <break> <']'>

    list_destructure_items =
             destructure (<break> <','> <break> destructure)*

    function_destructure =
             function_destructure_argument (<break> <'->'> <break> function_destructure_argument)+

    function_destructure_argument =
             destructure

    type_destructure =
             namespace? Name (<break> type_destructure_argument)*

    type_destructure_argument =
             destructure

    variable_destructure =
             namespace? name

    value_destructure =
             literal

    tuple_destructure =
             <'('> <break> destructure (<break> <','> <break> destructure)+ <break> <')'>

    lambda =
             <'('> <'\\\\'> <break> destructure <break> <'->'> <break> lambda_body <break> <')'>

    lambda_body =
             function_or_expression


(* Rules for comments *)

    doc =
             <'{-|'> doc_line* <'-}'>

    doc_part =
             doc_line

    doc_header =
             <'#'> #'.*'

    doc_list =
             <'@docs'> <break> (Name | name) <break> ( <break> <','> <break> (Name | name) )*

    doc_line =
             !'-}' #'.+'


(* Building blocks *)


    Name =
             #'[A-Z][a-zA-Z0-9]*'
    name =
             #'(?!\\b(if|then|else|in|let|case|of|type)\\b)[a-z][a-zA-Z0-9]*'
    int =
             !'->' #'-?[0-9]+'

    float =
             !'->' #'-?[0-9]+\\.[0-9]*'

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

    symbol = #'(?!(->|=)\\b)[+/*<>:&|=^?%#~!-]+'

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

