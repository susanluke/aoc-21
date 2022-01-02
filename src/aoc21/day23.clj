(ns aoc21.day23)

(comment
  "
#############
#...........#
###B#C#B#D###
  #A#D#C#A#
  #########
"
  )

(def move-costs {:a 1 :b 10 :c 100 :d 1000})
(def home-cols {:a 3 :b 5 :c 6 :d 8})
(def home-rows [2 3])
(def corridor-row 1)
(def corridor-rest-cols [1 2 4 6 8 10 11])

;; TODO: Think about whether we should separately keep an list of each amphipod
;; locations
(def initial-state
  [(repeat 13 nil)
   (repeat 13 nil)
   [nil nil nil
    {:amphi-type :b :sat-in-corridoor false}
    nil
    {:amphi-type :c :sat-in-corridoor false}
    nil
    {:amphi-type :b :sat-in-corridoor false}
    nil
    {:amphi-type :d :sat-in-corridoor false}
    nil nil nil]
   [nil nil nil
    {:amphi-type :a :sat-in-corridoor false}
    nil
    {:amphi-type :d :sat-in-corridoor false}
    nil
    {:amphi-type :c :sat-in-corridoor false}
    nil
    {:amphi-type :a :sat-in-corridoor false}
    nil nil nil]
   (repeat 13 nil)])

(defn abs [n] (max n (- n)))

(defn get-row-col [m [r c]]
  (-> (nth m r) (nth c)))

(defn dist-to-get-amphipod-top-of-home-col [state amphi-type [r c :as location]]
  (let [home-col (home-cols amphi-type)]
    (if (= c home-col)
      ;; Already in home col, now check whether all beneath are the same type
      (if (every? #(= amphi-type
                      (:amphi-type (get-row-col state [% home-col])))
                  (range (inc r) (inc (apply max home-rows))))
        ;; Anything beneath this of the right type, so don't need to move
        0
        ;; Need to move out into corridor, +1 to get to rest location
        (inc (- r corridor-row))))
    ;; Need to get out, across the corridor (leave cost of descending into home
    ;; row)
    (+ (- r corridor-row)
       (abs (- (home-cols amphi-type) c)))))

(defn dist-to-get-amphipods-home [state amphi-type])





(comment
  " We want to: Work out a cost function of the suggested state - how much we
have to move each amphipod to get to final state?  Cost of moves + theoretical
cost to get to final state?

Look at possible next states.  Check if they have been 'visited', (or maybe
check what cost has accrued for them, and if we've found a lower value).  Look
at costs for next states and choose the lowest?

How do we know if we're at a deadend? If no more moves possible?

Check if we're at final state?

")
