(require '[cljs.build.api :as b])

(b/watch "src"
  {:main 'friendcount.core
   :output-to "out/friendcount.js"
   :output-dir "out"})
