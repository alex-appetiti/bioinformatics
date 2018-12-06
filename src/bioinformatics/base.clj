(ns bioinformatics.base
  (:refer-clojure :exclude [newline])
  (:import
   (java.util LinkedList)))

(def newline (byte \newline))

(defn mod-mult
  ([] 1)
  ([^long n] (mod n 1000000))
  ([^long n ^long m] (* (mod n 1000000) m)))

(defn vmap
  [f m]
  (into {} (map (fn [[k v]] [k (f v)])) m))

(defn sliding-window
  [^long n]
  (fn [rf]
    (let [window (LinkedList.)]
      (fn
        ([] (rf))
        ([result] (rf result))
        ([result input]
         (.addLast window input)
         (when (> (.size window) n) (.remove window))
         (if (= n (.size window))
           (rf result (vec (.toArray window)))
           result))))))
