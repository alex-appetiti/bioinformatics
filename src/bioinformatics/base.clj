(ns bioinformatics.base
  (:refer-clojure :exclude [newline]))

(def newline (byte \newline))

(defn vmap
  [f m]
  (into {} (map (fn [[k v]] [k (f v)])) m))
