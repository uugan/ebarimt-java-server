package com.github.uugan.ebarimt;

public interface Gateway {
    void init() throws Exception;
    void start() throws Exception;
    void stop() throws Exception;
}
