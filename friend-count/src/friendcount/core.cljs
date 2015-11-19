(ns friendcount.core
  (:require [cljs.nodejs :as node]))

(node/enable-util-print!)

(def users-file "..\\yelp_dataset_challenge_academic_dataset\\yelp_academic_dataset_user.json")
(def friend-counts-file "user_friend_counts.json")

(def ^:private example-row #js {:funny 166, :useful 278, :cool 245, :review_count 108, :name "Russel", :user_id "18kPq7GPye-YQ3LyKyAZPw", :friends #js ["rpOyqD_893cqmDAtJLbdog" "4U9kSBLuBDU391x6bxU-YA" "fHtTaujcyKvXglE33Z5yIw"], :fans 69, :average_stars 4.14, :type "user", :compliments #js {:profile 8, :cute 15, :funny 11, :plain 25, :writer 9, :note 20, :photos 15, :hot 48, :cool 78, :more 3}, :elite #js [2005 2006]})

(defn row-vector->friend-count-map [rows]
  (zipmap (map #(.-user_id %) rows) (map #(-> % .-friends .-length) rows)))

(def LineByLineReader (node/require "line-by-line"))

(defn line-by-line-reader [file-name {:keys [error line end] :as handlers}]
  (reduce-kv
    (fn [lr k handler-fn]
      (.on lr (name k) handler-fn))
    (new LineByLineReader file-name)
    handlers))

(defn read-array-async [file-name call-back]
  (let [lines (array)
        lr (line-by-line-reader file-name
    {:error #(.error js/console %)
     :line #(.push lines (.parse js/JSON %))
     :end #(call-back lines)})]))

(defn write-json-rows [file-name js-rows]
  (let [stream (-> (node/require "fs") (.createWriteStream file-name))]
    (.once stream "open"
      (fn [_]
        (doseq [row js-rows]
          (.write stream (.stringify js/JSON row))
          (.write stream "\n"))
        (.end stream)))))
 
(defn process-rows [js-row-array]
  (println "read" (.-length js-row-array) "rows")
  (println (aget js-row-array 0))
  (let [friend-counts-by-id (row-vector->friend-count-map (vec js-row-array))]
    (println "rows: " (count friend-counts-by-id))
    (println "first: " (first friend-counts-by-id))
    (doseq [row js-row-array]
      (let [friends (.-friends row)
            friend-counts (map friend-counts-by-id friends)]
        (doseq [attr ["friends" "votes" "elite" "compliments" "type"]]
          (js-delete row attr))
        (aset row "friend-counts" (clj->js friend-counts))))
    (write-json-rows friend-counts-file js-row-array)))

(defn -main [& args]
  (read-array-async users-file process-rows))

(set! *main-cli-fn* -main)

