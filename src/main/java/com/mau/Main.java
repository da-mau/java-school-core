package com.mau;

import com.mau.http.MauServer;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Main {

    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws IOException {
        Executor executor = Executors.newFixedThreadPool(NUMBER_OF_CORES);
        MauServer server = new MauServer(executor);
        server.startServer();
    }
}
