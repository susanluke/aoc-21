(ns aoc21.day05
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def f "day-05-input.txt")
(def data (slurp (io/resource f)))

(defn parse-line [s]
  (let [[_ x1 y1 x2 y2] (re-matches #"(\d+),(\d+) -> (\d+),(\d+)" s)]
    {:x1 (read-string x1) :y1 (read-string y1)
     :x2 (read-string x2) :y2 (read-string y2)}))

(defn parse-string [s]
  (->> (string/split-lines s)
       (map parse-line)))

(defn update-grid [grid [x y]]
  (update-in grid [y x] inc))

(defn h-or-v? [{:keys [x1 y1 x2 y2]}]
  (or (= x1 x2)
      (= y1 y2)))

(defn coord-range [p1 p2]
  (update-in (vec (sort [p1 p2])) [1] inc))

(defn mark-grid [coords]
  (let [x-max       (apply max (concat (map :x1 coords)
                                       (map :x2 coords)))
        y-max       (apply max (concat (map :y1 coords)
                                       (map :y2 coords)))
        row         (vec (repeat (inc x-max) 0))
        grid        (vec (repeat (inc y-max) row))]
    (loop [grid   grid
           coords coords]
      (if (empty? coords)
        grid
        (let [{:keys [x1 y1 x2 y2]} (first coords)
              points                (if (h-or-v? (first coords))
                                      (for [x (apply range (coord-range x1 x2))
                                            y (apply range (coord-range y1 y2))]
                                        [x y])
                                      (map list
                                           (range x1
                                                  (if (< x1 x2) (inc x2) (dec x2))
                                                  (if (< x1 x2) 1 -1))
                                           (range y1
                                                  (if (< y1 y2) (inc y2) (dec y2))
                                                  (if (< y1 y2) 1 -1))))]
          (recur (reduce update-grid grid points)
                 (rest coords)))))))

(defn count-greater-than-2 [grid-marked]
  (->> (flatten grid-marked)
       (filter (partial < 1))
       count))

(defn get-day05-answer-pt1 [s]
  (let [coords      (->> (parse-string s)
                         (filter h-or-v?))]
    (-> (mark-grid coords)
        count-greater-than-2)))

(defn get-day05-answer-pt2 [s]
  (let [coords      (parse-string s)]
    (-> (mark-grid coords)
        count-greater-than-2)))
