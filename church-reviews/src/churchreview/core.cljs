(ns churchreview.core
  (:require [cljs.nodejs :as node]
            [churchreview.config :as config]
            [churchreview.common :as common]
            [churchreview.churches :as churches]
            [churchreview.church-reviews :as church-reviews]
            [churchreview.all-reviews :as all-reviews]))

(node/enable-util-print!)

(def step1-find-churches churches/find-churches)
(def step2-find-church-reviews church-reviews/find-church-reviews)
(def step3-find-church-reviews all-reviews/count-reviews-by-date)

;; Main

(defn -main [& args]
  (step1-find-churches
    (step2-find-church-reviews
      (step3-find-church-reviews
        (fn [reviews]
          (println "Finished with the church data extraction."))))))

(set! *main-cli-fn* -main)
(-main)
