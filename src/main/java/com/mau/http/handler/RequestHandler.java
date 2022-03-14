package com.mau.http.handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public abstract class RequestHandler {
    /**
     * Returns bad request message.
     */
    protected void returnBadRequest(HttpExchange httpExchange, String result) throws IOException {
        httpExchange.sendResponseHeaders(400, 0);

        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(result.getBytes());
        outputStream.close();

    }

    protected abstract void  handle(HttpExchange httpExchange) throws IOException ;
}
