(ns aoc21.day23)



;; TODO: sort out all these variables so they can be passed around

(def move-costs {:a 1 :b 10 :c 100 :d 1000})
(def home-cols {:a 3 :b 5 :c 7 :d 9})
(def home-rows [2 3])
(def corridor-row 1)
(def corridor-rest-cols [1 2 4 6 8 10 11])

(def grid-info {:move-costs         {:a 1 :b 10 :c 100 :d 1000}
                :home-rows          [2 3]
                :home-cols          {:a 3 :b 5 :c 7 :d 9}
                :corridor-row       1
                :corridor-rest-cols [1 2 4 6 8 10 11]})

;; TODO: do we need a record to represent each amphipod?

;; TODO: Think about whether we should separately keep an list of each amphipod
;; locations.  I don't think we need to as it's pretty easy to scan through and
;; find amphis.


;; TODO: what is the point of these rows with 13 nils? I
;; don't think we need them.


(def initial-state
  [(repeat 13 nil)
   (repeat 13 nil)
   [nil nil nil :b nil :c nil :b nil :d nil nil nil]
   [nil nil nil :a nil :d nil :c nil :a nil nil nil]
   (repeat 13 nil)])

(def grid-num-rows (count initial-state))
(def grid-num-cols (count (first initial-state)))

(defn abs [n] (max n (- n)))

(defn get-row-col [m [r c]]
(-> (nth m r) (nth c)))

(defn all-behind-of-type? [state {:keys [home-rows]} amphi-type [r c]]
  (every? #(= amphi-type
              (get-row-col state [% c]))
          (range (inc r) (inc (apply max home-rows)))))

;; If amphipod doesn't need to move, will return 0
;;
;; If amphipod is in home col and needs to move out to make way for one beneath
;; will return distance out of home-col, to the side and to top of home-col
;;
;; If amphipod is in the wrong column, returns distance out of init-col, along
;; corridor to top of home-col.
;;
;; Journey down home-col can be calculated separately, taking into account that
;; each AP needs to descend a different distance.
(defn dist-to-get-amphipod-top-of-home-col [state amphi-type [r c :as location]]
  (let [home-col (home-cols amphi-type)]
    (if (= c home-col)
      ;; Already in home col, now check whether all beneath are the same type
      (if (all-behind-of-type? state amphi-type location)
        ;; Anything beneath this of the right type, so don't need to move
        0
        ;; Need to move out into corridor, +2 to get to rest location and back
        ;; again
        (+ (- r corridor-row) 2))
      ;; Need to get out, across the corridor (leave cost of descending into home
      ;; row)
      (+ (- r corridor-row)
         (abs (- home-col c))))))

(defn get-all-grid-locations [{:keys [num-rows num-cols] :as grid-info}]
  (for [r (range num-rows)
        c (range num-cols)]
    [r c]))

(defn find-amphipod-locations [state grid-info amphi-type]
  (->> (get-all-grid-locations grid-info)
       (map #(if (= (get-row-col state %)
                    amphi-type)
               %
               nil))
       (remove nil?)))

(defn home-locations [{:keys [home-rows home-cols] :as grid-info} amphi-type]
  (map vector home-rows (repeat (home-cols amphi-type))))

(defn num-move-in-corridor [state amphi-type]
  (let [home-col     (home-cols amphi-type)
        ;; TODO switch to threading macro
        num-stay-put (-> (take-while #(= % amphi-type)
                                     (map (partial get-row-col state)
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

;; TODO a bit confusing, should we pass in colum, rather than amphi-type
(defn top-of-col [state amphi-type]
  (->> (home-locations amphi-type)
       (drop-while #(nil? (get-row-col state %)))
       first))

(defn top-of-col-move? [state amphi-type]
  (let [loc (top-of-col state amphi-type)
        ]))






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
