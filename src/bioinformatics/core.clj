(ns bioinformatics.core
  (:require
   [clojure.math.combinatorics :as combinatorics]
   [clojure.set :as set]
   [clojure.string :as str]
   [bioinformatics.base :as base]
   [bioinformatics.challenge :as challenge]
   [bioinformatics.dna :as dna]
   [bioinformatics.fasta :as fasta]
   [bioinformatics.fib :as fib]
   [bioinformatics.io :as io]
   [bioinformatics.protein :as protein]
   [bioinformatics.rna :as rna])
  (:import
   (java.util Scanner)))

(defn- two-lines
  [bytes]
  (let [[line-1 [nl & line-2]] (split-with #(not= base/newline %) in-stream)]
    [line-1 line-2]))

(defn -main
  [challenge]
  (let [in-stream (io/stream-from System/in)
        scanner (Scanner. System/in)]
    (case (keyword challenge)
      :dna (apply println (challenge/dna in-stream))

      :rna (io/write-all rna/to-bytes (challenge/rna in-stream))

      :fib (println (challenge/fib (.nextInt scanner) (.nextInt scanner)))

      :revc (io/write-all dna/to-bytes (challenge/dna in-stream))

      :gc (apply println (challenge/gc in-stream))

      :hamm (->> in-stream two-lines (apply challenge/hamm) println)

      :prot (io/write-all protein/to-bytes (challenge/prot in-stream))

      :subs (->> in-stream two-lines (apply challenge/subs) (apply println))

      :mrna (println (challenge/mrna in-stream))

      :perm (let [[perm-count perms] (challenge/perm (.nextInt scanner))]
              (println perm-count)
              (run! #(apply println %) perms))

      :prtm (println (challenge/prtm in-stream))

      :revp (run! #(apply println %) (challenge/revp in-stream))

      :pper (println (challenge/pper (.nextInt scanner) (.nextInt scanner)))

      :sign (let [[entry-count entries] (challenge/sign (.nextInt scanner))]
              (println entry-count)
              (run! #(apply println %) entries))

      :iprb (println (challenge/iprb (.nextInt scanner)
                                     (.nextInt scanner)
                                     (.nextInt scanner)))

      :grph (run! #(apply println %) (challenge/grph in-stream)))))
