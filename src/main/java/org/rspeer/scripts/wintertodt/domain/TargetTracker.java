package main.java.org.rspeer.scripts.f2ppker.domain;

import com.google.inject.Singleton;
import org.rspeer.commons.logging.Log;
import org.rspeer.game.Vars;
import org.rspeer.game.component.tdi.Skill;
import org.rspeer.game.event.SkillEvent;

/**
 * Tracks and manages PKing target information.
 * Handles current targets, pending targets, and damage prediction.
 */
@Singleton
public class TargetTracker {
    // Target state
    private String targetName = null;
    private String pendingTargetName = null;

    // Damage prediction state
    private int lastSkillChange = 0;
    private boolean damagePredictionValid = false;

    /**
     * Processes skill changes for damage prediction
     */
    public void skillChange(SkillEvent event) {
        if (event.getSource() == Skill.RANGED) {
            lastSkillChange = event.getChange();
            damagePredictionValid = lastSkillChange > 0;
            Log.fine("TargetTracker: Ranged skill change detected: " +
                    lastSkillChange + ", prediction valid: " + damagePredictionValid);
        }
    }

    /**
     * Gets the current confirmed target
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * Sets a confirmed target
     */
    public void setTargetName(String targetName) {
        Log.info("TargetTracker: Setting target: " + targetName);
        this.targetName = targetName;
    }

    /**
     * Clears the current target
     */
    public void clearTarget() {
        Log.info("TargetTracker: Target cleared");
        this.targetName = null;
        resetDamagePrediction();
    }

    /**
     * Gets the current pending target
     */
    public String getPendingTargetName() {
        return pendingTargetName;
    }

    /**
     * Sets a pending target (not yet confirmed in combat)
     */
    public void setPendingTargetName(String targetName) {
        Log.info("TargetTracker: Setting pending target: " + targetName);
        this.pendingTargetName = targetName;
    }

    /**
     * Confirms a pending target as the current target
     */
    public void confirmPendingTarget() {
        if (pendingTargetName != null) {
            Log.info("TargetTracker: Confirming pending target: " + pendingTargetName);
            setTargetName(pendingTargetName);
            pendingTargetName = null;
        }
    }

    /**
     * Clears a pending target
     */
    public void clearPendingTarget() {
        if (pendingTargetName != null) {
            Log.info("TargetTracker: Clearing pending target: " + pendingTargetName);
            pendingTargetName = null;
        }
    }

    /**
     * Gets the last skill experience change
     */
    public int getLastSkillChange() {
        return lastSkillChange;
    }

    /**
     * Checks if damage prediction is currently valid
     */
    public boolean isDamagePredictionValid() {
        return damagePredictionValid;
    }

    /**
     * Resets damage prediction values
     */
    public void resetDamagePrediction() {
        lastSkillChange = 0;
        damagePredictionValid = false;
        Log.fine("TargetTracker: Reset damage prediction values");
    }

    /**
     * Gets the current target's hitpoints
     * Uses VARBIT 6099 which is the opponent's health value
     */
    public int getTargetHitpoints() {
        return Vars.get(Vars.Type.VARBIT, 6099);
    }
}