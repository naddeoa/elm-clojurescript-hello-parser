(require '[cljs.build.api :as b])

(b/watch "src"
  {:main 'elm-toolkit.core
   :output-to "out/elm_toolkit.js"
   :target :nodejs
   :output-dir "out"})
