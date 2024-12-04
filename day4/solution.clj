(ns aoc2024.day4.solution
  (:require [clojure.java.io]
            [clojure.string :as str]
            [clojure.math :as math])
  (:import (java.io File)))

(defn not-blank? [s]
  (not (str/blank? s)))

(defn read-input [filename]
  (when (.exists (File. filename))
    (with-open [rdr (clojure.java.io/reader filename)]
      (->>
       (line-seq rdr)
       (filter not-blank?)
       (mapv #(str/split % #""))))))

(def input (read-input "input.txt"))
; Input: 140x140 grid of characters
; We need to count the number of times XMAS appears
; XMAS can be spelled by going all directions, including diagonals
; XMAS can overlap letters

; So what I'm thinking is doing a sliding window approach. I will scan left to 
; right, top to bottom over the input. At each letter, I will count the number
; of times XMAS can be spelled from the current position. I will do this by
; first checking if the current position contains an X. If it does not, there
; are 0 XMAS's from this position.
; If it does, I'll check each adjacent/diagonal entry for an M. So on and so
; forth until I reach an S. If I reach an S, I return 1.

; It would be helpful to have a recursive helper that takes a letter to find
; and the i,j values of the current index. For every letter that's not an S,
; it will return the sum of recursing on all 8 adjacent/diagonal positions.

; I also need a helper function that generates all of the adjacent indices.
; This should take a position (i, j) and bounds for the grid, and generate a
; sequence (lazily) of all adjacent/diagonal positions.

; If my calculations are correct, I have O(n*m) positions to check, where n is
; the number of rows and m is the number of columns of the grid. For each
; position, I need to check at most 8^4 different paths. So
(defn != [a b]
  (not (== a b)))

(defn in-bounds? [coords dimensions]
  (every? true? (into (map #(>= % 0) coords)
                      (map #(< %1 %2) coords dimensions))))

(defn gen-adj-pos [[row col] dimensions]
  (for [i (range -1 2)
        j (range -1 2)
        :let [new-row (+ row i)
              new-col (+ col j)]
        :when (and
               (or (!= new-row row)
                   (!= new-col col))
               (in-bounds? [new-row new-col] dimensions))]
    (vector new-row new-col)))

(defn count-xmas [[row col] goal grid]
  (let [dimensions [(count grid)
                    (count (first grid))]
        curr-letter (get-in grid [row col])]
    (cond
      (not= curr-letter goal) 0
      (= goal curr-letter "S") 1
      :else (reduce +
                    (for [pos (gen-adj-pos [row col] dimensions)
                          :let [next-goal ({"X" "M" "M" "A" "A" "S"} goal)]]
                      (count-xmas pos next-goal grid))))))

(defn part-one [grid]
  (let [n-rows (count grid)
        n-cols (count (first grid))]
    (reduce +
            (for [row (range 0 n-rows)
                  col (range 0 n-cols)]
              (count-xmas [row col] "X" grid)))))

(def test-input (read-input "test.txt"))
(part-one input)
