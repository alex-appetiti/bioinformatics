(ns bioinformatics.fib)

(defn rabbits
  [months litter]
  (loop [babies 1
         adults 0
         month  1]
    (if (< month months)
      (recur (* adults litter)
             (+ adults babies)
             (inc month))
      (+ babies adults))))
