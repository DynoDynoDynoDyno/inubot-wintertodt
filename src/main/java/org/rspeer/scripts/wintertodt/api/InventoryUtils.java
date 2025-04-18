package org.rspeer.scripts.f2ppker.api;

import org.rspeer.game.adapter.component.inventory.Bank;
import org.rspeer.game.component.Inventories;
import org.rspeer.game.component.tdi.Tab;
import org.rspeer.game.component.tdi.Tabs;
import org.rspeer.commons.logging.Log;

import static org.rspeer.scripts.f2ppker.Constants.*;

/**
 * Utility methods for inventory operations
 */
public final class InventoryUtils {

    private InventoryUtils() {
        throw new IllegalAccessError();
    }

    /**
     * Gets the count of food items in inventory
     */
    public static int getFoodCount() {
        return Inventories.backpack().getCount(SWORDFISH);
    }

    /**
     * Checks if food is low
     */
    public static boolean isFoodLow() {
        return getFoodCount() <= MIN_FOOD_COUNT;
    }

    /**
     * Checks if the player has a loot key
     */
    public static boolean hasLootKey() {
        return Inventories.backpack().contains("Loot key");
    }

    /**
     * Checks if all required PK gear is present
     */
    public static boolean hasRequiredPkGear() {
        boolean hasBow = CombatUtils.hasBowEquipped() ||
                Inventories.backpack().contains(MAPLE_SHORTBOW);
        boolean has2h = CombatUtils.has2hEquipped() ||
                Inventories.backpack().contains(RUNE_2H);
        boolean hasFood = getFoodCount() >= MIN_FOOD_COUNT;

        return hasBow && has2h && hasFood;
    }

    /**
     * Opens the bank if it's not already open
     */
    public static boolean openBank() {
        if (Bank.isOpen()) {
            return true;
        }

        if (!AreaUtils.isInBankArea()) {
            AreaUtils.walkToBankArea();
            return false;
        }

        return Bank.open();
    }

    /**
     * Ensures the inventory tab is open
     */
    public static boolean openInventoryTab() {
        if (!Tabs.isOpen(Tab.INVENTORY)) {
            return Tabs.open(Tab.INVENTORY);
        }
        return true;
    }
}