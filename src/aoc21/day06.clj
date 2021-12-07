(ns aoc21.day06
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def f "day-06-input.txt")
(def data-string (slurp (io/resource f)))
(def post-lay 6)
(def post-birth 8)
(def cycle-length (inc (max post-lay post-birth)))

(defn add-missing-frequency [fs n]
  (if (get fs n) fs (assoc fs n 0)))

(defn data->state [d]
  (->> (reduce add-missing-frequency
               (frequencies d)
               (range cycle-length))
       (into [])
       (sort-by first)
       (map second)))

(defn simulate-day [state]
  (let [lays (first state)]
    (-> (vec (rest state))
        (conj 0)
        (update post-lay + lays)
        (update post-birth + lays))))

(defn get-day06-answer [ds days]
  (->> (string/split ds #",")
       (map read-string)
       data->state
       (iterate simulate-day)
       (drop days)
       first
       (apply +)))
