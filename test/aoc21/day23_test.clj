(ns aoc21.day23-test
  (:require [clojure.test :refer :all]
            [aoc21.day23 :as sut]))

(def grid-state-pt1
  [(repeat 13 nil)
   [nil nil nil :b nil :c nil :b nil :d nil nil nil]
   [nil nil nil :a nil :d nil :c nil :a nil nil nil]])

(def grid-info-pt1 {:move-costs         {:a 1 :b 10 :c 100 :d 1000}
                    :home-rows          [1 2]
                    :home-cols          {:a 3 :b 5 :c 7 :d 9}
                    :corridor-row       0
                    :corridor-rest-cols [1 2 4 6 8 10 11]})

(deftest get-all-behind-of-type?-test
  (is (= false (sut/all-behind-of-type? [[nil] [:a] [:b] [:c]] {:home-rows [1 2 3]} :a [0 0])))
  (is (= true (sut/all-behind-of-type? [[nil] [:a] [:a] [:a]] {:home-rows [1 2 3]} :a [0 0])))
  (is (= true (sut/all-behind-of-type? grid-state-pt1 grid-info-pt1 :a [1 3])))
  (is (= false (sut/all-behind-of-type? grid-state-pt1 grid-info-pt1 :b [1 5])))
  (is (= true (sut/all-behind-of-type? grid-state-pt1 grid-info-pt1 :c [1 7])))
  (is (= false (sut/all-behind-of-type? grid-state-pt1 grid-info-pt1 :d [1 9]))))
