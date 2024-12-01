(ns aoc2024.day1.solution
  (:require [clojure.java.io]
            [clojure.string :as str])
  (:import (java.io File)))

; I need to:
; 1) Read the file
; 2) line by line
; 3) Split each line
; 4) Cast each split number to an integer
; 5) Accumulate each pair of integers into a map where we:
;   a) Add the first number to left list
;   b) Add the second number to right list

; First, I am choosing to use java.io.Reader instead of slurp because
; the input.txt file is large and I don't want to save it all in memory
(defn not-blank? [s]
  (not (str/blank? s)))

; Reducing function
(defn collect [acc [l r]]
  ; We need to use a threading macro to ensure that the right update is
  ; performed on the result of the left update
  (-> acc
      (update :left conj l)
      (update :right conj r)))

(defn read-input [file]
  (when (.exists (File. file))
    (with-open [rdr (clojure.java.io/reader file)]
      (->>
       ; This is the initial value ->> is threading to the next fxns
       (line-seq rdr)
       (filter not-blank?)
       (map #(str/split % #"\s+"))
       ; Remember: map is being applied to the sequence supplied by ->>,
       ; NOT each vector element. Need to nest/destructure each vector
       ; element to perform parse-long on each string and keep them
       ; grouped together
       (map #(mapv parse-long %))
       ; Now, reduce it into a map. This forces evaluation, whereas take
       ; is never evaluated and closes the stream.
       (reduce collect {:left [] :right []})))))

(def input (read-input "input.txt"))

; Calculate similarity for part one
(defn similarity-score-one [[l r]]
  (abs (- l r)))

; Calculate total score for part one
(defn part-one [m]
  (let [l (sort (get m :left))
        r (sort (get m :right))]
    (->> l
         (map vector r)
         (map similarity-score-one)
         (reduce +))))

(part-one input) ; 2113135

; Calculate similarity score for part two
; l is the key value from the left map, freq-l is the frequency of l in the
; l-freqs map, and r-freqs is the map of right frequencies
(defn similarity-score-two [l freq-l r-freqs]
  (* l freq-l (or (get r-freqs l) 0)))

; Calculate total score for part two
(defn part-two [m]
  (let [l-freqs (frequencies (get m :left))
        r-freqs (frequencies (get m :right))]
    (->> l-freqs
         (map (fn [[l freq-l]]
                (similarity-score-two l freq-l r-freqs)))
         (reduce +))))

(part-two input) ; 19097157
