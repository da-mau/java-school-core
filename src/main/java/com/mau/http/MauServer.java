package com.mau.http;

import com.mau.context.ApplicationContext;
import com.mau.http.handler.MainHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This class will act as the Http Server.
 * It receives an executor as a parameter for the constructor.
 * It has only one endpoint configured, for it, it needs a FibonacciHandler to deal with the request.
 */
public final class MauServer {

    private MauServer() {
    }

    public static void run(Class<?> clazz, String[] args) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        try (InputStream input = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("application.properties")){
            Properties properties = new Properties();
            properties.load(input);
            ApplicationContext.init(clazz);
            Integer port = Integer.parseInt(properties.getProperty("app.port"));
            String context = properties.getProperty("app.context");
            //TODO Load Config
            Executor executor = Executors.newFixedThreadPool(1);
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            //TODO Load controllers to declare contexts
            server.createContext(context, ApplicationContext.getBean(MainHandler.class));
            server.setExecutor(executor);
            server.start();
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
