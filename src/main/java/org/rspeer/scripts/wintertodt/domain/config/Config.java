package main.java.org.rspeer.scripts.f2ppker.domain.config;

import com.google.inject.Singleton;
import org.rspeer.commons.logging.Log;
import org.rspeer.game.component.Inventories;
import org.rspeer.game.adapter.component.inventory.Equipment;
import org.rspeer.game.component.Item;
import org.rspeer.game.script.meta.ScriptConfig;
import main.java.org.rspeer.scripts.f2ppker.data.Constant;

/**
 * Manages configuration for the F2P PKer script.
 * Handles user-configurable settings from the UI.
 */
@Singleton
public class Config {
  // UI Configurable options
  private boolean useSpecialAttack;
  private int minHealthToEat = 31;
  private int maxCombatLevel = 97;
  private int minCombatLevel = 67;

  // Flag to indicate if configuration is complete
  private boolean complete = false;

  /**
   * Initialize configuration from ScriptConfigEvent
   */
  public void initialize(ScriptConfig config) {
    useSpecialAttack = config.getBoolean(ConfigKey.USE_SPECIAL_ATTACK);
    minHealthToEat = config.getInteger(ConfigKey.MIN_HEALTH_TO_EAT);
    maxCombatLevel = config.getInteger(ConfigKey.MAX_COMBAT_LEVEL);
    minCombatLevel = config.getInteger(ConfigKey.MIN_COMBAT_LEVEL);

    Log.info("Config: F2P PKer initialized with: Health=" + minHealthToEat +
            ", Combat Range=" + minCombatLevel + "-" + maxCombatLevel +
            ", Special=" + useSpecialAttack);

    complete = true;
  }

  /**
   * Initialize with a provided builder
   */
  public void initialize(ConfigBuilder builder) {
    initialize(builder.build());
  }

  /**
   * Checks if the player has all required equipment for PKing
   */
  public boolean hasRequiredEquipment() {
    // Check for bow
    Equipment equipment = Inventories.equipment();
    Item mainHand = equipment.getItemAt(Equipment.Slot.MAINHAND);
    boolean hasBow = mainHand != null && mainHand.getName().equals(Constant.MAPLE_SHORTBOW);

    // Check for essential items in inventory
    boolean has2H = Inventories.backpack().contains(Constant.RUNE_2H);
    boolean hasFood = Inventories.backpack().contains(Constant.SWORDFISH);

    return hasBow && has2H && hasFood;
  }

  /**
   * Checks if the player has adequate food for PKing
   */
  public boolean hasAdequateFood() {
    return Inventories.backpack().getCount(item ->
            item.getName().equals(Constant.SWORDFISH)) >= Constant.MIN_FOOD_COUNT;
  }

  // Getters
  public boolean isUseSpecialAttack() {
    return useSpecialAttack;
  }

  public int getMinHealthToEat() {
    return minHealthToEat;
  }

  public int getMaxCombatLevel() {
    return maxCombatLevel;
  }

  public int getMinCombatLevel() {
    return minCombatLevel;
  }

  public boolean isComplete() {
    return complete;
  }
}