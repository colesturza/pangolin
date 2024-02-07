/* (C)2024 */
package com.colesturza.pangolin.http.impl.jetty;

import com.colesturza.pangolin.http.Context;
import com.colesturza.pangolin.http.HttpMethod;
import com.colesturza.pangolin.http.QueryParamParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyServletContext implements Context {

  private static final Logger logger = LoggerFactory.getLogger(JettyServletContext.class);

  private final HttpServletRequest request;
  private final HttpServletResponse response;

  private final Map<String, String> queryParameters;
  private final Map<String, String> pathParameters;

  private InputStream responseBodyStream;

  public JettyServletContext(HttpServletRequest request, HttpServletResponse response) {
    this.request = request;
    this.response = response;
    this.queryParameters =
        Collections.unmodifiableMap(QueryParamParser.parseQueryParams(request.getQueryString()));
    this.pathParameters = new HashMap<>();
  }

  @Override
  public String getPath() {
    return request.getRequestURI();
  }

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.valueOf(request.getMethod());
  }

  @Override
  public Map<String, String> getRequestHeaders() {
    Enumeration<String> headerNames = request.getHeaderNames();
    Map<String, String> headers = new HashMap<>();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      String headerValue = request.getHeader(headerName);
      headers.put(headerName, headerValue);
    }
    return Collections.unmodifiableMap(headers);
  }

  @Override
  public Map<String, String> getRequestQueryParameters() {
    return queryParameters;
  }

  @Override
  public Map<String, String> getRequestPathParameters() {
    return pathParameters;
  }

  // Delegates for HttpServletResponse
  @Override
  public void setResponseStatus(int status) {
    response.setStatus(status);
  }

  @Override
  public int getResponseStatus() {
    return response.getStatus();
  }

  @Override
  public void setResponseHeader(String name, String value) {
    response.setHeader(name, value);
  }

  @Override
  public void setResponseContentType(String type) {
    response.setContentType(type);
  }

  @Override
  public void setResponseBody(InputStream stream) {
    if (responseBodyStream != null) {
      try {
        responseBodyStream.close();
      } catch (IOException e) {
        logger
            .atError()
            .setMessage("failed to close former response body stream")
            .setCause(e)
            .log();
      }
    }
    this.responseBodyStream = stream;
  }

  @Override
  public int getRequestContentLength() {
    return request.getContentLength();
  }

  @Override
  public String getRequestContentType() {
    return request.getContentType();
  }

  @Override
  public String getRequestBody() throws IOException {
    Charset charset;
    try {
      charset = Charset.forName(request.getCharacterEncoding());
    } catch (IllegalArgumentException e) {
      charset = StandardCharsets.UTF_8;
    }
    return new String(getRequestBodyAsBytes(), charset);
  }

  @Override
  public byte[] getRequestBodyAsBytes() throws IOException {
    return request.getInputStream().readAllBytes();
  }

  @Override
  public <T> T getRequestBodyAsClass(Class<T> clazz) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getRequestBodyAsClass'");
  }

  @Override
  public String getRequestHost() {
    return request.getRemoteHost();
  }

  @Override
  public String getRequestIp() {
    return request.getRemoteAddr();
  }

  @Override
  public int getRequestPort() {
    return request.getRemotePort();
  }

  @Override
  public String getRequestProtocol() {
    return request.getProtocol();
  }

  @Override
  public String getRequestHeader(String key) {
    return request.getHeader(key);
  }

  @Override
  public String getRequestQueryParameter(String key) {
    return queryParameters.get(key);
  }

  @Override
  public <T> T getRequestQueryParameterAsClass(String key, Class<T> clazz) {
    return clazz.cast(queryParameters.get(key));
  }

  @Override
  public String getRequestQueryString() {
    return request.getQueryString();
  }

  @Override
  public String getRequestPathParameter(String key) {
    return pathParameters.get(key);
  }

  @Override
  public <T> T getRequestPathParameter(String key, Class<T> clazz) {
    return clazz.cast(pathParameters.get(key));
  }

  @Override
  public void setResponseBodyJson(Object obj) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'setResponseBodyJson'");
  }

  @Override
  public void sendResponse() {
    if (responseBodyStream != null) {
      try {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = responseBodyStream.read(buffer)) != -1) {
          response.getOutputStream().write(buffer, 0, bytesRead);
        }
      } catch (IOException e) {
        logger
            .atError()
            .setMessage("failed to write to response body output stream")
            .setCause(e)
            .log();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return;
      } finally {
        try {
          responseBodyStream.close();
          responseBodyStream = null;
        } catch (IOException e) {
          logger
              .atError()
              .setMessage("failed to close response body input stream")
              .setCause(e)
              .log();
        }
      }
    }
  }
}
