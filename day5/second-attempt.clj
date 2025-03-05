(ns aoc2024.day5.solution
  (:require
   [clojure.string :as str])
  (:import
   [java.io File]))

(defn not-blank? [s]
  (not (str/blank? s)))

(defn rule? [s]
  (when-let [rule (re-matches #"(\d+)\|(\d+)" s)]
    (mapv parse-long (rest rule))))

(rule? "97,43,56")
(rule? "")

(defn update? [s]
  (when (re-matches #"\d+(?:,\d+)*" s)
    (mapv parse-long (re-seq #"\d+" s))))

(defn process-line [s coll]
  (cond (when-let [[x y] (rule? s)]
          (assoc-in coll [:rules x] y))
        (when-let [update-list (update? s)]
          (update-in coll [:updates] conj update-list))))

(defn read-input [filename]
  (when (.exists (File. filename))
    (reduce
     process-line
     {:rules {} :updates []}
     (filter not-blank? (str/split-lines (slurp filename))))))

(read-input "test.txt")
