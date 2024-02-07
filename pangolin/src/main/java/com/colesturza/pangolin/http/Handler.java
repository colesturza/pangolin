/* (C)2024 */
package com.colesturza.pangolin.http;

/**
 * Represents a handler for processing HTTP requests in a web application framework.
 *
 * <p>Handlers are responsible for processing incoming HTTP requests and generating appropriate
 * responses. Each handler typically corresponds to a specific route or endpoint in the application,
 * defining the logic to execute when a request is received at that route.
 *
 * <p>The {@code Handler} interface defines a single method, {@code serve()}, which takes a {@code
 * Context} object representing the request context. Implementations of this interface provide the
 * logic to handle the request and generate the response.
 */
public interface Handler {
  /**
   * Handles an incoming HTTP request and generates an appropriate response.
   *
   * <p>This method is invoked when an HTTP request is received at a route or endpoint associated
   * with this handler. The {@code ctx} parameter contains the context information for the incoming
   * request, including request parameters, headers, and other relevant data.
   *
   * <p>The implementation of this method should process the request, perform any necessary actions,
   * and generate an appropriate response, which may include setting response headers, writing
   * response content, or forwarding the request to another handler.
   *
   * @param ctx The context object containing information about the incoming HTTP request.
   */
  void serve(Context ctx);
}
