(require 'cljs.build.api)

(cljs.build.api/build "src"
  {:main 'friendcount.core
   :output-to "main.js"
   :target :nodejs})
