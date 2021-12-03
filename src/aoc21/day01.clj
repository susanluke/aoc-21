(ns aoc21.day01
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def f "day-01-input.txt")

(defn parse-file []
  (->> (-> f
           io/resource
           slurp
           string/split-lines)
       (map read-string)))

(defn get-num-increases [l]
  (->> l
       (partition 2 1)
       (map #(apply < %))
       (filter identity)
       count))

(defn get-num-increases-by-window [w l]
  (let [step 1]
    (->> l
         (partition w step)
         (map #(apply + %))
         (get-num-increases))))

(defn get-day01-answer-pt-1 []
  (get-num-increases (parse-file)))

(defn get-day01-answer-pt-2 []
  (get-num-increases-by-window 3 (parse-file)))
