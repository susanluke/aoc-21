(ns aoc21.day07
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def f "day-07-input.txt")
(def data-string (slurp (io/resource f)))

(defn parse-string [s]
  (map read-string (string/split s #",")))

(defn median [l]
  (nth (sort l)
       (int (/ (count l) 2))))

(defn avg [l]
  (int (/ (apply + l)
          (count l))))

(defn abs [n] (max n (- n)))

(defn constant-fuel [m l]
  (->> (map #(abs  (- m %)) l)
       (apply +)))

(defn triangle [n]
  (/ (* n (inc n)) 2))

(defn triangle-fuel [m l]
  (->> (map #(abs  (- m %)) l)
       (map triangle)
       (apply +)))

(defn get-day07-answer-pt1 [s]
  (let [l (parse-string s)]
    (constant-fuel (median l) l)))

(defn get-day07-answer-pt2 [s]
  (let [l (parse-string s)
        m (avg l)]
    ;; good enough to pass test
    (min (triangle-fuel (dec m) l)
         (triangle-fuel m l)
         (triangle-fuel (inc m) l))))
