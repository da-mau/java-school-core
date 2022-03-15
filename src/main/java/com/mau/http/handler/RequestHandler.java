package com.mau.http.handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public abstract class RequestHandler {
    public static final int BAD_REQUEST_HTTP_CODE = 400;
    public static final int SUCCESS_HTTP_CODE = 200;
    public static final int CREATED_HTTP_CODE = 201;
    public static final int NOT_FOUND_HTTP_CODE = 404;
    public static final int CONFLICT_HTTP_CODE = 409;
    public static final int NO_CONTENT_HTTP_CODE = 204;
    public static final String BAD_REQUEST = "Bad Request";
    protected int responseCode;
    protected String response;

    /**
     * Returns bad request message.
     */
    protected void returnResponse(HttpExchange httpExchange, int responseCode, String result) throws IOException {
        int responseLength = result != null ? result.length() : -1;
        httpExchange.sendResponseHeaders(responseCode, responseLength);
        OutputStream outputStream = httpExchange.getResponseBody();
        if(NO_CONTENT_HTTP_CODE != responseCode){
            outputStream.write(result.getBytes());
        }
        outputStream.close();

    }

    protected abstract void  handle(HttpExchange httpExchange) throws IOException ;

}
