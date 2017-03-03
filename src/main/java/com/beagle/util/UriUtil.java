package com.beagle.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public class UriUtil {

  public static String getUri(String... paths) {
    return Arrays.stream(paths).collect(Collectors.joining());
  }
}
