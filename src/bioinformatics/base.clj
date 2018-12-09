(ns bioinformatics.base
  (:refer-clojure :exclude [newline])
  (:import
   (java.util LinkedList)))

(def newline (byte \newline))

(defn mod-mult
  ([] 1)
  ([^long n] (mod n 1000000))
  ([^long n ^long m] (mod (* n m) 1000000)))

(defn vmap
  [f m]
  (into {} (map (fn [[k v]] [k (f v)])) m))

(defn kmap
  [f m]
  (into {} (map (fn [[k v]] [(f k) v])) m))

(defn sliding-window
  ([^long n] (sliding-window n :full))
  ([^long n full-or-all]
   (fn [rf]
     (let [window (LinkedList.)]
       (fn
         ([] (rf))
         ([result]
          (case full-or-all
            :full (rf result)
            :all
            (loop [result result]
              (if (.isEmpty window)
                (rf result)
                (do
                  (.remove window)
                  (recur (rf result (vec (.toArray window)))))))))
         ([result input]
          (.addLast window input)
          (when (> (.size window) n) (.remove window))
          (if (= n (.size window))
            (rf result (vec (.toArray window)))
            result)))))))
