(ns bioinformatics.dna
  (:refer-clojure :exclude [complement])
  (:require
   [clojure.set :as set]
   [bioinformatics.base :as base]))

(def base->byte
  {:g (byte \G)
   :a (byte \A)
   :t (byte \T)
   :c (byte \C)})

(def byte->base (set/map-invert base->byte))

(def complement
  {:g :c
   :a :t
   :t :a
   :c :g})

(defn reverse-complement
  [dna]
  (map complement (reverse dna)))

(defn gc-content
  [dna]
  (let [{:keys [g c]} (frequencies dna)
        length (count dna)]
    (/ (+ g c) length)))

(defn hamming-distance
  [dna-1 dna-2]
  (transduce
   (map (fn [[b1 b2]] (if-not (= b1 b2) 1 0)))
   +
   (map vector dna-1 dna-2)))

(defn positions-of
  ([needle]
   (comp
    (base/sliding-window (count needle))
    (keep-indexed #(when (= needle %2) %1))))
  ([needle haystack]
   (into [] (positions-of needle) haystack)))

(def from-bytes
  (comp
   (take-while #(not= base/newline %))
   (map byte->base)))

(def from-string
  (comp
   (map byte)
   from-bytes))

(def to-bytes (map base->byte))
