(ns aoc21.day10-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [aoc21.day10 :as sut]))

(def f "day-10-input.txt")
(def raw-data (slurp (io/resource f)))

(def test-raw-data
  "[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]")

(deftest get-day08-answer-pt1-test
  (testing "Test day 09 pt 1"
    (is (= 26397 (sut/day10-answer-pt1 test-raw-data)))
    (is (= 362271 (sut/day10-answer-pt1 raw-data)))))

(deftest get-day08-answer-pt2-test
  (testing "Test day 09 pt 1"
    (is (= 288957 (sut/day10-answer-pt2 test-raw-data)))
    (is (= 1698395182 (sut/day10-answer-pt2 raw-data)))))
