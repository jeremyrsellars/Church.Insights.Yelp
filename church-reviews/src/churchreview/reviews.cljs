(ns churchreview.reviews
  (:require [cljs.nodejs :as node]
            [churchreview.config :as cfg]
            [churchreview.common :as common
              :refer [make-row->js process-rows read-array-async
                      continue write-json-rows]]))

;; Church Reviews

(def review-fields ["business_id","date","stars"])

(defn make-church-review?-fn [business-id-set]
  (fn church-review? [review-js]
    (not= nil (-> review-js (.-business_id) business-id-set))))

(def review-row->js (make-row->js review-fields))

(defn row-vector->reviews-vec [rows]
  (println "Processing review rows...")
  (mapv review-row->js rows))

(defn review-rows-processor [business-set]
  (partial process-rows row-vector->reviews-vec))

(defn find-church-reviews
  [next-fn]
  (fn [churches]
    (let [church-business-id-set (->> churches (map #(.-business_id %)) (into #{}))]
      (read-array-async (cfg/dataset-file "review") (make-church-review?-fn church-business-id-set)
        (fn [js-row-array]
          (let [processor (review-rows-processor church-business-id-set)]
            (processor js-row-array
              (fn [reviews]
                (write-json-rows cfg/reviews-file reviews)
                (continue next-fn reviews)))))))))

