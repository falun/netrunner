(ns game.core
  (:require
    [game.core.access]
    [game.core.actions]
    [game.core.agendas]
    [game.core.bad-publicity]
    [game.core.board]
    [game.core.card]
    [game.core.card-defs]
    [game.core.change-vals]
    [game.core.charge]
    [game.core.checkpoint]
    [game.core.commands]
    [game.core.cost-fns]
    [game.core.costs]
    [game.core.damage]
    [game.core.def-helpers]
    [game.core.diffs]
    [game.core.drawing]
    [game.core.effects]
    [game.core.eid]
    [game.core.engine]
    [game.core.events]
    [game.core.expose]
    [game.core.finding]
    [game.core.flags]
    [game.core.gaining]
    [game.core.hand-size]
    [game.core.hosting]
    [game.core.ice]
    [game.core.identities]
    [game.core.initializing]
    [game.core.installing]
    [game.core.mark]
    [game.core.memory]
    [game.core.moving]
    [game.core.optional]
    [game.core.payment]
    [game.core.pick-counters]
    [game.core.play-instants]
    [game.core.player]
    [game.core.process-actions]
    [game.core.prompt-state]
    [game.core.prompts]
    [game.core.props]
    [game.core.psi]
    [game.core.purging]
    [game.core.revealing]
    [game.core.rezzing]
    [game.core.runs]
    [game.core.sabotage]
    [game.core.say]
    [game.core.servers]
    [game.core.set-up]
    [game.core.shuffling]
    [game.core.state]
    [game.core.subtypes]
    [game.core.tags]
    [game.core.to-string]
    [game.core.toasts]
    [game.core.trace]
    [game.core.turns]
    [game.core.update]
    [game.core.virus]
    [game.core.winning]
    [game.macros]
    [potemkin :refer [import-vars]]))

(defmacro expose-vars
  "Surrounds a import-vars call with an anonymous function to get arround the 64kb limit on the method size in the JVM" 
  [symbols]
  `((fn [] (import-vars ~symbols))))

(expose-vars
  [game.core.access
   access-bonus
   access-bonus-count
   access-card
   access-cost
   access-cost-bonus
   access-end
   access-helper-archives
   access-helper-hq
   access-helper-rd
   access-helper-remote
   access-non-agenda
   breach-server
   clean-access-args
   choose-access
   facedown-cards
   faceup-accessible
   get-all-content
   get-all-hosted
   get-only-card-to-access
   interactions
   max-access
   msg-handle-access
   must-continue?
   no-trash-or-steal
   num-cards-central
   num-cards-to-access
   root-content
   set-only-card-to-access
   steal
   steal-cost
   steal-cost-bonus
   turn-archives-faceup])

(expose-vars
  [game.core.actions
   advance
   click-advance
   click-credit
   click-draw
   click-run
   close-deck
   do-purge
   generate-install-list
   generate-runnable-zones
   get-runnable-zones
   move-card
   play
   play-ability
   play-auto-pump
   play-auto-pump-and-break
   play-corp-ability
   play-dynamic-ability
   play-heap-breaker-auto-pump-and-break
   play-runner-ability
   play-subroutine
   play-unbroken-subroutines
   remove-tag
   resolve-prompt
   score
   select
   trash-resource
   view-deck])

(expose-vars
  [game.core.agendas
   update-advancement-requirement
   update-all-advancement-requirements
   update-all-agenda-points])

(expose-vars
  [game.core.bad-publicity
   bad-publicity-prevent
   gain-bad-publicity
   lose-bad-publicity])

(expose-vars
  [game.core.board
   all-active
   all-active-installed
   all-installed
   all-installed-runner-type
   card->server
   get-all-cards
   get-all-installed
   get-remote-names
   get-remote-zones
   get-remotes
   get-zones
   in-play?
   installable-servers
   installed-byname
   server->zone
   server-list])

(expose-vars
  [game.core.card
   active?
   agenda?
   asset?
   assoc-host-zones
   can-be-advanced?
   card-index
   condition-counter?
   corp-installable-type?
   corp?
   event?
   facedown?
   faceup?
   fake-identity?
   get-advancement-requirement
   get-agenda-points
   get-card
   get-card-hosted
   get-cid
   get-counters
   get-nested-host
   get-title
   get-zone
   hardware?
   has-subtype?
   ice?
   identity?
   in-archives-root?
   in-current?
   in-deck?
   in-discard?
   in-hand?
   in-hq-root?
   in-play-area?
   in-rd-root?
   in-root?
   in-scored?
   in-server?
   installed?
   is-public?
   is-type?
   map->Card
   operation?
   program?
   protecting-a-central?
   protecting-archives?
   protecting-hq?
   protecting-rd?
   resource?
   rezzed?
   runner?
   upgrade?
   virus-program?])

(expose-vars
  [game.core.card-defs
   card-def
   defcard-impl])

(expose-vars
  [game.core.change-vals
   change])

(expose-vars
  [game.core.charge
   can-charge
   charge-ability
   charge-card])

(expose-vars
  [game.core.checkpoint
   fake-checkpoint])

(expose-vars
  [game.core.commands
   command-adv-counter
   command-counter
   command-undo-click
   command-undo-turn
   parse-command])

(expose-vars
  [game.core.cost-fns
   break-sub-ability-cost
   card-ability-cost
   has-trash-ability?
   ignore-install-cost?
   install-additional-cost-bonus
   install-cost
   jack-out-cost
   play-additional-cost-bonus
   play-cost
   rez-additional-cost-bonus
   rez-cost
   run-additional-cost-bonus
   run-cost
   score-additional-cost-bonus
   trash-cost])

(expose-vars
  [game.core.costs
   total-available-credits])

(expose-vars
  [game.core.damage
   chosen-damage
   corp-can-choose-damage?
   damage
   damage-bonus
   damage-count
   damage-prevent
   enable-corp-damage-choice
   enable-runner-damage-choice
   runner-can-choose-damage?])

(expose-vars
  [game.core.def-helpers
   breach-access-bonus
   combine-abilities
   corp-rez-toast
   defcard
   do-brain-damage
   do-meat-damage
   do-net-damage
   make-recurring-ability
   offer-jack-out
   reorder-choice
   trash-on-empty
   corp-recur])

(expose-vars
  [game.core.diffs
   public-states
   public-diffs])

(expose-vars
  [game.core.drawing
   draw
   draw-bonus
   first-time-draw-bonus
   max-draw
   remaining-draws])

(expose-vars
  [game.core.effects
   any-effects
   gather-effects
   get-effects
   get-effect-maps
   get-effect-value
   register-constant-effects
   register-floating-effect
   sum-effects
   unregister-constant-effects
   unregister-effects-for-card
   unregister-floating-effects])

(expose-vars
  [game.core.eid
   complete-with-result
   effect-completed
   eid-set-defaults
   make-eid
   make-result
   register-effect-completed])

(expose-vars
  [game.core.engine
   ability-as-handler
   can-trigger?
   checkpoint
   dissoc-req
   gather-events
   is-ability?
   merge-costs-paid
   not-used-once?
   pay
   prompt!
   queue-event
   register-ability-type
   register-default-events
   register-events
   register-pending-event
   register-once
   register-suppress
   resolve-ability
   select-ability-kw
   should-trigger?
   trigger-event
   trigger-event-simult
   trigger-event-sync
   trigger-suppress
   unregister-event-by-uuid
   unregister-events
   unregister-floating-events
   unregister-suppress
   unregister-suppress-by-uuid])

(expose-vars
  [game.core.events
   event-count
   first-event?
   first-installed-trash-own?
   first-installed-trash?
   first-run-event?
   first-successful-run-on-server?
   first-trash?
   get-installed-trashed
   get-turn-damage
   last-turn?
   no-event?
   no-run-event?
   not-last-turn?
   run-event-count
   run-events
   second-event?
   turn-events])

(expose-vars
  [game.core.expose
   expose
   expose-prevent])

(expose-vars
  [game.core.finding
   find-card
   find-cid
   find-latest
   get-scoring-owner])

(expose-vars
  [game.core.flags
   ab-can-prevent?
   any-flag-fn?
   can-access-loud
   can-access?
   can-advance?
   can-host?
   can-rez?
   can-run-server?
   can-run?
   can-score?
   can-steal?
   can-trash?
   card-can-prevent?
   card-flag-fn?
   card-flag?
   cards-can-prevent?
   check-flag-types?
   clear-all-flags-for-card!
   clear-persistent-flag!
   clear-run-flag!
   clear-run-register!
   clear-turn-flag!
   clear-turn-register!
   enable-run-on-server
   get-card-prevention
   get-prevent-list
   get-preventing-cards
   has-flag?
   in-corp-scored?
   in-runner-scored?
   is-scored?
   lock-zone
   persistent-flag?
   prevent-current
   prevent-draw
   prevent-jack-out
   prevent-run-on-server
   register-persistent-flag!
   register-run-flag!
   register-turn-flag!
   release-zone
   run-flag?
   turn-flag?
   untrashable-while-resources?
   untrashable-while-rezzed?
   when-scored?
   zone-locked?])

(expose-vars
  [game.core.gaining
   base-mod-size
   deduct
   gain
   gain-credits
   gain-clicks
   lose
   lose-credits
   lose-clicks
   safe-inc-n
   sub->0])

(expose-vars
  [game.core.hand-size
   corp-hand-size+
   hand-size
   hand-size+
   runner-hand-size+
   sum-hand-size-effects
   update-hand-size])

(expose-vars
  [game.core.hosting
   host
   remove-from-host])

(expose-vars
  [game.core.ice
   add-extra-sub!
   add-sub
   add-sub!
   all-subs-broken-by-card?
   all-subs-broken?
   any-subs-broken-by-card?
   any-subs-broken?
   auto-icebreaker
   break-all-subroutines
   break-all-subroutines!
   break-sub
   break-subroutine
   break-subroutine!
   break-subroutines
   break-subroutines-msg
   breakable-subroutines-choice
   breaker-strength
   dont-resolve-all-subroutines
   dont-resolve-all-subroutines!
   dont-resolve-subroutine
   dont-resolve-subroutine!
   get-current-ice
   get-run-ices
   get-strength
   ice-strength
   pump
   pump-all-ice
   pump-all-icebreakers
   pump-ice
   remove-extra-subs!
   remove-sub
   remove-sub!
   remove-subs
   remove-subs!
   reset-all-ice
   reset-all-subs
   reset-all-subs!
   reset-sub
   reset-sub!
   resolve-subroutine
   resolve-subroutine!
   resolve-unbroken-subs!
   set-current-ice
   strength-pump
   sum-ice-strength-effects
   unbroken-subroutines-choice
   update-all-ice
   update-all-icebreakers
   update-breaker-strength
   update-ice-in-server
   update-ice-strength])

(expose-vars
  [game.core.identities
   disable-card
   disable-identity
   enable-card
   enable-identity])

(expose-vars
  [game.core.initializing
   ability-init
   card-init
   deactivate
   make-card
   subroutines-init
   update-abilities-cost-str
   update-ability-cost-str
   update-all-card-labels])

(expose-vars
  [game.core.installing
   corp-can-pay-and-install?
   corp-install
   corp-install-cost
   corp-install-list
   corp-install-msg
   install-as-condition-counter
   install-locked?
   runner-can-install?
   runner-can-pay-and-install?
   runner-install])

(expose-vars
  [game.core.link
   get-link
   link+
   update-link])

(expose-vars
  [game.core.mark
   set-mark
   is-mark?
   identify-mark
   identify-mark-ability])

(expose-vars
  [game.core.memory
   available-mu
   caissa-mu+
   mu+
   update-mu
   virus-mu+])

(expose-vars
  [game.core.moving
   add-to-currently-drawing
   as-agenda
   discard-from-hand
   flip-facedown
   flip-faceup
   forfeit
   mill
   move
   move-zone
   remove-from-currently-drawing
   swap-agendas
   swap-cards
   swap-ice
   swap-installed
   trash
   trash-cards
   trash-prevent
   uninstall])

(expose-vars
  [game.core.optional
   get-autoresolve
   never?
   optional-ability
   set-autoresolve])

(expose-vars
  [game.core.payment
   add-cost-label-to-ability
   build-cost-label
   build-cost-string
   build-spend-msg
   can-pay?
   cost->string
   cost-name
   cost-ranks
   cost-target
   cost-targets
   cost-value
   handler
   label
   merge-costs
   payable?
   value])

(expose-vars
  [game.core.play-instants
   can-play-instant?
   play-instant
   play-instant-costs])

(expose-vars
  [game.core.pick-counters
   pick-credit-providing-cards
   pick-virus-counters-to-spend])

(expose-vars
  [game.core.process-actions
   command-parser
   process-action])

(expose-vars
  [game.core.props
   add-counter
   add-icon
   add-prop
   remove-icon
   set-prop])

(expose-vars
  [game.core.player
   map->Corp
   map->Runner
   new-corp
   new-runner])

(expose-vars
  [game.core.prompt-state
   add-to-prompt-queue
   remove-from-prompt-queue])

(expose-vars
  [game.core.prompts
   cancellable
   choice-parser
   clear-run-prompts
   clear-wait-prompt
   resolve-select
   show-run-prompts
   show-prompt
   show-prompt-with-dice
   show-select
   show-trace-prompt
   show-wait-prompt])

(expose-vars
  [game.core.psi
   psi-game])

(expose-vars
  [game.core.purging
   purge])

(expose-vars
  [game.core.revealing
   conceal-hand
   reveal
   reveal-hand])

(expose-vars
  [game.core.rezzing
   derez
   get-rez-cost
   rez])

(expose-vars
  [game.core.runs
   add-run-effect
   bypass-ice
   can-bypass-ice
   check-auto-no-action
   check-for-empty-server
   complete-run
   continue
   encounter-ends
   end-run
   end-run-prevent
   force-ice-encounter
   gain-next-run-credits
   gain-run-credits
   get-current-encounter
   handle-end-run
   jack-out
   jack-out-prevent
   make-run
   pass-ice
   prevent-access
   redirect-run
   set-next-phase
   set-phase
   start-next-phase
   successful-run
   successful-run-replace-breach
   toggle-auto-no-action
   total-cards-accessed
   total-run-cost])


(expose-vars
  [game.core.sabotage
   sabotage-ability])

(expose-vars
  [game.core.say
   enforce-msg
   indicate-action
   make-message
   make-system-message
   play-sfx
   say
   system-msg
   implementation-msg
   system-say])

(expose-vars
  [game.core.servers
   central->name
   central->zone
   from-same-server?
   get-server-type
   in-same-server?
   is-central?
   is-remote?
   is-root?
   name-zone
   protecting-same-server?
   remote->name
   remote-num->name
   same-server?
   target-server
   type->rig-zone
   unknown->kw
   zone->name
   zone->sort-key
   zones->sorted-names])

(expose-vars
  [game.core.set-up
   build-card
   create-deck
   init-game
   keep-hand
   mulligan])

(expose-vars
  [game.core.shuffling
   shuffle!
   shuffle-deck
   shuffle-into-deck
   shuffle-into-rd-effect])

(expose-vars
  [game.core.state
   make-rid
   map->State
   new-state])

(expose-vars
  [game.core.subtypes
   update-all-subtypes])

(expose-vars
  [game.core.tags
   gain-tags
   lose-tags
   tag-prevent])

(expose-vars
  [game.core.to-string
   card-str])

(expose-vars
  [game.core.toasts
   show-error-toast
   toast])

(expose-vars
  [game.core.trace
   init-trace
   init-trace-bonus])

(expose-vars
  [game.core.turns
   end-phase-12
   end-turn
   start-turn])

(expose-vars
  [game.core.update
   update!
   update-hosted!])

(expose-vars
  [game.core.virus
   count-virus-programs
   get-virus-counters
   number-of-virus-counters
   number-of-runner-virus-counters])

(expose-vars
  [game.core.winning
   check-win-by-agenda
   clear-win
   concede
   flatline
   win
   win-decked])

(expose-vars
  [game.macros
   continue-ability
   effect
   msg
   req
   wait-for
   when-let*])

