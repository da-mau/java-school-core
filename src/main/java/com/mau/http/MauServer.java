package com.mau.http;

import com.mau.context.ApplicationContext;
import com.mau.http.handler.MainHandler;
import com.mau.model.RandomEntity;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This class will act as the Http Server.
 * It receives an executor as a parameter for the constructor.
 * It has only one endpoint configured, for it, it needs a FibonacciHandler to deal with the request.
 */
public final class MauServer {

    private static final String RANDOM_PATH = "/entities";

    private MauServer() {
    }

    public static void run(Class<?> clazz, String[] args) throws IOException {
        //TODO Load Config
        Executor executor = Executors.newFixedThreadPool(1);
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext(RANDOM_PATH, ApplicationContext.getBean(MainHandler.class));
        server.setExecutor(executor);
        server.start();
    }

    public void startServer() throws IOException {

    }

}
