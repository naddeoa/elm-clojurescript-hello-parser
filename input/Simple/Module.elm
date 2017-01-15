module Simple.Module exposing (..)

import List
import String
import Array


type MyType
    = A
    | B


myFunction : MyType -> MyType
myFunction m =
    m
