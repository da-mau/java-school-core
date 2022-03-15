package com.mau.http;

import com.mau.http.handler.MainHandler;
import com.mau.model.RandomEntity;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * This class will act as the Http Server.
 * It receives an executor as a parameter for the constructor.
 * It has only one endpoint configured, for it, it needs a FibonacciHandler to deal with the request.
 */
public class MauServer {


    private final Executor executor;
    private static final String RANDOM_PATH = "/entities";

    public MauServer(Executor executor) {
        this.executor = executor;
    }

    public void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext(RANDOM_PATH, new MainHandler());
        server.setExecutor(getExecutor());
        server.start();
    }

    public Executor getExecutor() {
        return executor;
    }
}
