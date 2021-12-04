(ns aoc21.day03
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.core.matrix :as m]))

(def f "day-03-input.txt")

(defn parse-file []
  (->> (-> f io/resource slurp string/split-lines vec)
       (mapv #(mapv (fn [c] (Character/digit c 2)) %))))

(defn binary-char-vec->int [cs]
  (->> cs
       (apply str)
       (str "2r")
       read-string))

(defn get-day03-answer-pt1 []
  (let [data       (parse-file)
        num-lines  (count data)
        threshold  (/ num-lines 2)
        bit-counts (->> data
                        m/transpose
                        (m/slice-map #(apply + %)))
        epsilon    (->> bit-counts
                        (map #(if (< threshold %) \1 \0))
                        binary-char-vec->int)
        gamma      (->> bit-counts
                        (map #(if (> threshold %) \1 \0))
                        binary-char-vec->int)]
    (* gamma epsilon)))
