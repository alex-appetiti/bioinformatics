(ns bioinformatics.core
  (:require
   [clojure.set :as set]
   [clojure.string :as str]
   [clojure.java.io :as java.io]
   [bioinformatics.base :as base]
   [bioinformatics.fasta :as fasta]
   [bioinformatics.dna :as dna]
   [bioinformatics.io :as io]
   [bioinformatics.rna :as rna]))

(defn -main
  [challenge]
  (let [in-stream (io/stream-from System/in)]
    (case (keyword challenge)
      :dna
      (let [base->count (frequencies (eduction dna/from-bytes in-stream))
            counts (map base->count [:a :c :g :t])]
        (println (str/join " " counts)))

      :rna
      (let [rna (eduction dna/from-bytes rna/from-dna in-stream)]
        (io/write-all rna/to-bytes rna))

      :revc
      (let [dna (eduction dna/from-bytes in-stream)]
        (io/write-all dna/to-bytes (dna/reverse-complement dna)))

      :gc
      (let [id->dna (into {} fasta/from-bytes in-stream)
            id->gc (base/vmap dna/gc-content id->dna)
            [id gc] (apply max-key val id->gc)]
        (println id)
        (println (double (* 100 gc))))

      :hamm
      (let [[line-1 [nl & line-2]] (split-with #(not= base/newline %) in-stream)
            dna1 (eduction dna/from-bytes line-1)
            dna2 (eduction dna/from-bytes line-2)]
        (println (dna/hamming-distance dna1 dna2))))))
