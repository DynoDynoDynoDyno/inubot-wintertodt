package org.rspeer.scripts.f2ppker.api;

import org.rspeer.game.movement.Movement;
import org.rspeer.game.position.Position;
import org.rspeer.game.scene.Players;
import org.rspeer.commons.logging.Log;

import static org.rspeer.scripts.f2ppker.Constants.*;

/**
 * Utility methods for area and position operations
 */
public final class AreaUtils {

    private AreaUtils() {
        throw new IllegalAccessError();
    }

    /**
     * Checks if the local player is in the safe area
     */
    public static boolean isInSafeArea() {
        return SAFE_AREA.contains(Players.self());
    }

    /**
     * Checks if a position is in the safe area
     */
    public static boolean isInSafeArea(Position position) {
        return SAFE_AREA.contains(position);
    }

    /**
     * Checks if the local player is in the combat area
     */
    public static boolean isInCombatArea() {
        return COMBAT_AREA.contains(Players.self());
    }

    /**
     * Checks if the local player is in the bank area
     */
    public static boolean isInBankArea() {
        return BANK_AREA.contains(Players.self());
    }

    /**
     * Checks if the local player is in the altar area
     */
    public static boolean isInAltarArea() {
        return ALTAR_AREA.contains(Players.self());
    }

    /**
     * Walks to the combat area
     */
    public static boolean walkToCombatArea() {
        Log.info("Walking to combat area");
        return Movement.walkTo(COMBAT_AREA.getCenter());
    }

    /**
     * Walks to the bank area
     */
    public static boolean walkToBankArea() {
        Log.info("Walking to bank area");
        return Movement.walkTo(BANK_AREA.getCenter());
    }

    /**
     * Walks to the altar area
     */
    public static boolean walkToAltarArea() {
        Log.info("Walking to altar area");
        return Movement.walkTo(ALTAR_AREA.getCenter());
    }

    /**
     * Walks to the safe area
     */
    public static boolean walkToSafeArea() {
        Log.info("Walking to safe area");

        // Enable run if we're moving to safety and have energy
        if (!Movement.isRunEnabled() && Movement.getRunEnergy() > 5) {
            Movement.toggleRun(true);
        }

        return Movement.walkTo(SAFE_AREA.getCenter());
    }
}