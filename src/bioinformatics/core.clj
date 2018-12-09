(ns bioinformatics.core
  (:require
   [clojure.math.combinatorics :as combinatorics]
   [clojure.set :as set]
   [clojure.string :as str]
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
        (println (protein/mrna-count protein)))

      :perm
      (let [scanner (Scanner. System/in)
            length (.nextInt scanner)
            permutations (combinatorics/permutations (range 1 (inc length)))]
        (println (count permutations))
        (run! #(apply println %) permutations))

      :prtm
      (let [protein (eduction protein/from-bytes in-stream)]
        (println (double (protein/mass protein))))

      :revp
      (let [dna (val (first (into {} fasta/from-bytes in-stream)))
            results (->> (dna/restriction-sites 4 12 dna)
                         (map (fn [[ix l]] [(inc ix) l])))]
        (run! #(apply println %) results))

      :pper
      (let [scanner (Scanner. System/in)
            n (.nextInt scanner)
            k (.nextInt scanner)]
        (println (reduce base/mod-mult (range n (- n k) -1))))

      :sign
      (let [scanner (Scanner. System/in)
            size (.nextInt scanner)
            entries (for [permutation (combinatorics/permutations
                                       (range 1 (inc size)))
                          signs (combinatorics/selections [-1 1] size)]
                      (map * signs permutation))]
        (println (count entries))
        (run! #(apply println %) entries))

      :iprb
      (let [scanner (Scanner. System/in)
            dominant (.nextInt scanner)
            neutral (.nextInt scanner)
            recessive (.nextInt scanner)
            population (+ dominant neutral recessive)
            total (* 4 (combinatorics/count-combinations (range population) 2))
            dom-dom (combinatorics/count-combinations (range dominant) 2)
            neu-neu (combinatorics/count-combinations (range neutral) 2)
            dom-neu (* dominant neutral)
            dom-rec (* dominant recessive)
            neu-rec (* neutral recessive)]
        (->
         (/ (+ (* 4 dom-dom)
               (* 4 dom-neu)
               (* 4 dom-rec)
               (* 3 neu-neu)
               (* 2 neu-rec))
            total)
         double
         println))

      :grph
      (let [name->dna (into {} fasta/from-bytes in-stream)
            edges (fasta/edges 3 name->dna)]
        (run! #(apply println %) edges)))))
