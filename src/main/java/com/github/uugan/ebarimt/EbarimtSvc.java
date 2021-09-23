package com.github.uugan.ebarimt;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.Methods;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
/**
 * @author Uugan
 * @since 1.0
 */
public class EbarimtSvc implements Gateway {
    public static final Logger logger = LoggerFactory.getLogger(EbarimtSvc.class);

    private Undertow _server;
    private int _port = 8080;
    private static EbarimtSvc _instance = null;
    BridgePosAPI _posApi;
    private ReentrantLock mutex = new ReentrantLock();

    //TODO: create scheduled job for senddata?
    public static EbarimtSvc getInstance() {
        if (_instance == null) {
            _instance = new EbarimtSvc();
        }
        return _instance;
    }

    public static void main(String[] args) {

        try {
            _instance = EbarimtSvc.getInstance();
            _instance.init();
            _instance.start();
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }

    @Override
    public void init() throws Exception {
        try {
            logger.debug(System.getProperty("java.library.path"));
            if (System.getProperty("port") != null) {
                _port = Integer.parseInt(System.getProperty("port"));
            }
            _server = Undertow.builder().addHttpListener(_port, "0.0.0.0")
                    .setHandler(Handlers.routing()
                            .add(Methods.GET, "/*", new BlockingHandler(new HttpGetHandler()))
                            .add(Methods.POST, "/*", new BlockingHandler(new HttpPostHandler()))
                    )
                    .build();

            _posApi = new BridgePosAPI();
            String res_info = _posApi.getInformation();
            logger.debug("[getInformation={}]", res_info);
            String result = _posApi.sendData();
            logger.debug("[result={}]", result);
            String res_check = _posApi.checkApi();
            logger.debug("[checkAPI={}]", res_check);
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }

    @Override
    public void start() throws Exception {
        _server.start();
    }

    @Override
    public void stop() throws Exception {
        if (_server != null) {
            _server.stop();
        }
    }

    public String onPOST(HttpServerExchange httpServerExchange, byte[] data) {
        String path = httpServerExchange.getRequestPath();
        String ipaddress = httpServerExchange.getSourceAddress().getHostString();
        //  int port = httpServerExchange.getHostPort();
        String ret = "";
        try {
            String req = new String(data, "UTF-8").replaceAll("[\\t\\n\\r]+", " ");
            JSONObject req_json = new JSONObject(req);
            if ("/put".equals(path)) {
                ret = sendPutReq(req_json);
            } else if ("/return".equals(path)) {
                ret = sendReturnReq(req_json);
            }
            logger.debug("onPOST [ipaddress={}, req={}, resp={}]", ipaddress, req, ret);

        } catch (Exception ex) {
            ret = "Internal error: " + ex.getMessage();
            logger.error("onPOST [ipaddress={}, resp={}]", ipaddress, ret);
        }
        return ret;
    }

    public String onGET(HttpServerExchange httpServerExchange) {
        String ret = "";
        String path = httpServerExchange.getRequestPath();
        Map<String, Deque<String>> params = httpServerExchange.getQueryParameters();
        String ipaddress = httpServerExchange.getSourceAddress().getHostString();
        try {

            if (path.startsWith("/senddata")) {
                ret = sendData();
            } else if (path.equals("/info")) {
                ret = getInfo();
            } else if (path.equals("/check")) {
                ret = getCheckApi();
            }
            logger.debug("onGET [ipaddress={}, req={}, resp={}]", ipaddress, path + ":" + params, ret);
        } catch (Exception ex) {
            logger.error("", ex);
            ret = "Internal error:" + ex.getMessage();
            logger.error("onGET [ipaddress={},req={}, resp={}]", ipaddress, path + ":" + params, ret);
        }
        return ret;
    }


    private String sendData() {
        String ret = "";
        try {
            mutex.lock();
            ret = _posApi.sendData();
        } catch (Exception ex) {
            logger.error("", ex);
            ret = ex.getMessage();
        } finally {
            if (mutex.isLocked()) {
                mutex.unlock();
            }
        }
        return ret;
    }

    private String getInfo() {
        String ret = "";
        try {
            mutex.lock();
            ret = _posApi.getInformation();
        } catch (Exception ex) {
            logger.error("", ex);
            ret = ex.getMessage();
        } finally {
            if (mutex.isLocked()) {
                mutex.unlock();
            }
        }
        return ret;
    }

    private String getCheckApi() {
        String ret = "";
        try {
            mutex.lock();
            ret = _posApi.checkApi();
        } catch (Exception ex) {
            logger.error("", ex);
            ret = ex.getMessage();
        } finally {
            if (mutex.isLocked()) {
                mutex.unlock();
            }
        }
        return ret;
    }

    private String sendPutReq(JSONObject req_json) {
        String ret = "";
        try {
            mutex.lock();
            ret = _posApi.put(req_json.toString());
        } catch (Exception ex) {
            logger.error("", ex);
            ret = ex.getMessage();
        } finally {
            if (mutex.isLocked()) {
                mutex.unlock();
            }
        }
        return ret;
    }

    private String sendReturnReq(JSONObject req_json) {
        String ret = "";
        try {
            mutex.lock();
            ret = _posApi.returnBill(req_json.toString());
        } catch (Exception ex) {
            logger.error("", ex);
            ret = ex.getMessage();
        } finally {
            if (mutex.isLocked()) {
                mutex.unlock();
            }
        }
        return ret;
    }


}
