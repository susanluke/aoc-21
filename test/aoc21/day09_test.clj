(ns aoc21.day09-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [aoc21.day09 :as sut]))

(def f "day-09-input.txt")
(def raw-data (slurp (io/resource f)))

(def test-raw-data
  "2199943210
3987894921
9856789892
8767896789
9899965678")

(deftest get-day08-answer-pt1-test
  (testing "Test day 09 pt 1"
    (is (= 15 (sut/day09-answer-pt1 test-raw-data)))
    (is (= 504 (sut/day09-answer-pt1 raw-data)))))

(deftest get-day08-answer-pt1-test
  (testing "Test day 09 pt 1"
    (is (= 1134 (sut/day09-answer-pt2 test-raw-data)))
    (is (= 1558722 (sut/day09-answer-pt2 raw-data)))))
