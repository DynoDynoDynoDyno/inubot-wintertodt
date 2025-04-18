package main.java.org.rspeer.scripts.f2ppker.domain;

import com.google.inject.Singleton;
import org.rspeer.commons.logging.Log;

/**
 * Tracks weapon usage and switching for PKing.
 * Manages 2H and bow switch logic.
 */
@Singleton
public class WeaponTracker {
    // Weapon state tracking
    private boolean rune2hUsedSinceLastBow = false;
    private boolean bowHitRegistered = false;
    private boolean bowAttackRegistered = false;

    // Timing information
    private int lastBowHitTick = 0;
    private int lastBowAttackTick = 0;

    /**
     * Records when a 2H attack is performed
     */
    public void registerRune2hAttack() {
        Log.fine("WeaponTracker: Registered 2H attack");
        rune2hUsedSinceLastBow = true;
        bowHitRegistered = false;
    }

    /**
     * Records when a bow hit (damage) is registered
     */
    public void registerBowHit(int tick) {
        Log.fine("WeaponTracker: Registered bow hit (damage) on tick " + tick);
        lastBowHitTick = tick;
        bowHitRegistered = true;
        rune2hUsedSinceLastBow = false;
    }

    /**
     * Records when a bow attack animation is performed
     */
    public void registerBowAttack(int tick) {
        Log.fine("WeaponTracker: Registered bow attack animation on tick " + tick);
        lastBowAttackTick = tick;
        bowAttackRegistered = true;
        // Don't reset rune2hUsedSinceLastBow here - that only happens on hit
    }

    /**
     * Checks if the player can use Rune 2H
     * We can use 2H if:
     * 1. We haven't used it since our last bow hit
     * 2. We've actually registered a bow attack (not just a hit)
     */
    public boolean canUseRune2h() {
        return !rune2hUsedSinceLastBow && bowAttackRegistered;
    }

    /**
     * Gets the tick when the last bow hit occurred
     */
    public int getLastBowHitTick() {
        return lastBowHitTick;
    }

    /**
     * Gets the tick when the last bow attack occurred
     */
    public int getLastBowAttackTick() {
        return lastBowAttackTick;
    }

    /**
     * Resets all weapon tracking state
     */
    public void reset() {
        Log.fine("WeaponTracker: Reset weapon state tracking");
        rune2hUsedSinceLastBow = false;
        bowHitRegistered = false;
        bowAttackRegistered = false;
        lastBowHitTick = 0;
        lastBowAttackTick = 0;
    }
}
