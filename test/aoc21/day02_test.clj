(ns aoc21.day02-test
  (:require [clojure.test :refer :all]
            [aoc21.day02 :as sut]))

(deftest get-day02-answer-pt1-test
  (testing "testing day 02"
    (is (= 1484118 (sut/get-day02-answer-pt1)))
    (is (= 1463827010 (sut/get-day02-answer-pt2)))))
