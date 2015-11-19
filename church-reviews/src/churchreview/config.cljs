(ns churchreview.config)

(defn dataset-file [short-name]
  (str "..\\yelp_dataset_challenge_academic_dataset\\yelp_academic_dataset_" short-name ".json"))
(def churches-file "churches.json")
(def reviews-file "church-reviews.json")
