(ns bioinformatics.dna
  (:refer-clojure :exclude [complement])
  (:require
   [clojure.set :as set]
   [bioinformatics.base :as base])
  (:import
   (java.util LinkedList)))

(def base->byte
  {::g (byte \G)
   ::a (byte \A)
   ::t (byte \T)
   ::c (byte \C)})

(def byte->base (set/map-invert base->byte))

(def complement
  {::g ::c
   ::a ::t
   ::t ::a
   ::c ::g})

(defn reverse-complement
  [dna]
  (map complement (reverse dna)))

(defn gc-content
  [dna]
  (let [{:keys [::g ::c] :or {g 0 c 0}} (frequencies dna)
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

; Revisit later.
(defn restriction-sites
  [^long min ^long max dna]
  (for [size (range min (inc max))
        :let [snippets (partition size 1 dna)]
        [ix snippet] (map-indexed vector snippets)
        :when (= snippet (reverse-complement snippet))]
    [ix size]))

(def from-bytes
  (comp
   (take-while #(not= base/newline %))
   (map byte->base)))

(def from-string
  (comp
   (map byte)
   from-bytes))

(def to-bytes (map base->byte))
