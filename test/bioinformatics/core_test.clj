(ns bioinformatics.core-test
  (:require
   [clojure.test :refer :all]
   [bioinformatics.core :refer :all]
   [bioinformatics.io :as io]
   [bioinformatics.dna :as dna]))

(deftest dna-test
  (let [in-stream (.getBytes "GATTACA")
        base->count (frequencies (eduction dna/from-bytes in-stream))
        counts (map base->count [::dna/a ::dna/c ::dna/g ::dna/t])]
    (is (= [3 1 1 2] counts))))
