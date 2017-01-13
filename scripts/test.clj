(require '[cljs.build.api :as b])

(b/watch "test"
  {:main 'elm-toolkit.parser_test
   :output-to "out/test.js"
   :target :nodejs
   :output-dir "out"})

