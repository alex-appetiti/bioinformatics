(ns bioinformatics.io
  (:require
   [clojure.java.io :as io])
  (:import
   (java.io OutputStream PrintStream InputStreamReader BufferedReader)
   (java.util.stream Collectors)))

(defn stream-from
  ([source] (stream-from source 1024))
  ([source buffer-size]
   (let [buffer (byte-array buffer-size)
         stream (io/input-stream source)]
     ((fn next-step []
        (lazy-seq
         (let [bytes-read (.read stream buffer)]
           (when-not (= -1 bytes-read)
             (let [chunk-buff (chunk-buffer bytes-read)]
               (dotimes [i bytes-read] (chunk-append chunk-buff (aget buffer i)))
               (chunk-cons (chunk chunk-buff) (next-step)))))))))))

(defn write-all
  [xform coll]
  (let [f (xform #(.write System/out (byte %2)))]
    (reduce f nil coll)
    (.write System/out (int \newline))))
