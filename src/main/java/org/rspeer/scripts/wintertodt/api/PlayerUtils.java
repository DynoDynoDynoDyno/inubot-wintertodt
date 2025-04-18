package org.rspeer.scripts.f2ppker.api;

import org.rspeer.game.adapter.scene.Player;
import org.rspeer.game.scene.Players;
import org.rspeer.commons.logging.Log;

import static org.rspeer.scripts.f2ppker.Constants.*;

/**
 * Utility methods for player-related operations
 */
public final class PlayerUtils {

    private PlayerUtils() {
        throw new IllegalAccessError();
    }

    /**
     * Find potential PK targets based on configuration
     */
    public static Player findPotentialTarget(int minCombatLevel, int maxCombatLevel) {
        return Players.query()
                .filter(p -> !p.equals(Players.self()))
                .filter(p -> p.getCombatLevel() >= minCombatLevel && p.getCombatLevel() <= maxCombatLevel)
                .filter(p -> p.getTarget() == null) // not targeting others
                .filter(p -> Players.query().targeting(p).results().isEmpty()) // not being targeted
                .filter(p -> !SAFE_AREA.contains(p.getPosition())) // not in safe area
                .filter(p -> !isWearingHeavyArmor(p)) // not wearing heavy armor
                .results()
                .first();
    }

    /**
     * Gets the nearest player who is attacking you
     */
    public static Player getAttacker() {
        return Players.query()
                .targeting(Players.self())
                .filter(p -> ATTACK_ANIMATIONS.contains(p.getAnimationId()))
                .filter(p -> !SAFE_AREA.contains(p.getPosition()))
                .results()
                .nearest();
    }

    /**
     * Gets a player by name
     */
    public static Player getPlayerByName(String name) {
        if (name == null) {
            return null;
        }
        return Players.query().names(name).results().first();
    }

    /**
     * Checks if a player appears to be wearing heavy armor
     */
    public static boolean isWearingHeavyArmor(Player player) {
        if (player.getAppearance() == null) {
            return false;
        }

        for (String keyword : EQUIPMENT_KEYWORDS) {
            if (player.getAppearance().query().nameContains(keyword).results().size() > 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a player is valid and still targetable
     */
    public static boolean isValidTarget(Player player) {
        return player != null &&
                !SAFE_AREA.contains(player.getPosition()) &&
                (player.getTarget() == null || player.getTarget().equals(Players.self()));
    }
}