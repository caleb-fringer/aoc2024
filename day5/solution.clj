(ns aoc2024.day5.solution
  (:import (java.io File))
  (:require [clojure.string :as str]))

(defn parse-input [filename]
  (when (File. exists
    (str/split-lines (slurp filename))))

(parse-input "test.txt")
