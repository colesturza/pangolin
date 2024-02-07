/* (C)2024 */
package com.colesturza.pangolin.http.impl;

import com.colesturza.pangolin.http.Context;
import com.colesturza.pangolin.http.Handler;
import com.colesturza.pangolin.http.HttpMethod;
import com.colesturza.pangolin.http.Middleware;
import com.colesturza.pangolin.http.Router;
import com.colesturza.pangolin.internal.ValidationUtil;
import com.google.common.base.Splitter;
import com.google.common.net.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * A router implementation based on the Trie data structure for efficient URL routing.
 *
 * <p>This router class organizes URL patterns using a Trie (prefix tree) data structure, allowing
 * for efficient matching of incoming URLs against registered patterns.
 *
 * <p>Each node in the Trie represents a segment of a URL pattern, and edges between nodes represent
 * possible transitions from one segment to another. This enables fast traversal through the Trie to
 * find the appropriate handler for a given URL.
 *
 * <p>The {@code TrieRouter} class provides methods to insert URL patterns along with their
 * corresponding handler functions into the router and to match incoming requests to the appropriate
 * handlers based on the request method and URL.
 *
 * <p><b>Note:</b> This router implementation supports the use of path parameters in URL patterns,
 * allowing dynamic routing based on variable segments of the URL.
 */
public class DefaultTrieRouter implements Router {

  /**
   * Represents a node in the Trie data structure used by the TrieRouter for efficient URL routing.
   *
   * <p>A TrieNode stores information about a segment of a URL pattern and its associated handler,
   * if applicable. Each node may have child nodes representing subsequent segments of the URL,
   * forming a hierarchical structure that allows for fast traversal and lookup.
   *
   * <p>The TrieNode class maintains references to its children nodes using a map, where the keys
   * are string representations of the next segment of the URL and the values are corresponding
   * child TrieNode objects. The isEndOfWord flag indicates whether the current node represents the
   * end of a URL pattern.
   *
   * <p>If a node represents a path parameter in a URL pattern, the parameterName field stores the
   * name of the parameter, and the regexPattern field may store a regular expression pattern
   * specifying constraints on the parameter value.
   *
   * <p>The handler field holds a reference to the handler function associated with the URL pattern
   * represented by the node. When a matching URL is found during routing, the corresponding handler
   * function is executed to process the request.
   *
   * <p><b>Note:</b> This class is private and encapsulated within the TrieRouter implementation.
   *
   * <p><b>Note:</b> In the map, a colon (':') is used to represent a path parameter in the URL
   * pattern.
   */
  private static class TrieNode {
    final Map<String, TrieNode> children;
    final Map<HttpMethod, Handler> handlers;
    boolean isEndOfWord;
    String parameterName;
    String regexPattern;

    TrieNode() {
      children = new HashMap<>();
      handlers = new HashMap<>();
    }
  }

  private static void defaultNotFound(Context ctx) {
    int status = 404;
    ctx.setResponseStatus(status);
    ctx.setResponseContentType(MediaType.PLAIN_TEXT_UTF_8.toString());
    ctx.setResponseBody("404 Not Found");
  }

  private static void defaultMethodNotAllowed(Context ctx) {
    int status = 405;
    ctx.setResponseStatus(status);
    ctx.setResponseContentType(MediaType.PLAIN_TEXT_UTF_8.toString());
    ctx.setResponseBody("405 Method Not Allowed");
  }

  private final TrieNode root;
  private Function<Handler, Handler> middlewareChain;
  private Handler notFound;
  private Handler methodNotAllowed;

  /**
   * Constructs a new TrieRouter with an empty root node and default handlers for not found and
   * method not allowed cases.
   */
  public DefaultTrieRouter() {
    root = new TrieNode();
    middlewareChain = null;
    notFound = DefaultTrieRouter::defaultNotFound;
    methodNotAllowed = DefaultTrieRouter::defaultMethodNotAllowed;
  }

  /**
   * Inserts a URL pattern and its corresponding handler for a specific HTTP method into the router.
   *
   * <p>This method associates the provided URL pattern with the given HTTP method and handler,
   * allowing the router to route requests to the specified handler when the incoming request
   * matches the provided method and pattern.
   *
   * <p>The URL pattern can contain path parameters denoted by a colon followed by a parameter name
   * (valid Java identifier) optionally followed by a regex pattern enclosed in parentheses. For
   * example, "/users/:id" or "/products/:code([A-Za-z0-9]+)". When a request matches a pattern
   * containing path parameters, the parameter values are extracted and passed to the handler.
   *
   * <p><b>Note:</b> The regex pattern for path parameters allows specifying constraints on the
   * parameter values. If a regex pattern is not provided, the default pattern matching any
   * character (except '/') is used.
   *
   * @param method the HTTP method for which the pattern and handler are being inserted.
   * @param pattern the URL pattern to be associated with the method and handler.
   * @param handler the handler function to be executed when the incoming request matches the method
   *     and pattern.
   * @throws NullPointerException if method, pattern, or handler is null.
   * @throws IllegalArgumentException if pattern is blank or if the regex pattern for path
   *     parameters is invalid.
   */
  @Override
  public void method(final HttpMethod method, final String pattern, final Handler handler) {

    if (method == null) {
      throw new NullPointerException("parameter `method` must not be null");
    }

    if (pattern == null) {
      throw new NullPointerException("parameter `pattern` must not be null");
    }

    if (pattern.isBlank()) {
      throw new IllegalArgumentException("parameter `pattern` must not be blank");
    }

    if (handler == null) {
      throw new NullPointerException("parameter `handler` must not be null");
    }

    TrieNode node = root;

    final Iterable<String> parts = Splitter.on("/").split(pattern);
    for (String part : parts) {

      if (part.isBlank()) {
        // ignore blank path segments
        continue;
      }

      final boolean hasPathParameter = part.charAt(0) == ':';
      String parameterName = null;
      String regexPattern = null;

      if (hasPathParameter) {
        parameterName = part.substring(1);

        final int patternStartIndex = part.indexOf('(');
        final int patternEndIndex = part.indexOf(')');
        if (patternStartIndex != -1 && patternEndIndex != -1) {
          parameterName = part.substring(1, patternStartIndex);
          regexPattern = part.substring(patternStartIndex + 1, patternEndIndex);
          if (!ValidationUtil.isValidRegexPattern(regexPattern)) {
            throw new IllegalArgumentException(
                String.format(
                    "%s is not a valid regex pattern for path parameter %s",
                    regexPattern, parameterName));
          }
        }

        if (!ValidationUtil.isValidJavaIdentifier(parameterName)) {
          throw new IllegalArgumentException(
              String.format("path parameter %s is not a valid identifier name", parameterName));
        }

        part = ":";
      }

      if (!node.children.containsKey(part)) {
        node.children.put(part, new TrieNode());
      }

      if (hasPathParameter) {
        node.children.get(part).parameterName = parameterName;
        node.children.get(part).regexPattern = regexPattern;
      }

      node = node.children.get(part);
    }

    node.isEndOfWord = true;
    node.handlers.put(method, handler);
  }

  /** {@inheritDoc} */
  @Override
  public void use(Middleware... middlewares) {
    for (Middleware middleware : middlewares) {
      this.middlewareChain = middleware.decorator().compose(this.middlewareChain);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void notFound(Handler handler) {
    this.notFound = handler;
  }

  /** {@inheritDoc} */
  @Override
  public void methodNotAllowed(Handler handler) {
    this.methodNotAllowed = handler;
  }

  /** {@inheritDoc} */
  @Override
  public void serve(Context ctx) {
    TrieNode node = root;

    if (node == null) {
      notFound.serve(ctx);
      return;
    }

    final Iterable<String> parts = Splitter.on("/").split(ctx.getPath());
    for (final String part : parts) {

      if (part.isBlank()) {
        // ignore blank path segments
        continue;
      }

      if (node.children.containsKey(part)) {
        node = node.children.get(part);
      } else if (node.children.containsKey(":")) {
        final String parameterName = node.children.get(":").parameterName;
        final String regexPattern = node.children.get(":").regexPattern;
        if (regexPattern != null && !Pattern.matches(regexPattern, part)) {
          notFound.serve(ctx);
          return;
        }
        ctx.getRequestPathParameters().put(parameterName, part);
        node = node.children.get(":");
      } else {
        notFound.serve(ctx);
        return;
      }
    }

    if (!node.isEndOfWord) {
      notFound.serve(ctx);
      return;
    }

    Handler handler = node.handlers.getOrDefault(ctx.getMethod(), methodNotAllowed);
    if (middlewareChain != null) {
      middlewareChain.apply(handler).serve(ctx);
    } else {
      handler.serve(ctx);
    }
  }
}
