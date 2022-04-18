(ns aoc21.day23-test
  (:require [clojure.test :refer :all]
            [aoc21.day23 :as sut]))

(comment
  "Part 1
#############
#...........#
###B#C#B#D###
  #A#D#C#A#
  #########
")

(def grid-state-pt1
  [(repeat 13 nil)
   [nil nil nil :b nil :c nil :b nil :d nil nil nil]
   [nil nil nil :a nil :d nil :c nil :a nil nil nil]])

(def grid-info-pt1 {:num-rows           3
                    :num-cols           13
                    :home-rows          [1 2]
                    :home-cols          {:a 3 :b 5 :c 7 :d 9}
                    :corridor-row       0
                    :corridor-rest-cols [1 2 4 6 8 10 11]
                    :move-costs         {:a 1 :b 10 :c 100 :d 1000}})

(comment
  "Part 2
#############
#...........#
###B#C#B#D###
  #D#C#B#A#
  #D#B#A#C#
  #A#D#C#A#
  #########")

(def grid-state-pt2
  [(repeat 13 nil)
   [nil nil nil :b nil :c nil :b nil :d nil nil nil]
   [nil nil nil :d nil :c nil :b nil :a nil nil nil]
   [nil nil nil :d nil :b nil :a nil :c nil nil nil]
   [nil nil nil :a nil :d nil :c nil :a nil nil nil]])

(def grid-info-pt2 {:num-rows           5
                    :num-cols           13
                    :home-rows          [1 2 3 4]
                    :home-cols          {:a 3 :b 5 :c 7 :d 9}
                    :corridor-row       0
                    :corridor-rest-cols [1 2 4 6 8 10 11]
                    :move-costs         {:a 1 :b 10 :c 100 :d 1000}})

(deftest get-all-behind-of-type?-test
  ;; Simple test cases
  (is (= false (sut/all-behind-of-type? [[nil] [:a] [:b] [:c]] {:home-rows [1 2 3]} :a [0 0])))
  (is (= true (sut/all-behind-of-type? [[nil] [:a] [:a] [:a]] {:home-rows [1 2 3]} :a [0 0])))

  ;; Pt1 test cases
  (is (= true (sut/all-behind-of-type? grid-state-pt1 grid-info-pt1 :a [1 3])))
  (is (= false (sut/all-behind-of-type? grid-state-pt1 grid-info-pt1 :b [1 5])))
  (is (= true (sut/all-behind-of-type? grid-state-pt1 grid-info-pt1 :c [1 7])))
  (is (= false (sut/all-behind-of-type? grid-state-pt1 grid-info-pt1 :d [1 9])))

  ;; Pt2 test cases
  (is (= false (sut/all-behind-of-type? grid-state-pt2 grid-info-pt2 :a [1 3])))
  (is (= true (sut/all-behind-of-type? (-> grid-state-pt2
                                           (assoc-in [2 3] :a)
                                           (assoc-in [3 3] :a)
                                           (assoc-in [4 3] :a))
                                       grid-info-pt2 :a [1 3])))
  (is (= true (sut/all-behind-of-type? (-> grid-state-pt2
                                           (assoc-in [2 5] :b)
                                           (assoc-in [3 5] :b)
                                           (assoc-in [4 5] :b))
                                       grid-info-pt2 :b [1 5]))))

(deftest find-amphipod-locations-test
  (testing "Part 1 cases"
    (is (= [[2 3] [2 9]]
           (sut/find-amphipod-locations grid-state-pt1 grid-info-pt1 :a)))
    (is (= [[1 3] [1 7]]
           (sut/find-amphipod-locations grid-state-pt1 grid-info-pt1 :b)))
    (is (= [[1 5] [2 7]]
           (sut/find-amphipod-locations grid-state-pt1 grid-info-pt1 :c)))
    (is (= [[1 9] [2 5]]
           (sut/find-amphipod-locations grid-state-pt1 grid-info-pt1 :d))))

  (testing "Part 2 cases"
    (is (= [[2 9] [3 7] [4 3] [4 9]]
           (sut/find-amphipod-locations grid-state-pt2 grid-info-pt2 :a)))
    (is (= [[1 3] [1 7] [2 7] [3 5]]
           (sut/find-amphipod-locations grid-state-pt2 grid-info-pt2 :b)))
    (is (= [[1 5] [2 5] [3 9] [4 7]]
           (sut/find-amphipod-locations grid-state-pt2 grid-info-pt2 :c)))
    (is (= [[1 9] [2 3] [3 3] [4 5]]
           (sut/find-amphipod-locations grid-state-pt2 grid-info-pt2 :d)))))

(deftest home-locations
  (testing "Part 1 cases"
    (is (= [[1 3] [2 3]]
           (sut/home-locations grid-info-pt1 :a)))
    (is (= [[1 5] [2 5]]
           (sut/home-locations grid-info-pt1 :b)))
    (is (= [[1 7] [2 7]]
           (sut/home-locations grid-info-pt1 :c)))
    (is (= [[1 9] [2 9]]
           (sut/home-locations grid-info-pt1 :d))))
  (testing "Part 2 cases"
    (is (= [[1 3] [2 3] [3 3] [4 3]]
           (sut/home-locations grid-info-pt2 :a)))
    (is (= [[1 5] [2 5] [3 5] [4 5]]
           (sut/home-locations grid-info-pt2 :b)))
    (is (= [[1 7] [2 7] [3 7] [4 7]]
           (sut/home-locations grid-info-pt2 :c)))
    (is (= [[1 9] [2 9] [3 9] [4 9]]
           (sut/home-locations grid-info-pt2 :d)))))
