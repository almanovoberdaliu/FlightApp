package org.flighApp.utils;

import java.util.ArrayList;
import java.util.List;

public class Utils {
  public static <T> List<T> nullSafe(List<T> list) {
    if (list == null) {
      return new ArrayList<>();
    }
    return list;
  }

  public static <T> boolean notNull(T element) {
    return element != null;
  }

  public static <T> boolean isNull(T element) {
    return element == null;
  }
}
