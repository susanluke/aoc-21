(ns aoc21.day05-test
  (:require [clojure.test :refer :all]
            [aoc21.day05 :as sut]))

(def test-data
  "0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2")

(deftest get-day05-answer-pt1-test
  (testing "Testing day 05"
    (is (= 5 (sut/get-day05-answer-pt1 test-data)))
    (is (= 5084 (sut/get-day05-answer-pt1 sut/data)))))

(deftest get-day05-answer-pt2-test
  (testing "Testing day 05 pt 2"
    (is (= 12 (sut/get-day05-answer-pt2 test-data)))
    (is (= 17882 (sut/get-day05-answer-pt2 sut/data)))))
