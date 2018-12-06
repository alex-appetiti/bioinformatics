(ns bioinformatics.core
  (:require
   [clojure.set :as set]
   [clojure.string :as str]
   [clojure.java.io :as java.io]
   [bioinformatics.base :as base]
   [bioinformatics.fasta :as fasta]
   [bioinformatics.fib :as fib]
   [bioinformatics.dna :as dna]
   [bioinformatics.io :as io]
   [bioinformatics.protein :as protein]
   [bioinformatics.rna :as rna])
  (:import
   (java.util Scanner)))

(defn -main
  [challenge]
  (let [in-stream (io/stream-from System/in)]
    (case (keyword challenge)
      :dna
      (let [base->count (frequencies (eduction dna/from-bytes in-stream))
            _ (println base->count)
            counts (map base->count [::dna/a ::dna/c ::dna/g ::dna/t])]
        (apply println counts))

      :rna
      (let [rna (eduction dna/from-bytes rna/from-dna in-stream)]
        (io/write-all rna/to-bytes rna))

      :fib
      (let [scanner (Scanner. System/in)
            months (.nextInt scanner)
            litter (.nextInt scanner)]
        (println (fib/rabbits months litter)))

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
        (println (dna/hamming-distance dna1 dna2)))

      :prot
      (let [protein (eduction rna/from-bytes protein/from-rna in-stream)]
        (io/write-all protein/to-bytes protein))

      :subs
      (let [[line-1 [nl & line-2]] (split-with #(not= base/newline %) in-stream)
            haystack (eduction dna/from-bytes line-1)
            needle (sequence dna/from-bytes line-2)
            positions (dna/positions-of needle haystack)]
        (apply println (map inc positions)))

      :mrna
      (let [protein (eduction protein/from-bytes in-stream)]
        (println (protein/mrna-count protein))))))
