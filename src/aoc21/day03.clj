(ns aoc21.day03
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.core.matrix :as m]))

(def f "day-03-input.txt")
(def numeric-offset 48)

(defn parse-file []
  (->> (-> f io/resource slurp string/split-lines vec)
       (mapv #(mapv (fn [c] (Character/digit c 2)) %))))

(defn binary-vec->int [cs]
  (reduce #(+ (* 2 %) %2) cs))

(defn get-gamma-epsilon-bits [data]
  (let [num-lines  (count data)
        threshold  (/ num-lines 2)
        bit-counts (->> data
                        m/transpose
                        (m/slice-map m/esum))
        epsilon    (map #(if (< threshold %) 1 0) bit-counts)
        gamma      (map #(if (> threshold %) 1 0) bit-counts)]
    [gamma epsilon]))

(defn get-day03-answer-pt1 []
  (->> (parse-file)
       get-gamma-epsilon-bits
       (map binary-vec->int)
       (m/ereduce *)))

(defn get-most-popular-bit-at-n [data n]
  (let [threshold (/ (count data) 2)
        total (apply + (map #(nth % n) data))]
    (if (<= threshold total) 1 0)))

(defn matches-bit-at-posn? [l n b]
  (= b (nth l n)))

(defn get-rating [data popularity]
  (let [filter-fn (if (= :most-popular popularity)
                    filter
                    remove)]
    (loop [data data
           n    0]
      (let [b             (get-most-popular-bit-at-n data n)
            filtered-data (filter-fn #(matches-bit-at-posn? % n b) data)]
        (if (= 1 (count filtered-data))
          (first filtered-data)
          (recur filtered-data (inc n)))))))

(defn get-day3-answer-pt2 []
  (let [data (parse-file)]
    (->> [(get-rating data :most-popular)
          (get-rating data :least-popular)]
         (map binary-vec->int)
         (apply *))))
