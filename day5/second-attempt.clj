(ns aoc2024.day5.solution
  (:require
   [clojure.string :as str])
  (:import
   [java.io File]))

(defn not-blank? [s]
  (not (str/blank? s)))

(defn rule? [line]
  (when-let [rule (re-matches #"(\d+)\|(\d+)" line)]
    (mapv parse-long (rest rule))))

(defn update? [line]
  (when (re-matches #"\d+(?:,\d+)*" line)
    (mapv parse-long (re-seq #"\d+" line))))

(defn process-line [rules-and-updates line]
  (if-let [[x y] (rule? line)]
    (assoc-in rules-and-updates [:rules x] y)
    (update-in rules-and-updates [:updates] conj (update? line))))

(defn read-input [filename]
  (when (.exists (File. filename))
    (reduce
     process-line
     {:rules {} :updates []}
     (filter not-blank? (str/split-lines (slurp filename))))))

(def input (read-input "test.txt"))
