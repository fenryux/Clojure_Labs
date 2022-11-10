;; small experiments
(def n 2)
(def parts (partition n (range 8)))
parts
(def smth (cycle(for [x parts
      :let [y (reduce + x)]]
  y)))
(take 10 smth)
;; small experiments ended


(defn sum-n [sqc]
  (cycle (for [x (partition n sqc)
               :let [y (reduce + x)]]
           y)))
(range 6)
(take 10 (sum-n (range 6)))