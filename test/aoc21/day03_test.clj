(ns aoc21.day03-test
  (:require [clojure.test :refer :all]
            [aoc21.day03 :as sut]))

(deftest get-day03-answer-test
  (testing "testing day 03"
    (is (= 1131506 (sut/get-day03-answer-pt1)))))
