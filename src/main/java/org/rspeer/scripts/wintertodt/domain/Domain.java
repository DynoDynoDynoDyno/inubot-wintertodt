package main.java.org.rspeer.scripts.f2ppker.domain;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.rspeer.event.Subscribe;
import org.rspeer.game.event.*;
import org.rspeer.game.scene.Players;
import org.rspeer.game.script.event.ScriptConfigEvent;
import main.java.org.rspeer.scripts.f2ppker.data.Action;
import main.java.org.rspeer.scripts.f2ppker.data.Constant;
import main.java.org.rspeer.scripts.f2ppker.domain.config.Config;
import org.rspeer.commons.logging.Log;

/**
 * Central event handler and domain coordinator for the F2P PKer script.
 * Handles all event subscriptions and coordinates between domain components.
 */
@Singleton
public class Domain {
  private final Config config;
  private final State state;
  private final Timers timers;
  private final TargetTracker targetTracker;
  private final WeaponTracker weaponTracker;
  private final Statistics statistics;

  @Inject
  public Domain(Config config, State state, Timers timers,
                TargetTracker targetTracker, WeaponTracker weaponTracker,
                Statistics statistics) {
    this.config = config;
    this.state = state;
    this.timers = timers;
    this.targetTracker = targetTracker;
    this.weaponTracker = weaponTracker;
    this.statistics = statistics;
  }

  @Subscribe
  public void notify(ScriptConfigEvent event) {
    config.initialize(event.getSource());
  }

  @Subscribe
  public void notify(TickEvent event) {
    timers.tick();
    state.tick();
  }

  @Subscribe
  public void notify(AnimationEvent event) {
    if (event.getSource().equals(Players.self())) {
      timers.animate();
      state.animate(event.getCurrent());

      // Track specific combat animations
      if (event.getCurrent() == Action.BOW_ATTACK.getAnimation()) {
        Log.fine("Domain: Bow attack animation detected");
        weaponTracker.registerBowAttack(timers.now());
      } else if (event.getCurrent() == Action.RUNE_2H_ATTACK.getAnimation()) {
        Log.fine("Domain: 2H attack animation detected");
      }

      // Track if this is an attack animation with a pending target
      if (Constant.ATTACK_ANIMATIONS.contains(event.getCurrent()) &&
              targetTracker.getPendingTargetName() != null) {
        Log.info("Domain: Attack animation detected, confirming target: " +
                targetTracker.getPendingTargetName());
        targetTracker.confirmPendingTarget();
        state.setCurrentState(State.BotState.RANGING);
      }
    }
  }

  @Subscribe
  public void notify(ChatMessageEvent event) {
    ChatMessageEvent.Type type = event.getType();
    if (type == ChatMessageEvent.Type.FILTERED || type == ChatMessageEvent.Type.GAME) {
      String text = event.getContents().toLowerCase();
      timers.message(text);

      // Handle combat-specific messages
      if (text.contains("is fighting another player")) {
        handlePlayerInCombatMessage();
      } else if (text.contains("died")) {
        statistics.message(text);
      }
    }
  }

  private void handlePlayerInCombatMessage() {
    // The player we're trying to attack is already in combat
    if (targetTracker.getPendingTargetName() != null) {
      Log.info("Domain: Target is fighting another player, clearing pending target");
      targetTracker.clearPendingTarget();
    } else if (targetTracker.getTargetName() != null) {
      Log.info("Domain: Target is fighting another player, clearing target");
      targetTracker.clearTarget();
      state.setCurrentState(State.BotState.IDLE);
    }
  }

  @Subscribe
  public void notify(SkillEvent event) {
    // Handle skill experience changes - important for damage prediction
    if (event.getSource() == org.rspeer.game.component.tdi.Skill.RANGED) {
      targetTracker.skillChange(event);

      // If we're targeting someone and got range exp, register a bow hit
      if (event.getChange() > 0 && targetTracker.getTargetName() != null) {
        weaponTracker.registerBowHit(timers.now());
      }
    }
  }

  @Subscribe
  public void notify(HitsplatEvent event) {
    // If our target took damage and we're in combat with them
    if (targetTracker.getTargetName() != null &&
            Players.query().names(targetTracker.getTargetName()).results().first() != null &&
            event.getSource().equals(Players.query().names(targetTracker.getTargetName()).results().first())) {

      // Record combat timing for this player
      timers.recordCombat(targetTracker.getTargetName());
    }
  }

  // Getters for domain components
  public Config getConfig() { return config; }
  public State getState() { return state; }
  public Timers getTimers() { return timers; }
  public TargetTracker getTargets() { return targetTracker; }
  public WeaponTracker getWeaponTracker() { return weaponTracker; }
  public Statistics getStatistics() { return statistics; }
}