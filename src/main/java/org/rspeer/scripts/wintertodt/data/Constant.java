package main.java.org.rspeer.scripts.f2ppker.data;

import org.rspeer.game.position.AbsolutePosition;
import org.rspeer.game.position.Position;
import org.rspeer.game.position.area.Area;

import java.util.Set;

/**
 * Constants used throughout the F2P PKer script.
 * Includes item names, areas, animations, timings, etc.
 */
public class Constant {
  // Food items
  public static final String SWORDFISH = "Swordfish";
  public static final int SWORDFISH_QTY = 26;
  public static final int MIN_FOOD_COUNT = 5;

  // Potions
  public static final String STRENGTH_POTION = "Strength potion(4)";
  public static final int STRENGTH_POTION_QTY = 1;

  // Weapons
  public static final String RUNE_2H = "Rune 2h sword";
  public static final int RUNE_2H_QTY = 1;
  public static final String MAPLE_SHORTBOW = "Maple shortbow";

  // Ammunition
  public static final String ADAMANT_ARROWS = "Adamant arrow";
  public static final int ADAMANT_ARROWS_QTY = 100;

  // Armor and equipment
  public static final String AMULET_OF_STRENGTH = "Amulet of strength";
  public static final String GREEN_DHIDE_CHAPS = "Green d'hide chaps";
  public static final String GREEN_DHIDE_VAMBRACES = "Green d'hide vambraces";
  public static final String LEATHER_BOOTS = "Leather boots";

  // Other items
  public static final String LOOT_KEY = "Loot key";

  // Areas
  public static final Area ALTAR_AREA = Area.rectangular(3240, 3209, 3247, 3204, 0);
  public static final Area BANK_AREA = Area.rectangular(3220, 3218, 3223, 3215, 0);
  public static final Area COMBAT_AREA = Area.rectangular(3232, 3221, 3238, 3215, 0);
  public static final Area SAFE_AREA = Area.polygonal(
          new AbsolutePosition(3199, 3237, 0),
          new AbsolutePosition(3199, 3200, 0),
          new AbsolutePosition(3230, 3200, 0),
          new AbsolutePosition(3232, 3200, 0),
          new AbsolutePosition(3232, 3226, 0),
          new AbsolutePosition(3221, 3236, 0)
  );

  // Animation IDs
  public static final int BOW_ATTACK_ANIMATION = 426;
  public static final int EAT_ANIMATION = 829;
  public static final int DRINK_ANIMATION = 829;  // Same as eating
  public static final int RUNE_2H_ATTACK_ANIMATION = 407;
  public static final int HILL_GIANT_CLUB_ATTACK_ANIMATION = 2661;
  public static final int RUNE_BATTLEAXE_ATTACK_ANIMATION = 395;
  public static final int RUNE_WARHAMMER_ATTACK_ANIMATION = 401;
  public static final int COMBAT_SPELL_ANIMATION = 711;
  public static final int RUNE_SCIMITAR_ATTACK_ANIMATION = 390;

  // Set of animations that indicate an attack
  public static final Set<Integer> ATTACK_ANIMATIONS = Set.of(
          BOW_ATTACK_ANIMATION,
          RUNE_2H_ATTACK_ANIMATION,
          HILL_GIANT_CLUB_ATTACK_ANIMATION,
          RUNE_BATTLEAXE_ATTACK_ANIMATION,
          RUNE_WARHAMMER_ATTACK_ANIMATION,
          COMBAT_SPELL_ANIMATION,
          RUNE_SCIMITAR_ATTACK_ANIMATION
  );

  // Strings used to identify heavy armor (we don't want to target these players)
  public static final String PLATEBODY_KEYWORD = "Rune platebody";
  public static final String PLATELEGS_KEYWORD = "Rune platelegs";
  public static final String PLATESKIRT_KEYWORD = "Runeplateskirt";
  public static final String HELM_KEYWORD = "Rune fullhelm";
  public static final String SHIELD_KEYWORD = "Rune kiteshield";
  public static final String GREEN_DHIDE_BODY_KEYWORD = "Green d'hide body";

  public static final Set<String> EQUIPMENT_KEYWORDS = Set.of(
          PLATEBODY_KEYWORD,
          PLATELEGS_KEYWORD,
          PLATESKIRT_KEYWORD,
          HELM_KEYWORD,
          SHIELD_KEYWORD,
          GREEN_DHIDE_BODY_KEYWORD
  );

  // Timings and cooldowns
  public static final int COMBAT_COOLDOWN_TICKS = 15;
  public static final int BOW_SPEED = 3;
  public static final int RUNE_2H_SPEED = 7;
  public static final int EAT_SPEED = 3;
  public static final int POTION_SPEED = 3;

  // Prayer
  public static final int MIN_PRAYER_POINTS = 10;

  // Stat thresholds
  public static final int BOOSTED_STR = 110;
}