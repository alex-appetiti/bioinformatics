(ns bioinformatics.fasta
  (:refer-clojure :exclude [newline])
  (:require
   [bioinformatics.base :as base]
   [bioinformatics.dna :as dna]))

(def delimiter (byte \>))

(defn normalised-entry
  [raw-entry]
  (let [[id [newline & dna]] (split-with #(not= base/newline %) raw-entry)]
    [(apply str (map char id))
     (into [] (comp (remove #{base/newline}) dna/from-bytes) dna)]))

(def from-bytes
  (comp
   (partition-by #(= delimiter %))
   (remove #(= [delimiter] %))
   (map normalised-entry)))
