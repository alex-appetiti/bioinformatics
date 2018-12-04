(ns bioinformatics.rna
  (:require
   [clojure.set :as set]
   [bioinformatics.base :as base]))

(def base->byte
  {:g (byte \G)
   :a (byte \A)
   :u (byte \U)
   :c (byte \C)})

(def byte->base (set/map-invert base->byte))

(def from-dna (map #(case % :t :u %)))

(def from-bytes
  (comp
   (take-while #(not= base/newline %))
   (map byte->base)))

(def from-string
  (comp
   (map byte)
   from-bytes))

(def to-bytes (map base->byte))
