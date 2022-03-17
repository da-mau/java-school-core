package com.mau;

import com.mau.http.MauServer;

import java.io.IOException;

public class Main {

    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws IOException {
        MauServer.run(Main.class, args);
    }


}
