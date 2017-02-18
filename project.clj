(defproject elm-toolkit "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.293"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [com.rpl/specter "0.13.2"]
                 [instaparse "1.4.5"]
                 [pjstadig/humane-test-output "0.8.1"]
                 [figwheel-sidecar "0.5.0-SNAPSHOT" :scope "test"]

                 ; For repl use
                 [com.cemerick/piggieback "0.2.1"]
                 [org.clojure/tools.nrepl "0.2.10"]
                 ]
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
  :jvm-opts ^:replace ["-Xmx1g" "-server"]
  :plugins [[lein-npm "0.6.1"]
            [lein-cljsbuild "1.0.0-alpha2"]
            [lein-cljfmt "0.5.6"]]
  :npm {:dependencies [[source-map-support "0.4.0"]]}
  :source-paths ["src" "target/classes"]
  :clean-targets ["out" "release"]
  :target-path "target")
