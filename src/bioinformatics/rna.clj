(ns bioinformatics.rna
  (:require
   [clojure.set :as set]
   [bioinformatics.base :as base]
   [bioinformatics.dna :as dna]))

(def base->byte
  {::g (byte \G)
   ::a (byte \A)
   ::u (byte \U)
   ::c (byte \C)})

(def dna->rna
  {:dna/g ::g
   :dna/a ::a
   :dna/t ::u
   :dna/c ::c})

(def byte->base (set/map-invert base->byte))

(def from-dna (map dna->rna))

(def from-bytes
  (comp
   (take-while #(not= base/newline %))
   (map byte->base)))

(def from-string
  (comp
   (map byte)
   from-bytes))

(def to-bytes (map base->byte))
