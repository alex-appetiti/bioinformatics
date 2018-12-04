(ns bioinformatics.rna)

(defn base->byte
  [base]
  (case base
    :g (byte 71)
    :a (byte 65)
    :u (byte 85)
    :c (byte 67)))

(def from-dna (map #(if (= :t %) :u %)))

(def to-bytes (map base->byte))
