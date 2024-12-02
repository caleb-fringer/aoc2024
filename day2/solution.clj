(ns aoc2024.day2.solution
  (:require [clojure.java.io]
            [clojure.string :as str])
  (:import (java.io File)))

; Input: list of vectors of integers. Each line represents 1 report, and each
; report is a vector of levels, which are integers.
; Goal: count how many reports are gradually increasing or decreasing

; A report is gradually increasing if for every level at position n in the report
; report[n-1] + 1 <= report[n] <= report[n-1] + 3
(defn increasing? [[a b]]
  (and (<= (+ a 1) b) (<= b (+ a 3))))

; A report is gradually decreasing if for every level at position n in the report
; report[n-1] - 3 <= report[n] <= report[n-1] - 1
(defn decreasing? [[a b]]
  (and (<= (- a 3) b) (<= b (- a 1))))

; Approach: Read the list into memory, parsing each line into a vector of ints
; Map the list of reports to a new list where 1 represents a safe report and 0
; represents an unsafe report. Reduce this to get the count
(defn not-blank? [s]
  (not (str/blank? s)))

(defn read-input [filename]
  (when (.exists (File. filename))
    (with-open [rdr (clojure.java.io/reader filename)]
      (->>
       (line-seq rdr)
       (filter not-blank?)
       (map #(str/split % #"\s+"))
       (map #(mapv parse-long %))
       (reduce conj [])))))

(def my-input (read-input "input.txt"))

(defn gradually-increasing? [s]
  (->> s
       (partition 2 1)
       (every? increasing?)))

(defn gradually-decreasing? [s]
  (->> s
       (partition 2 1)
       (every? decreasing?)))

(defn safe? [report]
  (or (gradually-increasing? report)
      (gradually-decreasing? report)))

(defn part-one [input]
  (reduce + (map {nil 0 true 1} (map safe? input))))

(def test-input (read-input "test.txt"))
(part-one test-input) ; 2

(part-one my-input) ; 631

; The largest length report is:
(apply max (map count my-input)) ; 8
; So, the reports lengths are constant. Currently, if we have n reports
; It takes O(1) time to calculate the safety of a single report, and
; O(n) time to calculate safety of n reports.
; If we allow ourselves to optionally remove 1 of 8 levels, then it now takes
; 8 * O(1) time to calculate the safety of a single report. So this is still
; constant time.

(defn remove-nth [n v]
  (into (subvec v 0 n) (subvec v (+ n 1))))

; Concatenate the vector with every vector where the nth element is removed
(defn remove-every-nth [v]
  (into (list v)
        (map #(remove-nth % v) (range (count v)))))

; A report is safe if some permutation of removing the nth element is safe
(defn safe-two? [report]
  (some safe? (remove-every-nth report)))

(defn part-two [input]
  (reduce + (map {nil 0 true 1} (map safe-two? input))))

(part-two test-input) ; 4
(part-two my-input) ; 665
