(ns churchreview.churches
  (:require [cljs.nodejs :as node]
            [churchreview.config :as cfg]
            [churchreview.common :as common
              :refer [make-row->js process-rows read-array-async
                      continue write-json-rows]]))

;; Church Businesses

(def business-fields ["business_id","name","open","review_count","stars"])

(defn church? [business-row]
  (-> business-row
      .-categories
      (.indexOf "Churches")
      (not= -1)))

(def church-row->js (make-row->js business-fields))

(defn row-vector->churches-vec [rows]
  (println "Processed church rows...")
  rows)

(def process-church-rows (partial process-rows row-vector->churches-vec))

(defn find-churches
  [next-fn]
  (read-array-async
    (cfg/dataset-file "business")
    church?
    church-row->js
    (fn [js-row-array]
      (process-church-rows js-row-array
        (fn [churches]
          (write-json-rows cfg/churches-file churches)
          (continue next-fn churches))))))