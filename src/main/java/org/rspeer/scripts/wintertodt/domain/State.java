package main.java.org.rspeer.scripts.f2ppker.domain;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.rspeer.commons.logging.Log;
import main.java.org.rspeer.scripts.f2ppker.data.Constant;

/**
 * Tracks and manages the state of the PKer bot.
 * Handles state transitions and provides information about the current state.
 */
@Singleton
public class State {
  /**
   * Different states the bot can be in during its operation
   */
  public enum BotState {
    IDLE,               // Not engaged in any activity
    RANGING,            // Using bow to attack
    POSITIONING_FOR_2H, // Moving to optimal position for 2H attack
    PREPARING_2H,       // Switching to 2H weapon
    EXECUTING_2H,       // Executing 2H attack
    EATING,             // Eating food
    RETREATING,         // Running away from danger
    LOOTING,            // Collecting loot from kills
    BANKING,            // Banking for supplies
    RESTORING_PRAYER    // Restoring prayer at altar
  }

  // State tracking
  private BotState currentState = BotState.IDLE;
  private BotState stateBeforeEating = BotState.IDLE;
  private int lastAnimation = -1;
  private int stateTickCounter = 0;

  // State-related flags
  private boolean needToAttack = false;
  private boolean needToEquipBow = false;
  private boolean retreating = false;
  private boolean looting = false;
  private boolean needsBanking = false;
  private boolean restoringPrayer = false;
  private int retreatStartTime = -1;

  // Timing constants
  private static final int EATING_ANIMATION_DURATION = 3;
  private static final int MAX_2H_EXECUTION_TICKS = 5;
  private static final int COMBAT_COOLDOWN_SECONDS = 10;

  // Injected dependencies
  private final Timers timers;
  private final TargetTracker targetTracker;

  @Inject
  public State(Timers timers, TargetTracker targetTracker) {
    this.timers = timers;
    this.targetTracker = targetTracker;
  }

  /**
   * Called every game tick to update state
   */
  public void tick() {
    // Increment the state tick counter
    stateTickCounter++;

    // Handle automatic state transitions based on tick counter
    switch (currentState) {
      case EATING:
        handleEatingStateTick();
        break;

      case EXECUTING_2H:
        handle2HStateTick();
        break;

      // Add other state-specific tick handling as needed
    }
  }

  private void handleEatingStateTick() {
    // After being in EATING state for a few ticks, transition back
    if (stateTickCounter >= EATING_ANIMATION_DURATION) {
      // Return to the state we were in before eating
      if (stateBeforeEating == BotState.EXECUTING_2H) {
        Log.info("State: Transitioning from EATING back to RANGING (was in 2H), need to equip bow");
        currentState = BotState.RANGING;
        needToEquipBow = true;
        needToAttack = true;
      } else if (stateBeforeEating == BotState.RETREATING) {
        Log.info("State: Transitioning from EATING back to RETREATING");
        currentState = BotState.RETREATING;
      } else if (stateBeforeEating == BotState.LOOTING) {
        Log.info("State: Transitioning from EATING back to LOOTING");
        currentState = BotState.LOOTING;
      } else if (targetTracker.getTargetName() != null) {
        // If we have a target, set state to RANGING and need to attack
        Log.info("State: Transitioning from EATING back to RANGING");
        currentState = BotState.RANGING;
        needToAttack = true;
      } else {
        Log.info("State: Transitioning from EATING back to IDLE");
        currentState = BotState.IDLE;
      }
      // Reset the counter
      stateTickCounter = 0;
    }
  }

  private void handle2HStateTick() {
    // Prevent staying in 2H state for too long
    if (stateTickCounter >= MAX_2H_EXECUTION_TICKS) {
      Log.info("State: 2H execution timeout, returning to RANGING");
      currentState = BotState.RANGING;
      needToEquipBow = true;
      needToAttack = true;
      stateTickCounter = 0;
    }
  }

  /**
   * Records animation changes
   */
  public void animate(int animationId) {
    if (animationId != -1) {
      this.lastAnimation = animationId;
    }
  }

  /**
   * Process chat messages that affect state
   */
  public void message(String text) {
    // Handle messages that affect state
    if (text.contains("you have been disconnected") ||
            text.contains("your session has expired")) {
      resetState();
    }
  }

  /**
   * Changes the current state
   */
  public void setCurrentState(BotState state) {
    // Only process if we're changing to a different state
    if (currentState != state) {
      Log.info("State: Changing from " + currentState + " to " + state);

      // Handle special state transitions
      if (state == BotState.EATING) {
        stateBeforeEating = currentState;
      } else if (state == BotState.RANGING &&
              (currentState == BotState.EXECUTING_2H || currentState == BotState.PREPARING_2H)) {
        needToEquipBow = true;
        needToAttack = true;
      }

      // Reset tick counter and update state
      stateTickCounter = 0;
      currentState = state;
    }
  }

  /**
   * Reset state completely (used after disconnection)
   */
  private void resetState() {
    currentState = BotState.IDLE;
    stateBeforeEating = BotState.IDLE;
    stateTickCounter = 0;
    needToAttack = false;
    needToEquipBow = false;
    retreating = false;
    looting = false;
    needsBanking = false;
    restoringPrayer = false;
    retreatStartTime = -1;
  }

  // Getters and setters
  public BotState getCurrentState() {
    return currentState;
  }

  public int getLastAnimation() {
    return lastAnimation;
  }

  public boolean isNeedToAttack() {
    return needToAttack;
  }

  public void setNeedToAttack(boolean needToAttack) {
    this.needToAttack = needToAttack;
  }

  public boolean isNeedToEquipBow() {
    return needToEquipBow;
  }

  public void setNeedToEquipBow(boolean needToEquipBow) {
    this.needToEquipBow = needToEquipBow;
  }

  public boolean isRetreating() {
    return retreating;
  }

  public void setRetreating(boolean retreating, int currentTick) {
    this.retreating = retreating;
    if (retreating) {
      retreatStartTime = currentTick;
      Log.info("State: Started retreating at tick " + retreatStartTime);
      setCurrentState(BotState.RETREATING);
    } else {
      retreatStartTime = -1;
      Log.info("State: Stopped retreating");
    }
  }

  public boolean isLooting() {
    return looting;
  }

  public void setLooting(boolean looting) {
    this.looting = looting;
    if (looting) {
      Log.info("State: Started looting process");
      setCurrentState(BotState.LOOTING);
    } else {
      Log.info("State: Finished looting process");
      setCurrentState(BotState.IDLE);
    }
  }

  public boolean isNeedsBanking() {
    return needsBanking;
  }

  public void setNeedsBanking(boolean needsBanking) {
    this.needsBanking = needsBanking;
    if (needsBanking) {
      Log.info("State: Banking needed");
      if (currentState != BotState.RETREATING) {
        setCurrentState(BotState.BANKING);
      }
    } else {
      Log.info("State: Banking no longer needed");
    }
  }

  public boolean isRestoringPrayer() {
    return restoringPrayer;
  }

  public void setRestoringPrayer(boolean restoringPrayer) {
    this.restoringPrayer = restoringPrayer;
    if (restoringPrayer) {
      Log.info("State: Started prayer restoration");
      setCurrentState(BotState.RESTORING_PRAYER);
    } else {
      Log.info("State: Finished prayer restoration");
      setCurrentState(BotState.IDLE);
    }
  }

  public boolean isCombatCooldownElapsed(int currentTick) {
    // Each tick is roughly 0.6 seconds, so convert seconds to ticks
    int cooldownTicks = (int)(COMBAT_COOLDOWN_SECONDS / 0.6);
    return retreatStartTime != -1 && currentTick - retreatStartTime >= cooldownTicks;
  }
}