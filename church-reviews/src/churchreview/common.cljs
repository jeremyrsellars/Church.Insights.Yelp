(ns churchreview.common
  (:require [cljs.nodejs :as node]))

(defn continue [continuation-fn & args]
  (when continuation-fn
    (apply continuation-fn args)))

;; line-by-line Json parsing and output functions

(defn make-row->js
  "Prepares a church js object for CSV"
  [fields]
  (fn row->js [row]
    (-> (zipmap
          fields
          (map (partial aget row) fields))
        clj->js)))


(def LineByLineReader (node/require "line-by-line"))

(defn line-by-line-reader [file-name {:keys [error line end] :as handlers}]
  (println "About to read lines from " file-name)
  (reduce-kv
    (fn [lr k handler-fn]
      (.on lr (name k) handler-fn))
    (new LineByLineReader file-name)
    handlers))

(defn read-array-async [file-name keep? call-back]
  (let [lines (array)
        lr (line-by-line-reader file-name
    {:error #(.error js/console %)
     :line #(let [o (.parse js/JSON %)]
              (when (keep? o)
                (.push lines o)))
     :end #(call-back lines)})]))

(defn process-rows [transform js-row-array continuation-fn]
  (println "read" (.-length js-row-array) "rows")
  (println (aget js-row-array 0))
  (let [items (transform (vec js-row-array))]
    (println "rows: " (count items))
    (println "first: " (first items))
    (continuation-fn items)))

(defn write-json-rows [file-name js-rows]
  (let [stream (-> (node/require "fs") (.createWriteStream file-name))]
    (.once stream "open"
      (fn [_]
        (doseq [row js-rows]
          (.write stream (.stringify js/JSON row))
          (.write stream "\n"))
        (.end stream)))))
