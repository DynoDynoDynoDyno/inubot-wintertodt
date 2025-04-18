package main.java.org.rspeer.scripts.f2ppker.domain.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration keys used in the UI configuration.
 * These must match exactly what is defined in the @ScriptUI annotation.
 */
public interface ConfigKey {
  String USE_SPECIAL_ATTACK = "Use special attack";
  String MIN_HEALTH_TO_EAT = "Min health to eat";
  String MAX_COMBAT_LEVEL = "Max combat level";
  String MIN_COMBAT_LEVEL = "Min combat level";

  /**
   * Helper method to get all config key names
   */
  static List<String> names() {
    List<String> values = new ArrayList<>();
    for (Field field : ConfigKey.class.getDeclaredFields()) {
      try {
        values.add((String) field.get(null));
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
    return values;
  }
}