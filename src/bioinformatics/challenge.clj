(ns bioinformatics.challenge
  (:require
   [clojure.math.combinatorics :as combinatorics]
   [clojure.set :as set]
   [clojure.string :as str]
   [bioinformatics.base :as base]
   [bioinformatics.challenge :as challenge]
   [bioinformatics.dna :as dna]
   [bioinformatics.fasta :as fasta]
   [bioinformatics.fib :as fib]
   [bioinformatics.io :as io]
   [bioinformatics.protein :as protein]
   [bioinformatics.rna :as rna]))

(defn dna
  [raw-dna]
  (let [base->count (frequencies (eduction dna/from-bytes raw-dna))]
    (map base->count [::dna/a ::dna/c ::dna/g ::dna/t])))

(defn rna
  [raw-dna]
  (eduction dna/from-bytes rna/from-dna raw-dna))

(defn fib
  [months litter]
  (fib/rabbits months litter))

(defn revc
  [raw-dna]
  (let [dna (into [] dna/from-bytes raw-dna)]
    (map dna/complement (rseq dna))))

(defn gc
  [raw-fasta]
  (let [id->dna (into {} fasta/from-bytes raw-fasta)
        id->gc (base/vmap dna/gc-content id->dna)
        [id gc] (apply max-key val id->gc)]
    [id (double (* 100 gc))]))

(defn hamm
  [raw-dna-1 raw-dna-2]
  (let [dna1 (eduction dna/from-bytes raw-dna-1)
        dna2 (eduction dna/from-bytes raw-dna-2)]
    (dna/hamming-distance dna1 dna2)))

(defn prot
  [raw-rna]
  (eduction rna/from-bytes protein/from-rna raw-rna))

(defn subs
  [raw-haystack raw-needle]
  (let [haystack (eduction dna/from-bytes raw-haystack)
        needle (sequence dna/from-bytes raw-needle)
        positions (dna/positions-of needle-haystack)]
    (map inc positions)))

(defn mrna
  [raw-protein]
  (protein/mrna-count (eduction protein/from-bytes raw-protein)))

(defn perm
  [length]
  (let [permutations (combinatorics/permutations (range 1 (inc length)))]
    [(count permutations) permutations]))

(defn prtm
  [raw-protein]
  (let [protein (eduction protein/from-bytes in-stream)]
    (double (protein/mass protein))))

(defn revp
  [raw-fasta]
  (let [dna (val (first (into {} fasta/from-bytes in-stream)))]
    (->> (dna/restriction-sites 4 12 dna)
         (map (fn [[ix l]] [(inc ix) l])))))

(defn pper
  [n k]
  (reduce base/mod-mult (range n (- n k) -1)))

(defn sign
  [size]
  (let [entries (for [permutation (combinatorics/permutations
                                   (range 1 (inc size)))
                      signs (combinatorics/selections [-1 1] size)]
                  (map * signs permutation))]
    [(count entries) entries]))

(defn iprb
  [dominant neutral recessive]
  (let [population (+ dominant neutral recessive)
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
     double)))

(defn grph
  [raw-fasta]
  (let [name->dna (into {} fasta/from-bytes in-stream)]
    (fasta/edges 3 name->dna)))
