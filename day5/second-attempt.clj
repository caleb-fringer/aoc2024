(ns aoc2024.day5.solution
  (:require
   [clojure.set :refer [intersection]]
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

(defn set-conj [s x]
  (conj (or s #{}) x))

(defn process-line [rules-and-updates line]
  (if-let [[x y] (rule? line)]
    (update-in rules-and-updates [:rules x] set-conj y)
    (update-in rules-and-updates [:updates] conj (update? line))))

(defn read-input [filename]
  (when (.exists (File. filename))
    (reduce
     process-line
     {:rules {} :updates []}
     (filter not-blank? (str/split-lines (slurp filename))))))

(def input (read-input "test.txt"))

(defn page-is-valid? [prev-pages page-dependencies]
    (= #{} (intersection prev-pages page-dependencies)))

(def first-update (first (:updates input)))

(defn subvec-value [v k]
  (subvec v 0 (.indexOf v k)))

(defn update-is-valid? [update-list rules]
  (reduce 'and (for [curr (reverse update-list)]
                (page-is-valid? 
                  (set (subvec-value update-list curr))
                  rules))))

(for [update-list (reverse (get-in input [:updates 0]))
      curr update-list
      :let [rules (:rules input)]]
  (page-is-valid? 
    (set (subvec-value update-list curr))
    rules))

(for [update-list (:updates input)]
  (update-is-valid? update-list (:rules input))) ; (true true true true true true)
