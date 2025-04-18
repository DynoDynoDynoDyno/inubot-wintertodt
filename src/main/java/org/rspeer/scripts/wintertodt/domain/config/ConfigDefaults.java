package main.java.org.rspeer.scripts.f2ppker.domain.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Default values for configuration options.
 * Used when configuration is not explicitly provided.
 */
public interface ConfigDefaults {
  boolean USE_SPECIAL_ATTACK = true;
  int MIN_HEALTH_TO_EAT = 31;
  int MAX_COMBAT_LEVEL = 97;
  int MIN_COMBAT_LEVEL = 67;

  /**
   * Helper method to get all default values
   */
  static List<Object> values() {
    List<Object> values = new ArrayList<>();
    for (Field field : ConfigDefaults.class.getDeclaredFields()) {
      try {
        values.add(field.get(null));
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
    return values;
  }
}