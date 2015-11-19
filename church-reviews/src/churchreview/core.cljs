(ns churchreview.core
  (:require [cljs.nodejs :as node]
            [churchreview.config :as config]
            [churchreview.common :as common]
            [churchreview.churches :as churches]
            [churchreview.reviews :as reviews]))

(node/enable-util-print!)

(def step1-find-churches churches/find-churches)
(def step2-find-church-reviews reviews/find-church-reviews)

;; Main

(defn -main [& args]
  (step1-find-churches
    (step2-find-church-reviews
      (fn [reviews]
        (println "Finished with the church data extraction.")))))

(set! *main-cli-fn* -main)
(-main)
