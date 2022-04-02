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
(def home-cols {:a 3 :b 5 :c 7 :d 9})
(def home-rows [2 3])
(def corridor-row 1)
(def corridor-rest-cols [1 2 4 6 8 10 11])

;; TODO: do we need a record to represent each amphipod?

;; TODO: Think about whether we should separately keep an list of each amphipod
;; locations.  I don't think we need to as it's pretty easy to scan through and
;; find amphis.

;; TODO: I dont't think we need to track sat-in-corridor, as algo would never
;; choose to go into corridor twice

;; TODO: what is the point of these rows with 13 nils? I
;; don't think we need them.
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

(def grid-num-rows (count initial-state))
(def grid-num-cols (count (first initial-state)))

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
        ;; Need to move out into corridor, +2 to get to rest location and back again
        (+ (- r corridor-row) 2))
      ;; Need to get out, across the corridor (leave cost of descending into home
      ;; row)
      (+ (- r corridor-row)
         (abs (- home-col c))))))

(defn get-all-grid-locations []
  (for [r (range grid-num-rows)
        c (range grid-num-cols)]
    [r c]))

(defn find-amphipod-locations [state amphi-type]
  (->> (get-all-grid-locations)
       (map #(if (= (-> (get-row-col state %)
                        :amphi-type)
                    amphi-type)
               %
               nil))
       (remove nil?)))

(defn home-locations [amphi-type]
  (map vector home-rows (repeat (home-cols amphi-type))))

(defn num-move-in-corridor [state amphi-type]
  (let [home-col     (home-cols amphi-type)
        num-stay-put (-> (take-while #(= % amphi-type)
                                     (map (comp :amphi-type (partial get-row-col state))
                                          (reverse (home-locations amphi-type))))
                         count)]
    (- (count home-rows) num-stay-put)))

(defn dist-to-get-amphipods-home [state amphi-type]
  (let [num-move-in (num-move-in-corridor state amphi-type)
        dist-to-move-in-home-row (apply + (range 1 (inc num-move-in)))]
    (->> (find-amphipod-locations state amphi-type)
         (map (partial dist-to-get-amphipod-top-of-home-col state amphi-type))
         (apply +)
         (+ dist-to-move-in-home-row))))

(defn cost-to-get-amphipods-home [state amphi-type]
  (* (amphi-type move-costs)
     (dist-to-get-amphipods-home state amphi-type)))

(defn cost-to-get-all-amphipods-home [state]
  (apply + (map (partial cost-to-get-amphipods-home initial-state)
                '(:a :b :c :d))))


(defn all-behind-of-type? [state amphi-type [r c]]
  (if (every? #(= amphi-type
                  (:amphi-type (get-row-col state [% c])))
              (range (inc r) (inc (apply max home-rows))))
    ;; Anything beneath this of the right type, so don't need to move
    true
    ;; Need to move out into corridor, +2 to get to rest location and back again
    false))

(defn candidate-moves [state]
  ;; note: have 2 choices of what to move:
  ;;     * the top ap in each home col
  ;;       can go to:
  ;;       --> it's home position (pref, if poss)
  ;;       --> the corridor
  ;;     * an ap in a corridor
  ;;       --> it's home position
  )




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
