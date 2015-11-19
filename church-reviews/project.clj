(defproject churchreviews "0.1.0-SNAPSHOT"
  :description "Analyze Yelp! reviews of churches"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170" :classifier "aot"
                  :exclusion [org.clojure/data.json]]
                 [org.clojure/data.json "0.2.6" :classifier "aot"]]
  :jvm-opts ^:replace ["-Xmx1g" "-server"]
  :plugins [[lein-npm "0.6.1"]
            [lein-cljsbuild "1.1.1"]]
  :npm {:dependencies [[source-map-support "0.3.2"]]}
  :source-paths ["src" "target/classes"]
  :clean-targets ["out" "release"]
  :target-path "target"
  :cljsbuild {
    :builds {:dev {:source-paths ["src"]
                   :notify-command ["node" "out/main.js"]
                   :compiler {
                    :output-to "out/main.js"  ; default: target/cljsbuild-main.js
                    :optimizations :simple
                    :pretty-print true
                    :target :nodej}}}})
