(ns bioinformatics.protein
  (:require
   [clojure.set :as set]
   [clojure.string :as str]
   [bioinformatics.base :as base]
   [bioinformatics.rna :as rna]))

(def amino-acid->mass
  (base/vmap
   rationalize
   {::a 71.03711
    ::c 103.00919
    ::d 115.02694
    ::e 129.04259
    ::f 147.06841
    ::g 57.02146
    ::h 137.05891
    ::i 113.08406
    ::k 128.09496
    ::l 113.08406
    ::m 131.04049
    ::n 114.04293
    ::p 97.05276
    ::q 128.05858
    ::r 156.10111
    ::s 87.03203
    ::t 101.04768
    ::v 99.06841
    ::w 186.07931
    ::y 163.06333
    ::stop 0}))

(def codon->amino-acid
  {[::rna/u ::rna/u ::rna/u] ::f
   [::rna/u ::rna/u ::rna/c] ::f
   [::rna/u ::rna/u ::rna/a] ::l
   [::rna/u ::rna/u ::rna/g] ::l
   [::rna/u ::rna/c ::rna/u] ::s
   [::rna/u ::rna/c ::rna/c] ::s
   [::rna/u ::rna/c ::rna/a] ::s
   [::rna/u ::rna/c ::rna/g] ::s
   [::rna/u ::rna/a ::rna/u] ::y
   [::rna/u ::rna/a ::rna/c] ::y
   [::rna/u ::rna/a ::rna/a] ::stop
   [::rna/u ::rna/a ::rna/g] ::stop
   [::rna/u ::rna/g ::rna/u] ::c
   [::rna/u ::rna/g ::rna/c] ::c
   [::rna/u ::rna/g ::rna/a] ::stop
   [::rna/u ::rna/g ::rna/g] ::w
   [::rna/c ::rna/u ::rna/u] ::l
   [::rna/c ::rna/u ::rna/c] ::l
   [::rna/c ::rna/u ::rna/a] ::l
   [::rna/c ::rna/u ::rna/g] ::l
   [::rna/c ::rna/c ::rna/u] ::p
   [::rna/c ::rna/c ::rna/c] ::p
   [::rna/c ::rna/c ::rna/a] ::p
   [::rna/c ::rna/c ::rna/g] ::p
   [::rna/c ::rna/a ::rna/u] ::h
   [::rna/c ::rna/a ::rna/c] ::h
   [::rna/c ::rna/a ::rna/a] ::q
   [::rna/c ::rna/a ::rna/g] ::q
   [::rna/c ::rna/g ::rna/u] ::r
   [::rna/c ::rna/g ::rna/c] ::r
   [::rna/c ::rna/g ::rna/a] ::r
   [::rna/c ::rna/g ::rna/g] ::r
   [::rna/a ::rna/u ::rna/u] ::i
   [::rna/a ::rna/u ::rna/c] ::i
   [::rna/a ::rna/u ::rna/a] ::i
   [::rna/a ::rna/u ::rna/g] ::m
   [::rna/a ::rna/c ::rna/u] ::t
   [::rna/a ::rna/c ::rna/c] ::t
   [::rna/a ::rna/c ::rna/a] ::t
   [::rna/a ::rna/c ::rna/g] ::t
   [::rna/a ::rna/a ::rna/u] ::n
   [::rna/a ::rna/a ::rna/c] ::n
   [::rna/a ::rna/a ::rna/a] ::k
   [::rna/a ::rna/a ::rna/g] ::k
   [::rna/a ::rna/g ::rna/u] ::s
   [::rna/a ::rna/g ::rna/c] ::s
   [::rna/a ::rna/g ::rna/a] ::r
   [::rna/a ::rna/g ::rna/g] ::r
   [::rna/g ::rna/u ::rna/u] ::v
   [::rna/g ::rna/u ::rna/c] ::v
   [::rna/g ::rna/u ::rna/a] ::v
   [::rna/g ::rna/u ::rna/g] ::v
   [::rna/g ::rna/c ::rna/u] ::a
   [::rna/g ::rna/c ::rna/c] ::a
   [::rna/g ::rna/c ::rna/a] ::a
   [::rna/g ::rna/c ::rna/g] ::a
   [::rna/g ::rna/a ::rna/u] ::d
   [::rna/g ::rna/a ::rna/c] ::d
   [::rna/g ::rna/a ::rna/a] ::e
   [::rna/g ::rna/a ::rna/g] ::e
   [::rna/g ::rna/g ::rna/u] ::g
   [::rna/g ::rna/g ::rna/c] ::g
   [::rna/g ::rna/g ::rna/a] ::g
   [::rna/g ::rna/g ::rna/g] ::g})

(def amino-acid->count
  (->> (group-by val codon->amino-acid)
       (base/vmap count)))

(defn mrna-count
  [protein]
  (transduce
   (map amino-acid->count)
   base/mod-mult
   protein))

(defn mass
  [protein]
  (transduce
   (map amino-acid->mass)
   +
   protein))

(def amino-acid->byte
  {::f (byte \F)
   ::l (byte \L)
   ::s (byte \S)
   ::y (byte \Y)
   ::c (byte \C)
   ::w (byte \W)
   ::p (byte \P)
   ::h (byte \H)
   ::q (byte \Q)
   ::r (byte \R)
   ::i (byte \I)
   ::m (byte \M)
   ::t (byte \T)
   ::n (byte \N)
   ::k (byte \K)
   ::v (byte \V)
   ::a (byte \A)
   ::d (byte \D)
   ::e (byte \E)
   ::g (byte \G)})

(def byte->amino-acid (set/map-invert amino-acid->byte))

(def from-rna
  (comp
   (partition-all 3)
   (map codon->amino-acid)))

(def to-bytes
  (comp
   (take-while #(not= ::stop %))
   (map amino-acid->byte)))

(def from-bytes
  (fn [rf]
    (completing ((keep byte->amino-acid) rf) #(rf %1 ::stop))))
