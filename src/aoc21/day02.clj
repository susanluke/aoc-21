(ns aoc21.day02
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def f "day-02-input.txt")

(defn parse-line [s]
  (let [[op n] (string/split s #"\s")]
    [(keyword op) (read-string n)]))

(defn parse-file []
  (->> (-> f io/resource slurp string/split-lines)
       (map parse-line)))

(defn move [[h d] [op n]]
  (case op
    :forward [(+ h n) d]
    :up      [h (- d n)]
    :down    [h (+ d n)]))

(defn move-aim [[h d a] [op n]]
  (case op
    :forward [(+ h n) (+ d (* a n)) a]
    :up      [h d (- a n)]
    :down    [h d (+ a n)]))

(defn get-day02-answer-pt1 []
  (->> (reduce move [0 0] (parse-file))
       (apply *)))

(defn get-day02-answer-pt2 []
  (let [[h d a] (reduce move-aim [0 0 0] (parse-file))]
    (* h d)))
