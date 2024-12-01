(ns aoc2024.day1.solution
  (:require [clojure.java.io]
            [clojure.string :as str]))

(def left (atom []))
(def right (atom []))

(with-open [rdr (clojure.java.io/reader "input.txt")]
  (doseq [line (line-seq rdr)
          :when (not (str/blank? line))
          :let [split-str (str/split line #"\s+")
                l-acc nil
                r-acc nil
                l (Integer/parseInt (first split-str))
                r (Integer/parseInt (last split-str))]]
    (swap! left conj l)
    (swap! right conj r)))

(swap! left sort)
(swap! right sort)

(reduce + (map #(abs (- %1 %2)) @left @right))

