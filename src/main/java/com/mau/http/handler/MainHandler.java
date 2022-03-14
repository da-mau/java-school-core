package com.mau.http.handler;

import com.mau.http.handler.impl.DeleteRequestHandler;
import com.mau.http.handler.impl.GetRequestHandler;
import com.mau.http.handler.impl.PostRequestHandler;
import com.mau.http.handler.impl.PutRequestHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class MainHandler implements HttpHandler {
    /**
     * Handles http requests
     */
    @Override
    public void handle(HttpExchange httpExchange) {
        String httpMethod = httpExchange.getRequestMethod();
        RequestHandler handler;
        boolean badRequest = false;
        try {
            switch (httpMethod) {
                case "GET":
                    handler = new GetRequestHandler();
                    break;
                case "POST":
                    handler = new PostRequestHandler();
                    break;
                case "PUT":
                    handler = new PutRequestHandler();
                    break;
                case "DELETE":
                    handler = new DeleteRequestHandler();
                    break;
                default:
                    handler = new PostRequestHandler();
                    break;
            }
            if (badRequest) {
                handler.returnBadRequest(httpExchange, "Bad Request");
            } else {
                handler.handle(httpExchange);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
