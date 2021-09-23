package com.github.uugan.ebarimt;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.Map;
/**
 * @author Uugan
 * @since 1.0
 */
public class HttpGetHandler implements HttpHandler {
    public static final Logger logger = LoggerFactory.getLogger(HttpGetHandler.class);

    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {
        try {
            String ret = EbarimtSvc.getInstance().onGET(httpServerExchange);
            httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            httpServerExchange.getResponseSender().send(ByteBuffer.wrap(ret.getBytes("UTF-8")));

        } catch (Exception ex) {
            logger.error("", ex);
            httpServerExchange.setStatusCode(500);
            httpServerExchange.getResponseSender().send(ex.getMessage());
        }
    }
}
