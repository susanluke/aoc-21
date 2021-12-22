(ns aoc21.day09
  (:require
   [clojure.string :as string]))

(defn parse-line [s]
  (vec (map (comp read-string str) s)))

(defn parse-raw-data [s]
  (->> (string/split-lines s)
       (map parse-line)
       vec))

(defn get-xy [m [x y]]
  (-> (nth m y) (nth x)))

(defn neighbours [[dim-x dim-y] [x y]]
  (->> (map (fn [[xn yn]]
              [(+ x xn) (+ y yn)])
            [[0 -1] [-1 0] [1 0] [0 1]])
       (filter (fn [[xn yn]]
                 (and (<= 0 xn (dec dim-x))
                      (<= 0 yn (dec dim-y)))))))

(defn neighbours-higher? [m dims coords]
  (let [neighbour-vals (map #(get-xy m %) (neighbours dims coords))]
    (every? #(< (get-xy m coords) %) neighbour-vals)))

(defn score [m dims coords]
  (if (neighbours-higher? m dims coords)
    (inc (get-xy m coords))
    0))

(defn day09-answer-pt1 [s]
  (let [data                   (parse-raw-data s)
        [dim-x dim-y :as dims] [(count (first data)) (count data)]]
    (->> (for [x (range dim-x)
               y (range dim-y)]
           (score data dims [x y]))
         (apply +))))

(defn basin-search [data dims state coords]
  (if (or (= 9 (get-xy data coords))
          ((:searched state) coords))
    state
    (reduce (partial basin-search data dims)
            (-> state
                (update :searched conj coords)
                (update :current-basin conj coords))
            (neighbours dims coords))))

(defn basin-search-top-level [data dims state coords]
  (let [new-state (basin-search data dims state coords)]
    (-> new-state
        (update :basins conj (:current-basin new-state))
        (assoc :current-basin #{}))))

(defn day09-answer-pt2 [s]
  (let [data (parse-raw-data s)
        [dim-x dim-y :as dims] [(count (first data)) (count data)]
        basin-data (reduce (partial basin-search-top-level data dims)
                           {:searched #{}
                            :basins '()
                            :current-basin #{}}
                           (for [x (range dim-x)
                                 y (range dim-y)]
                             [x y]))]
    (->> (:basins basin-data)
         (map count)
         (sort >)
         (take 3)
         (apply *))))
