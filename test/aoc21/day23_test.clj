(ns aoc21.day23-test
  (:require [clojure.test :refer :all]
            [aoc21.day23 :as sut]))

(def initial-state
  [(repeat 13 nil)
   (repeat 13 nil)
   [nil nil nil :b nil :c nil :b nil :d nil nil nil]
   [nil nil nil :a nil :d nil :c nil :a nil nil nil]
   (repeat 13 nil)])

(deftest get-all-behind-of-type?-test
  (is (= false (sut/all-behind-of-type? [[nil] [:a] [:b] [:c]] :a [0 0])))
  (is (= true (sut/all-behind-of-type? [[nil] [:a] [:a] [:a]] :a [0 0]))))
