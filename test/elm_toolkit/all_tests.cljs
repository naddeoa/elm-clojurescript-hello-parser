(ns elm-toolkit.all-tests
  (:require [elm-toolkit.if_test]
            [elm-toolkit.import_test]
            [elm-toolkit.module_test]
            [elm-toolkit.value_test]
            [elm-toolkit.render_test]
            [elm-toolkit.lambda_test]
            [elm-toolkit.tuple_test]
            [elm-toolkit.comment_test]
            [pjstadig.humane-test-output]
            [elm-toolkit.let_test]
            [elm-toolkit.type_test]
            [elm-toolkit.case_test]
            [elm-toolkit.string_test]
            [elm-toolkit.record_destructure_test]
            [elm-toolkit.destructure_test]
            [elm-toolkit.record_test]
            [elm-toolkit.function_test]
            [elm-toolkit.record_update_test]
            [elm-toolkit.type_alias_test]
            [elm-toolkit.annotation_test]))

(cljs.test/run-all-tests)
