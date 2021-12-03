(ns aoc21.day01-test
  (:require [clojure.test :refer :all]
            [aoc21.day01 :as sut]))

(deftest get-day01-answer-pt-1-test
  (testing "test day 01"
    (is (= 1266 (sut/get-day01-answer-pt-1)))))

(deftest get-day01-answer-pt-2-test
  (testing "test day 01"
    (is (= 1217 (sut/get-day01-answer-pt-2)))))
