package org.rspeer.scripts.f2ppker.data;

import org.rspeer.scripts.f2ppker.domain.Domain;

/**
 * Enum containing possible PKing actions and their durations in ticks.
 * Defines animations and cooldowns for combat actions.
 */
public enum Action {
  BOW_ATTACK(Constant.BOW_ATTACK_ANIMATION, Constant.BOW_SPEED),
  RUNE_2H_ATTACK(Constant.RUNE_2H_ATTACK_ANIMATION, Constant.RUNE_2H_SPEED),
  EAT_FOOD(Constant.EAT_ANIMATION, Constant.EAT_SPEED),
  DRINK_POTION(Constant.DRINK_ANIMATION, Constant.POTION_SPEED);

  private final int animation;
  private final int duration;

  Action(int animation, int duration) {
    this.animation = animation;
    this.duration = duration;
  }

  public int getAnimation() {
    return animation;
  }

  public int getTickDuration() {
    return duration;
  }

  /**
   * Checks if the action is currently active
   */
  public boolean isActive(Domain domain) {
    return domain.getState().getLastAnimation() == animation &&
            !domain.getTimers().isIdle(duration);
  }
}