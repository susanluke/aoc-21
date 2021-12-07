(ns aoc21.day07-test
  (:require [clojure.test :refer :all]
            [aoc21.day07 :as sut]))

(def test-string "16,1,2,0,4,2,7,1,2,14")

(deftest get-day07-answer-pt1-test
  (testing "Testing day 07 pt 1"
    (is (= 37 (sut/get-day07-answer-pt1 test-string)))
    (is (= 340052 (sut/get-day07-answer-pt1 sut/data-string)))))

(deftest get-day07-answer-pt2-test
  (testing "Testing day 07 pt 1"
    (is (= 168 (sut/get-day07-answer-pt2 test-string)))
    (is (= 92948968 (sut/get-day07-answer-pt2 sut/data-string)))))
