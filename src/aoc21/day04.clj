(ns aoc21.day04
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def f "day-04-input.txt")
(def data (slurp (io/resource f)))

(defn parse-board-row [s]
  (map read-string (string/split (string/trim s) #"\s+")))

(defn parse-board-string [s]
  (->> (-> s
           string/split-lines)
       (map parse-board-row)))

(defn transpose [xs]
  (apply map list xs))

(defn parse-string [s]
  (let [data          (string/split s #"\n\n")
        numbers (->> (-> (first data)
                         (string/split #","))
                     (map read-string))
        boards    (->> (rest data)
                       (map parse-board-string)
                       (map (fn [b] {:board b})))]
    [numbers boards]))

(defn board->draws [b ns]
  (map #(map ns %) b))

(defn winning-draw [mb]
  (apply min (map #(apply max %) mb)))

(defn winning-draw-rows-and-cols [b]
  (min (winning-draw b) (winning-draw (transpose b))))

(defn score-board [{:keys [board marked winning-draw]}]
  (let [lin-board        (flatten board)
        lin-marks        (flatten marked)
        unmarked-sum     (apply + (map #(if (< winning-draw %2) %1 0) lin-board lin-marks))
        winning-draw-val (->> (map list lin-board lin-marks)
                              (reduce #(when (= winning-draw (second %2))
                                         (reduced (first %2)))))]
    (* winning-draw-val unmarked-sum)))

(defn get-ordered-boards [s]
  (let [[bingo-numbers boards] (parse-string s)
        bingo-order            (->> (map-indexed #(list %2 %1) bingo-numbers)
                                    (map vec)
                                    vec
                                    (into {}))
        marked-boards          (map #(assoc % :marked
                                            (board->draws (:board %) bingo-order))
                                    boards)]
    (->> (map #(assoc % :winning-draw
                      (winning-draw-rows-and-cols (:marked %)))
              marked-boards)
         (sort-by :winning-draw))))


;;44088
(defn get-day04-answer-pt1 [s]
  (score-board (first (get-ordered-boards s))))

(defn get-day04-answer-pt2 [s]
  (score-board (last (get-ordered-boards s))))
