(require
  '[cljs.build.api :as b]
  '[cljs.repl :as repl]
  '[cljs.repl.browser :as browser])

(b/build "src"
  {:main 'elm-toolkit.core
   :output-to "out/elm_toolkit.js"
   :output-dir "out"
   :target :nodejs
   :verbose true})

(repl/repl (browser/repl-env)
  :output-dir "out")
