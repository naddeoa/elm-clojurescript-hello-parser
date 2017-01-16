module Simple.Lib exposing (hello, name)

import List


name : String
name =
    toString List.empty


hello : String -> String
hello str =
    str ++ str


emptyList : List a
emptyList =
    List.empty
