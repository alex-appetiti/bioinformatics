(ns bioinformatics.fasta
  (:refer-clojure :exclude [newline])
  (:require
   [clojure.set :as set]
   [bioinformatics.base :as base]
   [bioinformatics.dna :as dna]))

(def delimiter (byte \>))

(defn normalised-entry
  [raw-entry]
  (let [[id [newline & dna]] (split-with #(not= base/newline %) raw-entry)]
    [(apply str (map char id))
     (into [] (comp (remove #{base/newline}) dna/from-bytes) dna)]))

(defn edges
  [suffix-len name->dna]
  (let [dna->names (reduce-kv
                    #(update %1 %3 (fnil conj []) %2)
                    {}
                    name->dna)
        out->names (reduce-kv
                   #(update %1 (take suffix-len %2) (fnil into []) %3)
                   {}
                   dna->names)
        in->names (reduce-kv
                   #(update %1 (take-last suffix-len %2) (fnil into []) %3)
                   {}
                   dna->names)]
    (for [out (keys out->names)
          in-name (in->names out)
          out-name (out->names out)
          :when (not= in-name out-name)]
      [in-name out-name])))

(def from-bytes
  (comp
   (partition-by #(= delimiter %))
   (remove #(= [delimiter] %))
   (map normalised-entry)))
