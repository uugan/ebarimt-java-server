package com.github.uugan.ebarimt;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
/**
 * @author Uugan
 * @since 1.0
 */
public class HttpPostHandler implements HttpHandler {
    public static final Logger logger = LoggerFactory.getLogger(HttpPostHandler.class);
    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {

        httpServerExchange.getRequestReceiver().receiveFullBytes((exchange, data) -> {
            try {
                String ret = EbarimtSvc.getInstance().onPOST(httpServerExchange, data);
                httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                httpServerExchange.getResponseSender().send(ByteBuffer.wrap(ret.getBytes("UTF-8")));

            } catch (Exception ex) {
                logger.error("", ex);
                httpServerExchange.setStatusCode(500);
                httpServerExchange.getResponseSender().send("Internal error:" + ex.getMessage());
            }
        });
    }
}
