(ns aoc2024.day4.solution
  (:require
   [clojure.java.io]
   [clojure.string :as str])
  (:import
   (java.io File)))

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
(defn != [a b]
  (not (== a b)))

(def test-input (read-input "test.txt"))

(defn in-bounds? [coords dimensions]
  (every? true? (into (map #(>= % 0) coords)
                      (map #(< %1 %2) coords dimensions))))

(defn add-vector [u v]
  (mapv + u v))

(defn scale-vector [s v]
  (mapv #(* s %) v))

; Correct test for counting Xmas:
; Take a pos, direction vector, and a grid
; Init curr pos to pos:
(defn check-xmas [pos dir grid]
  (let [dimensions [(count grid)
                    (count (first grid))]]
    (->>
     (mapv #(vector (add-vector pos (scale-vector %1 dir)) %2)
           (range 4)
           ["X" "M" "A" "S"])
     (every? #(and (in-bounds? (first %) dimensions)
                   (= (get-in grid (first %)) (second %))))
     ({false 0 true 1}))))

(defn generate-directions []
  (for [i (range -1 2)
        j (range -1 2)]
    (vector i j)))

(defn part-one [grid]
  (let [n-rows (count grid)
        n-cols (count (first grid))]
    (reduce +
            (for [col (range n-cols)
                  row (range n-rows)]
              (reduce + (map #(check-xmas [row col] % grid) (generate-directions)))))))

(part-one input)

(defn check-mas [pos dir grid]
  (let [dimensions [(count grid)
                    (count (first grid))]]
    (->>
     (map #(vector (add-vector pos (scale-vector %1 dir)) %2) (range -1 2)
          ["M" "A" "S"])
     (every? #(and (in-bounds? (first %) dimensions)
                   (= (get-in grid (first %)) (second %)))))))

(check-mas [1 2] [1 1] test-input)

(defn x-mas? [pos [a b] grid]
  (and
   (or
    (check-mas pos a grid)
    (check-mas pos (scale-vector -1 a) grid))
   (or
    (check-mas pos b grid)
    (check-mas pos (scale-vector -1 b) grid))))

(defn check-x-mas [pos grid]
  (let [valid-pairs '([[1 1] [1 -1]])]
    (->>
     (mapv #(x-mas? pos % grid) valid-pairs)
     (mapv {false 0 true 1}))))

(defn part-two [grid]
  (let [n-rows (count grid)
        n-cols (count (first grid))]
    (reduce + (for [col (range n-cols)
                    row (range n-rows)]
                (reduce + (check-x-mas [row col] grid))))))

(part-two input)
