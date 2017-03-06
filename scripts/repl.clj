(require
  '[cljs.repl :as repl]
  '[cljs.repl.node :as node])

;; (cljs.build.api/build "src"
;;                       {:main 'elm-toolkit.core
;;                        :output-to "out/repl.js"
;;                        :verbose true})

;; (repl/repl (node/repl-env)
;;            :watch "src"
;;            :output-dir "out")

(repl/repl (node/repl-env))
