package main.java.org.rspeer.scripts.f2ppker.domain;

import com.google.inject.Singleton;
import java.util.EnumMap;
import java.util.Map;
import java.util.HashMap;
import main.java.org.rspeer.scripts.f2ppker.data.Action;

/**
 * Manages and tracks all time-related events in the game.
 * Tracks animations, action cooldowns, and combat timing.
 */
@Singleton
public class Timers {
  /** Current time in ticks **/
  private int now;

  /** Tick of the last animation **/
  private int animation;

  /** Tracks when actions were last performed */
  private final Map<Action, Integer> actionTimers = new EnumMap<>(Action.class);

  /** Tracks when players were last in combat */
  private final Map<String, Integer> playerLastCombatTick = new HashMap<>();

  /**
   * Called on each game tick to increment the internal timer
   */
  void tick() {
    now++;
  }

  /**
   * Called when an animation is performed to record the time
   */
  void animate() {
    animation = now;
  }

  /**
   * Processes chat messages that might reset animation timers
   */
  void message(String text) {
    // Some messages can interrupt animations
    if (text.contains("you eat") ||
            text.contains("health points") ||
            text.contains("heals") ||
            text.contains("restore")) {
      // Reset animation timer
      animation = 0;
    }
  }

  /**
   * Records that a player was in combat at the current tick
   */
  public void recordCombat(String playerName) {
    playerLastCombatTick.put(playerName, now);
  }

  /**
   * Checks if a player has been in combat within the specified number of ticks
   */
  public boolean hasBeenInCombatRecently(String playerName, int ticks) {
    Integer lastTime = playerLastCombatTick.get(playerName);
    return lastTime != null && now - lastTime < ticks;
  }

  /**
   * Records when an action was performed
   */
  public void recordAction(Action action) {
    actionTimers.put(action, now);
  }

  /**
   * Checks if enough time has passed since the last action to perform it again
   */
  public boolean isActionReady(Action action) {
    Integer lastTime = actionTimers.get(action);
    return lastTime == null || now - lastTime >= action.getTickDuration();
  }

  /**
   * Checks if the player has been idle for at least the specified number of ticks
   */
  public boolean isIdle(int ticksThreshold) {
    return now - animation >= ticksThreshold;
  }

  /**
   * Returns the current tick count
   */
  public int now() {
    return now;
  }

  /**
   * Returns the tick when the last animation occurred
   */
  public int getLastAnimationTick() {
    return animation;
  }
}