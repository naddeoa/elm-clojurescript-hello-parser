module Array
    exposing
        ( Array
        , empty
        , repeat
        , initialize
        , fromList
        , isEmpty
        , length
        , push
        , append
        , get
        , set
        , slice
        , toList
        , toIndexedList
        , map
        , indexedMap
        , filter
        , foldl
        , foldr
        )

{-| A library for fast immutable arrays. The elements in an array must have the
same type. The arrays are implemented in Relaxed Radix Balanced-Trees for fast
reads, updates, and appends.

# Arrays
@docs Array

# Creating Arrays
@docs empty, repeat, initialize, fromList

# Basics
@docs isEmpty, length, push, append

# Get and Set
@docs get, set

# Taking Arrays Apart
@docs slice, toList, toIndexedList

# Mapping, Filtering, and Folding
@docs map, indexedMap, filter, foldl, foldr
-}

import Native.Array
import A.B.Basics exposing (..)
import Maybe exposing (..)
import List


{-| Representation of fast immutable arrays. You can create arrays of integers
(`Array Int`) or strings (`Array String`) or any other type of value you can
dream up.
-}
type Array a
    = Array


type Thing a
    = A a
    | B


{-| Initialize an array. `initialize n f` creates an array of length `n` with
the element at index `i` initialized to the result of `(f i)`.

    initialize 4 identity    == fromList [0,1,2,3]
    initialize 4 (\n -> n*n) == fromList [0,1,4,9]
    initialize 4 (always 0)  == fromList [0,0,0,0]

-}
initialize : Int -> (Int -> a) -> Array a
initialize =
    Native.Array.initialize


repeat : Int -> a -> Array a
repeat n e =
    initialize n (always e)


fromList : List a -> Array a
fromList =
    Native.Array.fromList


foldr : (a -> b -> b) -> b -> Array a -> b
foldr =
    Native.Array.foldr


inf : Bool
inf =
    True && True || False


get : Int -> Array a -> Maybe a
get i array =
    if 0 <= i && i < Native.Array.length array then
        Just (Native.Array.get i array)
    else
        Nothing


filter2 : (a -> Bool) -> Array a -> Array a
filter2 isOkay arr =
    let
        a =
            5

        b =
            True && True

        fish : Bool -> Bool -> Bool
        fish a b =
            a || b

        ( three, four ) =
            ( 3, 4 )

        sixteen =
            4 ^ 2
    in
        Native.Array.foldl update Native.Array.empty arr


filter : (a -> Bool) -> Array a -> Array a
filter isOkay arr =
    let
        update x xs =
            if isOkay x then
                Native.Array.push x xs
            else
                xs
    in
        Native.Array.foldl update Native.Array.empty arr


getThing : Maybe a -> Maybe a
getThing maybe =
    case maybe of
        Nothing ->
            Nothing

        Maybe a ->
            Just a

