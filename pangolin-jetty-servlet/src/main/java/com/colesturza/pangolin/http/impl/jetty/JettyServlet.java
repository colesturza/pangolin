/* (C)2024 */
package com.colesturza.pangolin.http.impl.jetty;

import com.colesturza.pangolin.http.Context;
import com.colesturza.pangolin.http.Router;
import com.google.common.net.MediaType;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(JettyServlet.class);

  private final Router router;

  public JettyServlet(Router router) {
    this.router = router;
  }

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) {
    Context ctx = new JettyServletContext(request, response);
    try {
      router.serve(ctx);
    } catch (Exception e) {
      logger.atError().setMessage("failed to process request").setCause(e).log();
      int status = HttpStatus.INTERNAL_SERVER_ERROR_500;
      ctx.setResponseStatus(status);
      ctx.setResponseContentType(MediaType.PLAIN_TEXT_UTF_8.toString());
      ctx.setResponseBody(HttpStatus.getMessage(status));
    }
    ctx.sendResponse();
  }
}
