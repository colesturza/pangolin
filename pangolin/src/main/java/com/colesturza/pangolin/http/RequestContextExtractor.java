/* (C)2024 */
package com.colesturza.pangolin.http;

import java.util.Map;

public interface RequestContextExtractor {
  HttpMethod method();

  String path();

  Map<String, String> headers();

  Map<String, String> queryParams();

  String body();
}
