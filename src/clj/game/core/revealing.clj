(ns game.core.revealing
  (:require
    [game.core.eid :refer [make-eid]]
    [game.core.events :refer [trigger-event-sync]]))

(defn reveal-hand
  "Reveals a side's hand to opponent and spectators."
  [state side]
  (swap! state assoc-in [side :openhand] true))

(defn conceal-hand
  "Conceals a side's revealed hand from opponent and spectators."
  [state side]
  (swap! state update side dissoc :openhand))

(defn reveal
  "Trigger the event for revealing one or more cards."
  [state side & targets]
  (apply trigger-event-sync state side (make-eid state) (if (= :corp side) :corp-reveal :runner-reveal) (flatten targets)))
