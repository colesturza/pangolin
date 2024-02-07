/* (C)2024 */
package com.colesturza.pangolin.http;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class QueryParamParser {
  public static Map<String, String> parseQueryParams(String queryString) {
    if (queryString == null || queryString.isEmpty()) {
      return ImmutableMap.of();
    }

    Map<String, String> queryParams = new HashMap<>();
    Iterable<String> pairs = Splitter.on('&').split(queryString);
    for (String pair : pairs) {
      int idx = pair.indexOf('=');
      if (idx != -1) {
        String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
        String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
        queryParams.put(key, value);
      }
    }
    return ImmutableMap.copyOf(queryParams);
  }
}
