(require '[cljs.build.api :as b])

(println "Building ...")

(let [start (System/nanoTime)]
  (b/build "src"
    {:main 'elm-toolkit.core
     :output-to "out/elm_toolkit.js"
     :output-dir "out"
     :target :nodejs
     :verbose true})
  (println "... done. Elapsed" (/ (- (System/nanoTime) start) 1e9) "seconds"))


