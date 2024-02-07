/* (C)2024 */
package com.colesturza.pangolin.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/** Interface representing the context of an HTTP request and response. */
public interface Context {

  // Request Methods

  /** Returns the content length of the request body. */
  int getRequestContentLength();

  /** Returns the content type of the request body. */
  String getRequestContentType();

  /**
   * Returns the request body as a string.
   *
   * @throws IOException
   */
  String getRequestBody() throws IOException;

  /**
   * Returns the request body as a byte array.
   *
   * @throws IOException
   */
  byte[] getRequestBodyAsBytes() throws IOException;

  /**
   * Returns the request body parsed into the specified class.
   *
   * @param clazz The class type to parse the request body into.
   * @param <T> The type of the parsed object.
   * @return The parsed object.
   */
  <T> T getRequestBodyAsClass(Class<T> clazz);

  /** Returns the host of the request. */
  String getRequestHost();

  /** Returns the IP address of the request. */
  String getRequestIp();

  /** Returns the HTTP method of the request. */
  HttpMethod getMethod();

  /** Returns the path of the request. */
  String getPath();

  /** Returns the port of the request. */
  int getRequestPort();

  /** Returns the protocol of the request. */
  String getRequestProtocol();

  /**
   * Returns the value of the specified request header.
   *
   * @param key The header key.
   * @return The header value.
   */
  String getRequestHeader(String key);

  /** Returns a map of all request headers. */
  Map<String, String> getRequestHeaders();

  /**
   * Returns the value of the specified query parameter.
   *
   * @param key The query parameter key.
   * @return The query parameter value.
   */
  String getRequestQueryParameter(String key);

  /**
   * Returns the specified query parameter parsed into the specified class.
   *
   * @param key The query parameter key.
   * @param clazz The class type to parse the query parameter into.
   * @param <T> The type of the parsed object.
   * @return The parsed object.
   */
  <T> T getRequestQueryParameterAsClass(String key, Class<T> clazz);

  /** Returns a map of all query parameters. */
  Map<String, String> getRequestQueryParameters();

  /** Returns the query string of the request. */
  String getRequestQueryString();

  /**
   * Returns the value of the specified path parameter.
   *
   * @param key The path parameter key.
   * @return The path parameter value.
   */
  String getRequestPathParameter(String key);

  /**
   * Returns the specified path parameter parsed into the specified class.
   *
   * @param key The path parameter key.
   * @param clazz The class type to parse the path parameter into.
   * @param <T> The type of the parsed object.
   * @return The parsed object.
   */
  <T> T getRequestPathParameter(String key, Class<T> clazz);

  /** Returns a map of all path parameters. */
  Map<String, String> getRequestPathParameters();

  // Response Methods

  /**
   * Sets the status of the response.
   *
   * @param status The response status.
   */
  void setResponseStatus(int status);

  /** Returns the status of the response. */
  int getResponseStatus();

  /**
   * Sets a header in the response.
   *
   * @param name The header name.
   * @param value The header value.
   */
  void setResponseHeader(String name, String value);

  /**
   * Sets the content type of the response.
   *
   * @param type The content type.
   */
  void setResponseContentType(String type);

  /**
   * Sets the response body as a string.
   *
   * @param s The string body.
   */
  default void setResponseBody(String s) {
    setResponseBody(s.getBytes());
  }

  /**
   * Sets the response body as a byte array.
   *
   * @param buf The byte array body.
   */
  default void setResponseBody(byte[] buf) {
    setResponseBody(new ByteArrayInputStream(buf));
  }

  /**
   * Sets the response body as an input stream.
   *
   * @param stream The input stream body.
   */
  void setResponseBody(InputStream stream);

  /**
   * Sets the response body by serializing the object to JSON.
   *
   * @param obj The object to serialize.
   */
  void setResponseBodyJson(Object obj);

  void sendResponse();
}
