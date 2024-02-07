/* (C)2024 */
package com.colesturza.pangolin.http;

import java.util.function.Function;

/**
 * Represents middleware in a web application framework.
 *
 * <p>Middleware provides a mechanism to intercept and potentially modify incoming HTTP requests
 * before they are handled by the application's main request handler. Middleware can perform tasks
 * such as authentication, logging, input validation, or any other pre-processing required for
 * requests.
 *
 * <p>The {@code Middleware} interface defines a method to create decorators that wrap a given
 * handler with additional functionality. Decorators are functions that take a handler as input and
 * return another handler that incorporates the middleware logic.
 */
public interface Middleware {

  /**
   * Returns a decorator function that wraps a given handler with additional middleware logic.
   *
   * <p>The decorator function takes a handler as input and returns another handler that
   * incorporates the middleware logic. The returned handler may modify the request or response
   * context, invoke the original handler, or perform other tasks before or after handling the
   * request.
   *
   * @return A function that decorates a handler with additional middleware logic.
   */
  Function<Handler, Handler> decorator();
}
