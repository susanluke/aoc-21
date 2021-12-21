(ns aoc21.day08
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]))

(def f "day-08-input.txt")
(def raw-data (slurp (io/resource f)))

;; digits with a unique number of segments
(def easy-to-match #{2 4 3 7})

(defn parse-word [s]
  (->> (map (comp keyword str) s)
       (into #{})))

(defn parse-io [s]
  (->> (string/split s #"\s")
       (map parse-word)))

(defn parse-line [s]
  (let [[_ input output] (re-matches #"^(.*) \| (.*)$" s)]
    {:input  (parse-io input)
     :output (parse-io output)}))

(defn parse-raw-data [s]
  (map parse-line (string/split-lines s)))

(defn get-day08-answer-pt1 [s]
  (let [data (parse-raw-data s)]
    (->> (map (comp (partial filter easy-to-match)
                    (partial map count)
                    :output)
              data)
         flatten
         count)))

(defn sets-with-n-segs [combs n]
  (let [results (filter #(= n (count %)) combs)]
    (if (= (count results) 1)
      (first results) ; only return the set
      results))) ; return a list of sets

(defn match-numbers [combs]
  (let [one       (sets-with-n-segs combs 2)
        four      (sets-with-n-segs combs 4)
        seven     (sets-with-n-segs combs 3)
        eight     (sets-with-n-segs combs 7)
        six-segs  (sets-with-n-segs combs 6)
        ;; Of the 6-segment digits (0,6,9)...
        six       (->> six-segs
                       ;; ...only 6 has segments which don't have 1's as a subset
                       (remove (partial set/subset? one))
                       first)
        nine      (->> (sets-with-n-segs combs 6)
                       ;; ...only 9 has segments with 4's as a subset
                       (filter (partial set/subset? four))
                       first)
        zero      (->> six-segs
                       ;; ...the one that isn't 6 or 9, must be 0
                       (remove #(or (= six %) (= nine %)))
                       first)
        ;; Of the 5-segment digits (2,3,5)...
        five-segs (sets-with-n-segs combs 5)
        three     (->> five-segs
                       ;; ...only 3 has segments with 1's as as subset
                       (filter (partial set/subset? one))
                       first)
        five      (->> five-segs
                       ;; ...only 5's segments are a subset of 6's
                       (filter #(set/subset? % six))
                       first)
        two       (->> five-segs
                       ;; ...the one that isn't 3 or 5, must be 2
                       (remove #(or (= three %) (= five %)))
                       first)]
    {0 zero 1 one 2 two   3 three 4 four
     5 five 6 six 7 seven 8 eight 9 nine}))

(defn digits->int [ds] (reduce #(+ (* 10 %) %2) ds))

(defn process-line [{:keys [input output]}]
  (let [digit->segs   (match-numbers input)
        segs->digit   (set/map-invert digit->segs)
        output-digits (map segs->digit output)]
    (digits->int output-digits)))

(defn get-day08-answer-pt2 [s]
  (let [data    (parse-raw-data s)
        results (map process-line data)]
    (apply + results)))
