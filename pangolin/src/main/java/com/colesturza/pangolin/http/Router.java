/* (C)2024 */
package com.colesturza.pangolin.http;

/**
 * Router interface represents a mechanism for routing HTTP requests to appropriate handlers. It
 * extends the Handler interface, which defines the basic contract for handling HTTP requests.
 */
public interface Router extends Handler {

  /**
   * Adds one or more middleware to the router. Middleware intercepts and potentially modifies
   * requests before they are handled by the router's handlers.
   *
   * @param middlewares One or more Middleware instances to be added to the router.
   */
  void use(Middleware... middlewares);

  /**
   * Registers a handler for a specific HTTP method and URL pattern.
   *
   * @param method The HTTP method for which the handler is registered (e.g., GET, POST).
   * @param pattern The URL pattern to match against incoming requests.
   * @param handler The handler responsible for processing requests that match the method and
   *     pattern.
   */
  void method(HttpMethod method, String pattern, Handler handler);

  /**
   * Convenience method to register a handler for all HTTP methods.
   *
   * @param pattern The URL pattern to match against incoming requests.
   * @param handler The handler responsible for processing requests.
   */
  public default void handle(String pattern, Handler handler) {
    for (HttpMethod method : HttpMethod.values()) {
      method(method, pattern, handler);
    }
  }

  /**
   * Convenience method to register a handler for the GET HTTP method.
   *
   * @param pattern The URL pattern to match against incoming GET requests.
   * @param handler The handler responsible for processing GET requests.
   */
  public default void get(String pattern, Handler handler) {
    method(HttpMethod.GET, pattern, handler);
  }

  /**
   * Convenience method to register a handler for the HEAD HTTP method.
   *
   * @param pattern The URL pattern to match against incoming HEAD requests.
   * @param handler The handler responsible for processing HEAD requests.
   */
  public default void head(String pattern, Handler handler) {
    method(HttpMethod.HEAD, pattern, handler);
  }

  /**
   * Convenience method to register a handler for the POST HTTP method.
   *
   * @param pattern The URL pattern to match against incoming POST requests.
   * @param handler The handler responsible for processing POST requests.
   */
  public default void post(String pattern, Handler handler) {
    method(HttpMethod.POST, pattern, handler);
  }

  /**
   * Convenience method to register a handler for the PUT HTTP method.
   *
   * @param pattern The URL pattern to match against incoming PUT requests.
   * @param handler The handler responsible for processing PUT requests.
   */
  public default void put(String pattern, Handler handler) {
    method(HttpMethod.PUT, pattern, handler);
  }

  /**
   * Convenience method to register a handler for the DELETE HTTP method.
   *
   * @param pattern The URL pattern to match against incoming DELETE requests.
   * @param handler The handler responsible for processing DELETE requests.
   */
  public default void delete(String pattern, Handler handler) {
    method(HttpMethod.DELETE, pattern, handler);
  }

  /**
   * Convenience method to register a handler for the CONNECT HTTP method.
   *
   * @param pattern The URL pattern to match against incoming CONNECT requests.
   * @param handler The handler responsible for processing CONNECT requests.
   */
  public default void connect(String pattern, Handler handler) {
    method(HttpMethod.CONNECT, pattern, handler);
  }

  /**
   * Convenience method to register a handler for the OPTIONS HTTP method.
   *
   * @param pattern The URL pattern to match against incoming OPTIONS requests.
   * @param handler The handler responsible for processing OPTIONS requests.
   */
  public default void options(String pattern, Handler handler) {
    method(HttpMethod.OPTIONS, pattern, handler);
  }

  /**
   * Convenience method to register a handler for the TRACE HTTP method.
   *
   * @param pattern The URL pattern to match against incoming TRACE requests.
   * @param handler The handler responsible for processing TRACE requests.
   */
  public default void trace(String pattern, Handler handler) {
    method(HttpMethod.TRACE, pattern, handler);
  }

  /**
   * Convenience method to register a handler for the PATCH HTTP method.
   *
   * @param pattern The URL pattern to match against incoming PATCH requests.
   * @param handler The handler responsible for processing PATCH requests.
   */
  public default void patch(String pattern, Handler handler) {
    method(HttpMethod.PATCH, pattern, handler);
  }

  /**
   * Sets the handler for requests that do not match any registered route.
   *
   * @param handler The handler to be invoked for handling not found errors.
   */
  public void notFound(Handler handler);

  /**
   * Sets the handler for requests that match a route but do not allow the method used.
   *
   * @param handler The handler to be invoked for handling method not allowed errors.
   */
  public void methodNotAllowed(Handler handler);
}
