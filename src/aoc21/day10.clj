(ns aoc21.day10
  (:require [clojure.string :as string]))

(defn parse-raw-data [s]
  (string/split-lines s))

(def brackets
  {\{ \} \[ \] \( \) \< \>})

(def bracket-syntax-scores
  {\) 3 \] 57 \} 1197 \> 25137})

(def bracket-closer-scores
  {\( 1 \[ 2 \{ 3 \< 4})

(defn syntax-error-score
  ([s] (syntax-error-score '() s))
  ([openers s]
   (if (empty? s)
     {:score 0
      :openers openers}
     (let [next-char (first s)]
       (cond
         (and (not-empty openers)
              (= (brackets (first openers)) next-char))
         (syntax-error-score (rest openers) (rest s))

         (brackets next-char)
         (syntax-error-score (conj openers next-char) (rest s))

         :else
         {:score (bracket-syntax-scores next-char)})))))

(defn digits->closer-score [ds] (reduce #(+ (* 5 %) %2) ds))

(defn median [l]
  (nth (sort l)
       (int (/ (count l) 2))))

(defn day10-answer-pt1 [s]
  (->> (parse-raw-data s)
       (map syntax-error-score)
       (map :score)
       (apply +)))

(defn day10-answer-pt2 [s]
  (->> (parse-raw-data s)
       (map syntax-error-score)
       (filter #(= (:score %) 0))
       (map :openers)
       (map #(map bracket-closer-scores %))
       (map digits->closer-score)
       median))
