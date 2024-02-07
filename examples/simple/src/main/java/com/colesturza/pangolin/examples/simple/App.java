/* (C)2024 */
package com.colesturza.pangolin.examples.simple;

import com.colesturza.pangolin.http.Context;
import com.colesturza.pangolin.http.HttpMethod;
import com.colesturza.pangolin.http.impl.DefaultTrieRouter;
import com.colesturza.pangolin.http.impl.jetty.JettyServlet;
import org.eclipse.jetty.ee9.servlet.ServletContextHandler;
import org.eclipse.jetty.ee9.servlet.ServletHolder;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

/** Note: this is a wip and does not reflect a stable state of pangolin. */
public class App {

  public static void main(String[] args) throws Exception {
    DefaultTrieRouter router = new DefaultTrieRouter();
    router.method(HttpMethod.GET, "/hello", (ctx) -> handleHello(ctx));
    router.method(HttpMethod.GET, "/hello/:name", (ctx) -> handleHello(ctx));
    router.method(HttpMethod.GET, "/user/:id([1-9]\\d*|0)", (ctx) -> handleUser(ctx));
    router.method(HttpMethod.GET, "/error", (ctx) -> handleError(ctx));
    Server server = new Server(8080);
    Connector connector = new ServerConnector(server);
    server.addConnector(connector);
    ServletContextHandler contextHandler = new ServletContextHandler();
    contextHandler.setContextPath("/");
    contextHandler.addServlet(new ServletHolder(new JettyServlet(router)), "/");
    server.setHandler(contextHandler);
    server.start();
  }

  private static void handleHello(Context ctx) {
    String name = ctx.getRequestPathParameters().getOrDefault("name", "world");
    String surname = ctx.getRequestQueryParameter("surname");
    String result =
        String.format(
            "Hello %s%s!", name, surname != null && !surname.isBlank() ? " " + surname : "");
    ctx.setResponseStatus(HttpStatus.OK_200);
    ctx.setResponseBody(result);
  }

  private static void handleUser(Context ctx) {
    String id = ctx.getRequestPathParameter("id");
    String result = String.format("Hello user %s!", id);
    ctx.setResponseStatus(HttpStatus.OK_200);
    ctx.setResponseBody(result);
  }

  private static void handleError(Context ctx) {
    throw new RuntimeException("an error!");
  }
}
