package main.java.org.rspeer.scripts.f2ppker.domain.config;

import org.rspeer.game.script.meta.ScriptConfig;

/**
 * Builder pattern implementation for creating configuration objects.
 * Allows for chained configuration setup.
 */
public class ConfigBuilder {
  private final ScriptConfig config = new ScriptConfig();

  /**
   * Creates a builder with default configuration values
   */
  public static ConfigBuilder ofDefaults() {
    ConfigBuilder builder = new ConfigBuilder();
    ScriptConfig config = builder.config;
    config.put(ConfigKey.USE_SPECIAL_ATTACK, ConfigDefaults.USE_SPECIAL_ATTACK);
    config.put(ConfigKey.MIN_HEALTH_TO_EAT, ConfigDefaults.MIN_HEALTH_TO_EAT);
    config.put(ConfigKey.MAX_COMBAT_LEVEL, ConfigDefaults.MAX_COMBAT_LEVEL);
    config.put(ConfigKey.MIN_COMBAT_LEVEL, ConfigDefaults.MIN_COMBAT_LEVEL);
    return builder;
  }

  /**
   * Creates a builder from command-line arguments
   */
  public static ConfigBuilder ofArgs(String args) {
    if (args == null || args.isEmpty()) {
      return ofDefaults();
    }

    ConfigBuilder builder = new ConfigBuilder();
    // Parse args format: "key1=value1,key2=value2"
    String[] pairs = args.split(",");
    for (String pair : pairs) {
      String[] keyValue = pair.split("=");
      if (keyValue.length == 2) {
        String key = keyValue[0].trim();
        String value = keyValue[1].trim();

        if (key.equalsIgnoreCase("useSpecial") || key.equalsIgnoreCase("special")) {
          builder.useSpecialAttack(Boolean.parseBoolean(value));
        } else if (key.equalsIgnoreCase("minHealth") || key.equalsIgnoreCase("health")) {
          builder.minHealthToEat(Integer.parseInt(value));
        } else if (key.equalsIgnoreCase("maxLevel") || key.equalsIgnoreCase("maxCombat")) {
          builder.maxCombatLevel(Integer.parseInt(value));
        } else if (key.equalsIgnoreCase("minLevel") || key.equalsIgnoreCase("minCombat")) {
          builder.minCombatLevel(Integer.parseInt(value));
        }
      }
    }

    return builder;
  }

  public ConfigBuilder useSpecialAttack(boolean useSpecial) {
    config.put(ConfigKey.USE_SPECIAL_ATTACK, useSpecial);
    return this;
  }

  public ConfigBuilder minHealthToEat(int health) {
    config.put(ConfigKey.MIN_HEALTH_TO_EAT, health);
    return this;
  }

  public ConfigBuilder maxCombatLevel(int level) {
    config.put(ConfigKey.MAX_COMBAT_LEVEL, level);
    return this;
  }

  public ConfigBuilder minCombatLevel(int level) {
    config.put(ConfigKey.MIN_COMBAT_LEVEL, level);
    return this;
  }

  public ScriptConfig build() {
    return config;
  }
}