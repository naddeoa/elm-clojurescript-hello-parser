module Simple.Module exposing (..)

import List
import String
import Array


type MyType
    = A
    | B


type Fish
    = Sticks
    | SomethingElse


myFunction : MyType -> MyType
myFunction m =
    m


otherFunction : Int -> Int -> Int
otherFunction a b =
    a + b
