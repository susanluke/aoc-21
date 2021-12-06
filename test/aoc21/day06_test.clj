(ns aoc21.day06-test
  (:require [clojure.test :refer :all]
            [aoc21.day06 :as sut]))

(def test-string "3,4,3,1,2")

(deftest get-day06-answer
  (testing "Testing day 06"
    (is (= 26 (sut/get-day06-answer test-string 18)))
    (is (= 5934 (sut/get-day06-answer test-string 80)))
    (is (= 393019 (sut/get-day06-answer sut/data-string 80)))
    (is (= 1757714216975 (sut/get-day06-answer sut/data-string 256)))))
