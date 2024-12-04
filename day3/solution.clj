(ns aoc2024.day3.solution
  (:require [clojure.java.io]
            [clojure.string :as str])
  (:import (java.io File)))

(defn not-blank? [s]
  (not (str/blank? s)))

(defn read-input [filename]
  (when (.exists (File. filename))
    (with-open [rdr (clojure.java.io/reader filename)]
      (->>
       (line-seq rdr)
       (filter not-blank?)
       (apply str)))))

(def input (read-input "input.txt"))

(def mul-regex #"mul\((\d{1,3}),(\d{1,3})\)")

(defn extract-muls [s]
  (->> (re-seq mul-regex s)
       (map #(subvec % 1))
       (map #(mapv parse-long %))))

(defn part-one [s]
  (reduce + (map
             (fn [[a b]] (* a b))
             (extract-muls s))))

(part-one input) ; 181345830

(defn extract-enabled [s]
  (->> (re-seq #"do\(\).*?don't\(\)" s)
       (apply str)))

(defn part-two [s]
  (reduce + (map
             (fn [[a b]] (* a b))
             (extract-muls
              (extract-enabled (str "do()" s))))))

(part-two input) ; 98729041
