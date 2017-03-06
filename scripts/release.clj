(require '[cljs.build.api :as b])

(println "Building ...")

(let [start (System/nanoTime)]
  (b/build "src"
    {:output-to "release/index.js"
     :main 'elm-toolkit.core
     :output-dir "release"
     :optimizations :none
     :target :nodejs
     :verbose true})
  (println "... done. Elapsed" (/ (- (System/nanoTime) start) 1e9) "seconds"))
