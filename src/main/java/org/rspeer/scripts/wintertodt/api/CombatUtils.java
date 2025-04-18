package org.rspeer.scripts.f2ppker.api;

import org.rspeer.game.adapter.component.inventory.Equipment;
import org.rspeer.game.adapter.scene.Player;
import org.rspeer.game.combat.Combat;
import org.rspeer.game.component.Inventories;
import org.rspeer.game.component.Item;
import org.rspeer.game.component.tdi.Skill;
import org.rspeer.game.component.tdi.Skills;
import org.rspeer.game.component.tdi.Prayer;
import org.rspeer.game.component.tdi.Prayers;
import org.rspeer.commons.logging.Log;

import static org.rspeer.scripts.f2ppker.Constants.*;

/**
 * Utility methods for combat operations
 */
public final class CombatUtils {

    private CombatUtils() {
        throw new IllegalAccessError();
    }

    /**
     * Checks if a bow is currently equipped
     */
    public static boolean hasBowEquipped() {
        Equipment equipment = Inventories.equipment();
        Item mainHand = equipment.getItemAt(Equipment.Slot.MAINHAND);
        return mainHand != null && mainHand.getName().equals(MAPLE_SHORTBOW);
    }

    /**
     * Checks if a 2h sword is currently equipped
     */
    public static boolean has2hEquipped() {
        Equipment equipment = Inventories.equipment();
        Item mainHand = equipment.getItemAt(Equipment.Slot.MAINHAND);
        return mainHand != null && mainHand.getName().equals(RUNE_2H);
    }

    /**
     * Attempts to equip a bow
     */
    public static boolean equipBow() {
        if (hasBowEquipped()) {
            return true;
        }

        Item bow = Inventories.backpack().query().nameContains("shortbow").results().first();
        if (bow == null) {
            Log.severe("No bow found in inventory!");
            return false;
        }

        Log.info("Equipping bow");
        return bow.interact("Wield");
    }

    /**
     * Attempts to equip a 2h sword
     */
    public static boolean equip2h() {
        if (has2hEquipped()) {
            return true;
        }

        Item twoHand = Inventories.backpack().query().nameContains("2h sword").results().first();
        if (twoHand == null) {
            Log.severe("No 2h sword found in inventory!");
            return false;
        }

        Log.info("Equipping 2h sword");
        return twoHand.interact("Wield");
    }

    /**
     * Predicts damage based on XP gained
     */
    public static int predictDamageFromXp(int xpGained) {
        return xpGained / 4; // Each 4 XP is roughly 1 damage in OSRS
    }

    /**
     * Determines if the player's strength is boosted
     */
    public static boolean isStrengthBoosted() {
        int currentStr = Skills.getCurrentLevel(Skill.STRENGTH);
        int baseStr = Skills.getLevel(Skill.STRENGTH);
        return currentStr > baseStr;
    }

    /**
     * Activates a specific prayer
     */
    public static boolean activatePrayer(Prayer.Modern prayer) {
        if (!Prayers.isActive(prayer)) {
            Log.info("Activating " + prayer.name());
            return Prayers.toggle(true, prayer);
        }
        return true;
    }

    /**
     * Deactivates a specific prayer
     */
    public static boolean deactivatePrayer(Prayer.Modern prayer) {
        if (Prayers.isActive(prayer)) {
            Log.info("Deactivating " + prayer.name());
            return Prayers.toggle(false, prayer);
        }
        return true;
    }

    /**
     * Eats food from inventory
     */
    public static boolean eatFood() {
        Item food = Inventories.backpack().query().nameContains(SWORDFISH).results().first();
        if (food == null) {
            Log.info("No food found in inventory");
            return false;
        }

        Log.info("Eating food");
        return food.interact("Eat");
    }

    /**
     * Drinks a strength potion
     */
    public static boolean drinkStrengthPotion() {
        if (isStrengthBoosted() && Skills.getCurrentLevel(Skill.STRENGTH) >= BOOSTED_STR) {
            return false;
        }

        Item potion = Inventories.backpack().query().nameContains("Strength potion").results().first();
        if (potion == null) {
            Log.info("No strength potion found in inventory");
            return false;
        }

        Log.info("Drinking strength potion");
        return potion.interact("Drink");
    }
}