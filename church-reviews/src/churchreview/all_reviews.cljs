(ns churchreview.all-reviews
  (:require [cljs.nodejs :as node]
            [churchreview.config :as cfg]
            [churchreview.common :as common
              :refer [make-row->js process-rows read-array-async
                      continue write-json-rows]]))

;; Count all reviews, grouping by day

(def review-fields ["business_id","date","stars"])

(def match-everything (constantly true))

(defn review-row->date-string [row]
  (.-date row))

(defn post-process [dates]
  (println "Counting reviews...")
  (->> (frequencies dates)
       (into (sorted-map))
       (mapv clj->js)))

(defn count-reviews-by-date
  [next-fn]
  (fn [_]
    (read-array-async
      (cfg/dataset-file "review")
      match-everything
      review-row->date-string
      (fn [js-row-array]
        (let [processor (partial process-rows post-process)]
          (processor js-row-array
            (fn [reviews]
              (write-json-rows cfg/all-reviews-file reviews)
              (continue next-fn reviews))))))))

