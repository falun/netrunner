(in-ns 'game.core)

(defn turn-message
  "Prints a message for the start or end of a turn, summarizing credits and cards in hand."
  [state side start-of-turn]
  (let [pre (if start-of-turn "started" "is ending")
        hand (if (= side :runner) "their Grip" "HQ")
        cards (count (get-in @state [side :hand]))
        credits (get-in @state [side :credit])
        text (str pre " their turn " (:turn @state) " with " credits " [Credit] and " cards " cards in " hand)]
    (system-msg state side text {:hr (not start-of-turn)})))

(defn end-phase-12
  "End phase 1.2 and trigger appropriate events for the player."
  ([state side args] (end-phase-12 state side (make-eid state) args))
  ([state side eid args]
   (turn-message state side true)
   (wait-for (trigger-event-simult state side (if (= side :corp) :corp-turn-begins :runner-turn-begins) nil nil)
             (unregister-floating-effects state side :start-of-turn)
             (unregister-floating-events state side :start-of-turn)
             (unregister-floating-effects state side (if (= side :corp) :until-corp-turn-begins :until-runner-turn-begins))
             (unregister-floating-events state side (if (= side :corp) :until-corp-turn-begins :until-runner-turn-begins))
             (when (= side :corp)
               (wait-for (draw state side 1 nil)
                         (trigger-event-simult state side eid :corp-mandatory-draw nil nil)))
             (swap! state dissoc (if (= side :corp) :corp-phase-12 :runner-phase-12))
             (when (= side :corp)
               (update-all-advancement-costs state side)))))

(defn start-turn
  "Start turn."
  [state side args]
  ; Don't clear :turn-events until the player clicks "Start Turn"
  ; Fix for Hayley triggers
  (swap! state assoc :turn-events nil)

  ; Functions to set up state for undo-turn functionality
  (doseq [s [:runner :corp]] (swap! state dissoc-in [s :undo-turn]))
  (swap! state assoc :turn-state (dissoc @state :log))

  (when (= side :corp)
    (swap! state update-in [:turn] inc))

  (doseq [c (filter :new (all-installed state side))]
    (update! state side (dissoc c :new)))

  (swap! state assoc :active-player side :per-turn nil :end-turn false)
  (doseq [s [:runner :corp]]
    (swap! state assoc-in [s :register] nil))

  (let [phase (if (= side :corp) :corp-phase-12 :runner-phase-12)
        start-cards (filter #(card-flag-fn? state side % phase true)
                            (concat (all-active state side)
                                    (remove facedown? (all-installed state side))))
        extra-clicks (get-in @state [side :extra-click-temp] 0)]
    (gain state side :click (get-in @state [side :click-per-turn]))
    (cond
      (neg? extra-clicks) (lose state side :click (abs extra-clicks))
      (pos? extra-clicks) (gain state side :click extra-clicks))
    (swap! state dissoc-in [side :extra-click-temp])
    (swap! state assoc phase true)
    (trigger-event state side phase nil)
    (if (not-empty start-cards)
      (toast state side
             (str "You may use " (string/join ", " (map :title start-cards))
                  (if (= side :corp)
                    " between the start of your turn and your mandatory draw."
                    " before taking your first click."))
             "info")
      (end-phase-12 state side args))))

(defn handle-end-of-turn-discard
  ([state side _card _targets] (handle-end-of-turn-discard state side (make-eid state) _card _targets))
  ([state side eid _card _targets]
   (let [cur-hand-size (count (get-in @state [side :hand]))
         max-hand-size (max (hand-size state side) 0)]
     (if (> cur-hand-size max-hand-size)
       (continue-ability
         state side
         {:prompt (str "Discard down to " (quantify max-hand-size "card"))
          :choices {:card in-hand?
                    :max (- cur-hand-size max-hand-size)
                    :all true}
          :effect (req (system-msg state side
                                   (str "discards "
                                        (if (= :runner side)
                                          (join ", " (map :title targets))
                                          (quantify (count targets) "card"))
                                        " from " (if (= :runner side) "their Grip" "HQ")
                                        " at end of turn"))
                       (doseq [t targets]
                         (move state side t :discard))
                       (effect-completed state side eid))}
         nil nil)
       (effect-completed state side eid)))))

(defn end-turn
  ([state side args] (end-turn state side (make-eid state) args))
  ([state side eid args]
   (wait-for
     (handle-end-of-turn-discard state side nil nil)
     (turn-message state side false)
     (when (and (= side :runner)
                (neg? (hand-size state side)))
       (flatline state))
     (wait-for (trigger-event-simult state side (if (= side :runner) :runner-turn-ends :corp-turn-ends) nil nil)
               (trigger-event state side (if (= side :runner) :post-runner-turn-ends :post-corp-turn-ends))
               (swap! state assoc-in [side :register-last-turn] (-> @state side :register))
               (unregister-floating-effects state side :end-of-turn)
               (unregister-floating-events state side :end-of-turn)
               (unregister-floating-effects state side (if (= side :runner) :until-runner-turn-ends :until-corp-turn-ends))
               (unregister-floating-events state side (if (= side :runner) :until-runner-turn-ends :until-corp-turn-ends))
               (doseq [card (all-active-installed state :runner)]
                 ;; Clear :installed :this-turn as turn has ended
                 (when (= :this-turn (installed? card))
                   (update! state side (assoc (get-card state card) :installed true)))
                 ;; Clear the added-virus-counter flag for each virus in play.
                 ;; We do this even on the corp's turn to prevent shenanigans with something like Gorman Drip and Surge
                 (when (has-subtype? card "Virus")
                   (set-prop state :runner (get-card state card) :added-virus-counter false))
                 ;; Remove all-turn strength from icebreakers.
                 ;; We do this even on the corp's turn in case the breaker is boosted due to Offer You Can't Refuse
                 (when (has-subtype? card "Icebreaker")
                   (update-breaker-strength state :runner (get-card state card))))
               (doseq [card (all-installed state :corp)]
                 ;; Clear :this-turn flags as turn has ended
                 (when (= :this-turn (installed? card))
                   (update! state side (assoc (get-card state card) :installed true)))
                 (when (= :this-turn (:rezzed card))
                   (update! state side (assoc (get-card state card) :rezzed true))))
               ;; Update strength of all ice every turn
               (update-all-ice state side)
               (swap! state assoc :end-turn true)
               (swap! state update-in [side :register] dissoc :cannot-draw)
               (swap! state update-in [side :register] dissoc :drawn-this-turn)
               (clear-turn-register! state)
               (when-let [extra-turns (get-in @state [side :extra-turns])]
                 (when (pos? extra-turns)
                   (start-turn state side nil)
                   (swap! state update-in [side :extra-turns] dec)
                   (system-msg state side (string/join ["will have " (quantify extra-turns "extra turn") " remaining."]))))
               (effect-completed state side eid)))))
