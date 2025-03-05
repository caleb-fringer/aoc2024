(ns aoc2024.day5.solution
  (:require [clojure.java.io :refer [reader]]
            [clojure.string :refer [blank?]])
  (:import [java.io File]))
; X|Y means X must come before Y
; We're given a list of rules and several lists of pages ("updates")
; It seems each update is independent from the other updates
; For each "update", we need to determine if it is in the correct order
; This means ensuring that there are no pages that violate any rules

; Idea: Initially, we can have any number in the first position. The rules
; describe the set of possible numbers that cannot come next. So, create a map
; with the RHS of each rule as the key and a list containing all the LHS for that
; rule. Iterate over the list while maintaining a set of numbers that cannot come
; next in the list. For each number n in the list, we see if n is part of this set.
; If so, the list is invalid. Otherwise, we lookup n in the rules map and add all
; of its associated values to the set of excluded values.

; EX: From the test.txt input, consider the first update: 75,47,61,53,29
; excluded: {}
; n=0: list[n] = 75 => 75 is not in excluded, so we add its associations to
; excluded: {97}
; n=1: list[n] = 47 => 47 is not in excluded, add associations:
; excluded: {97, 75}
; n=2: list[n] = 61 => 61 is not in excluded, ...
; excluded: {97, 75, 47}
; ...

; EX: Sixth update: 97,13,75,29,47
; excluded: {}
; n=0: list[n] = 97
; excluded: {}
; n=1: list[n] = 13 => add to excluded
; excluded: {97, 61, 29, 47, 75, 53}
; n=2: list[n] = 75 => 75 is in excluded, fail out
(defn non-blank? [s]
  (not (blank? s)))

(defn extract-rule [v]
  (mapv #(parse-long %) (rest v)))

(merge-with into rules (apply hash-map (re-matches #"(\d+)\|(\d+)" %)))

(with-open [rdr (reader "test.txt")]
  (let [rules '()
        updates '()]
    (->>
     (line-seq rdr)
     (filter non-blank?))))
          ; If the line matches the first form, extract the numbers and insert
          ; them into a hashmap of rules
          ; If the line matches the second form, extract the the numbers into
          ; a list of lists (each line is a list)

