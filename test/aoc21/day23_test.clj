(ns aoc21.day23-test
  (:require [clojure.test :refer :all]
            [aoc21.day23 :as sut]))


;;TODO - we have padding on state that we don't need
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

(def grid-state-pt1-interim
  [[nil nil :b nil nil nil nil nil nil nil nil :d nil]
   [nil nil nil nil nil :c nil :b nil nil nil nil nil]
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

(def grid-info-pt2 (-> grid-info-pt1
                       (assoc :num-rows 5)
                       (assoc :home-rows [1 2 3 4])))

(deftest get-all-behind-of-type?-test
  ;; Simple test cases
  (is (= false (sut/all-behind-of-type? [[nil] [:a] [:b] [:c]] {:home-rows [1 2 3]} :a [0 0])))
  (is (= true (sut/all-behind-of-type? [[nil] [:a] [:a] [:a]] {:home-rows [1 2 3]} :a [0 0])))

  ;; Pt1 test cases
  (are [tf amphi-type loc]
      (= tf (sut/all-behind-of-type? grid-state-pt1 grid-info-pt1 amphi-type loc))
    true :a [1 3]
    false :b [1 5]
    true :c [1 7]
    false :d [1 9])

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

;; TODO - could complete test cases, but reasonable selection covered
(deftest dist-to-get-amphipod-top-of-home-col
  (testing "Part 1 cases"
    (are
        [dist amphi-type loc]
        (= dist (sut/dist-to-get-amphipod-top-of-home-col grid-state-pt1 grid-info-pt1 amphi-type loc))

      8 :a [2 9] ;; up 2 accross 6
      0 :a [2 3] ;; already in correct position
      3 :b [1 3] ;; up 1 accross 2
      3 :b [1 7] ;; up 1 across 2
      3 :c [1 5] ;; in correct position, but incoreect beneath -> has to move out of the way.  up 1, back and forth 2
      0 :c [2 7] ;; already in correct position
      3 :d [1 9] ;; correct posn, but incorrect beneath -> up 1, back and forth 2
      6 :d [2 5] ;; up 2 accross 2
      ))
  (testing "Part 1 interim case"
    (is (= 3 (sut/dist-to-get-amphipod-top-of-home-col grid-state-pt1-interim grid-info-pt1 :b [0 2])))
    (is (= 2 (sut/dist-to-get-amphipod-top-of-home-col grid-state-pt1-interim grid-info-pt1 :d [0 11]))))
  (testing "Part 2 cases"
    (are
        [dist amphi-type loc]
        (= dist (sut/dist-to-get-amphipod-top-of-home-col grid-state-pt2 grid-info-pt2 amphi-type loc))

      8 :a [2 9] ;; up 2 accross 6
      7 :a [3 7] ;; up 3 across 4
      0 :a [4 3] ;; already in correct position
      10 :a [4 9]   ;; up 4 accross 6
      )))

(deftest find-amphipod-locations-test
  (testing "Part 1 cases"
    (are [expected amphi-type]
        (= expected
           (sut/find-amphipod-locations grid-state-pt1 grid-info-pt1 amphi-type))
      [[2 3] [2 9]] :a
      [[1 3] [1 7]] :b
      [[1 5] [2 7]] :c
      [[1 9] [2 5]] :d))

  (testing "Part 2 cases"
    (are [expected amphi-type]
        (= expected
           (sut/find-amphipod-locations grid-state-pt2 grid-info-pt2 amphi-type))
      [[2 9] [3 7] [4 3] [4 9]] :a
      [[1 3] [1 7] [2 7] [3 5]] :b
      [[1 5] [2 5] [3 9] [4 7]] :c
      [[1 9] [2 3] [3 3] [4 5]] :d)))

(deftest home-locations
  (testing "Part 1 cases"
    (are [expected amphi-type]
        (is (= expected
               (sut/home-locations grid-info-pt1 amphi-type)))
      [[1 3] [2 3]] :a
      [[1 5] [2 5]] :b
      [[1 7] [2 7]] :c
      [[1 9] [2 9]] :d))
  (testing "Part 2 cases"
    (are [expected amphi-type]
        (is (= expected
               (sut/home-locations grid-info-pt2 amphi-type)))
      [[1 3] [2 3] [3 3] [4 3]] :a
      [[1 5] [2 5] [3 5] [4 5]] :b
      [[1 7] [2 7] [3 7] [4 7]] :c
      [[1 9] [2 9] [3 9] [4 9]] :d)))


(deftest num-move-in-corridor
  (are [expected amphi-type]
      (= expected
         (sut/num-move-in-corridor grid-state-pt1 grid-info-pt1 amphi-type))
    1 :a
    2 :b
    1 :c
    2 :d)
  (are [expected amphi-type]
      (= expected
         (sut/num-move-in-corridor grid-state-pt2 grid-info-pt2 amphi-type))
    3 :a
    4 :b
    3 :c
    4 :d))

(deftest dist-to-get-amphipods-home
  (testing "Pt1 cases"
    (are [expected amphi-type]
        (= expected
           (sut/dist-to-get-amphipods-home grid-state-pt1 grid-info-pt1 amphi-type))
      9 :a                  ;; 2U+6L+1D
      9 :b                  ;; 1U+2L+2D, 1U+2R+1D
      4 :c                  ;; 1U+2A+1D
      12 :d                 ;; 1U+1L+1R+2D, 2U+4R+1D
      ))
  (testing "Pt2 cases"
    (are [expected amphi-type]
        (= expected
           (sut/dist-to-get-amphipods-home grid-state-pt2 grid-info-pt2 amphi-type))
      31 :a                 ;; 2U+6L+3D, 3U+4D+2D, 4U+6A+1D = 11+9+11 = 31
      25 :b                 ;; 1U+2L+4D, 3U+1L+1R+3D, 1U+2L+2D, 2U+2L+1D = 7+8+5+5 = 25
      18 :c                 ;; 1U+2R+3D, 2U+2R+2D, 3U+2L+1D = 6+6+6 = 18
      38 :d                 ;; 1U+1R+1L+4D, 2U+6R+3D, 3U+6R+2D, 4U+4R+1D = 7+11+11+9 = 38
      )))

(deftest cost-to-get-amphipods-home
  (testing "Pt1 cases"
    (are [expected amphi-type]
        (= expected (sut/cost-to-get-amphipods-home grid-state-pt1 grid-info-pt1 amphi-type))
      9 :a 90 :b 400 :c 12000 :d))
  (testing "Pt2 cases"
    (are [expected amphi-type]
        (= expected (sut/cost-to-get-amphipods-home grid-state-pt2 grid-info-pt2 amphi-type))
      31 :a 250 :b 1800 :c 38000 :d)))

(deftest cost-to-get-amphipods-home
  (testing "Pt1"
    (is (= (+ 9 90 400 12000) (sut/cost-to-get-all-amphipods-home grid-state-pt1 grid-info-pt1)))
    (is (= (+ 31 250 1800 38000) (sut/cost-to-get-all-amphipods-home grid-state-pt2 grid-info-pt2)))))
