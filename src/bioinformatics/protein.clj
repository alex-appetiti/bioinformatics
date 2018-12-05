(ns bioinformatics.protein
  (:require
   [clojure.string :as str]))

(def codon->amino-acid
  {[:u :u :u] :f
   [:u :u :c] :f
   [:u :u :a] :l
   [:u :u :g] :l
   [:u :c :u] :s
   [:u :c :c] :s
   [:u :c :a] :s
   [:u :c :g] :s
   [:u :a :u] :y
   [:u :a :c] :y
   [:u :a :a] :stop
   [:u :a :g] :stop
   [:u :g :u] :c
   [:u :g :c] :c
   [:u :g :a] :stop
   [:u :g :g] :w
   [:c :u :u] :l
   [:c :u :c] :l
   [:c :u :a] :l
   [:c :u :g] :l
   [:c :c :u] :p
   [:c :c :c] :p
   [:c :c :a] :p
   [:c :c :g] :p
   [:c :a :u] :h
   [:c :a :c] :h
   [:c :a :a] :q
   [:c :a :g] :q
   [:c :g :u] :r
   [:c :g :c] :r
   [:c :g :a] :r
   [:c :g :g] :r
   [:a :u :u] :i
   [:a :u :c] :i
   [:a :u :a] :i
   [:a :u :g] :m
   [:a :c :u] :t
   [:a :c :c] :t
   [:a :c :a] :t
   [:a :c :g] :t
   [:a :a :u] :n
   [:a :a :c] :n
   [:a :a :a] :k
   [:a :a :g] :k
   [:a :g :u] :s
   [:a :g :c] :s
   [:a :g :a] :r
   [:a :g :g] :r
   [:g :u :u] :v
   [:g :u :c] :v
   [:g :u :a] :v
   [:g :u :g] :v
   [:g :c :u] :a
   [:g :c :c] :a
   [:g :c :a] :a
   [:g :c :g] :a
   [:g :a :u] :d
   [:g :a :c] :d
   [:g :a :a] :e
   [:g :a :g] :e
   [:g :g :u] :g
   [:g :g :c] :g
   [:g :g :a] :g
   [:g :g :g] :g})

(def amino-acid->byte
  {:f (byte \F)
   :l (byte \L)
   :s (byte \S)
   :y (byte \Y)
   :c (byte \C)
   :w (byte \W)
   :p (byte \P)
   :h (byte \H)
   :q (byte \Q)
   :r (byte \R)
   :i (byte \I)
   :m (byte \M)
   :t (byte \T)
   :n (byte \N)
   :k (byte \K)
   :v (byte \V)
   :a (byte \A)
   :d (byte \D)
   :e (byte \E)
   :g (byte \G)})

(def from-rna
  (comp
   (partition-all 3)
   (map codon->amino-acid)
   (take-while #(not= :stop %))))

(def to-bytes (map amino-acid->byte))
