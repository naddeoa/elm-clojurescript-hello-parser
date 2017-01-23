(ns elm-toolkit.all-tests
  (:require [elm-toolkit.if_test]
            [elm-toolkit.import_test]
            [elm-toolkit.module_test]
            [elm-toolkit.value_test]
            [elm-toolkit.render_test]
            [elm-toolkit.lambda_test]
            [pjstadig.humane-test-output]
            [elm-toolkit.let_test]
            [elm-toolkit.type_test]
            [elm-toolkit.annotation_test]))

(cljs.test/run-all-tests)
