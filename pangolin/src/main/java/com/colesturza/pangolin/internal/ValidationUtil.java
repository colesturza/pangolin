/* (C)2024 */
package com.colesturza.pangolin.internal;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public final class ValidationUtil {

  private ValidationUtil() {}

  /**
   * Checks if the provided string is a valid Java identifier.
   *
   * @param identifier the string to be checked.
   * @return {@code true} if the string is a valid Java identifier, {@code false} otherwise.
   */
  public static boolean isValidJavaIdentifier(String identifier) {
    if (identifier == null || identifier.isBlank()) {
      return false;
    }
    if (!Character.isJavaIdentifierStart(identifier.charAt(0))) {
      return false;
    }
    for (int i = 1; i < identifier.length(); i++) {
      if (!Character.isJavaIdentifierPart(identifier.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if the provided string is a valid regex pattern.
   *
   * @param regex the regex pattern to be checked.
   * @return {@code true} if the regex pattern is valid, {@code false} otherwise.
   */
  public static boolean isValidRegexPattern(String pattern) {
    try {
      Pattern.compile(pattern);
      return true;
    } catch (PatternSyntaxException ignored) {
      return false;
    }
  }
}
