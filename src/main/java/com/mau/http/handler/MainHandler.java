package com.mau.http.handler;

import com.mau.context.annotation.Component;
import com.mau.http.handler.impl.DeleteRequestHandler;
import com.mau.http.handler.impl.GetRequestHandler;
import com.mau.http.handler.impl.PostRequestHandler;
import com.mau.http.handler.impl.PutRequestHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
@Component
public class MainHandler implements HttpHandler {
    /**
     * Handles http requests
     */
    @Override
    public void handle(HttpExchange httpExchange) {
        String uri = httpExchange.getRequestURI().toString();
        String httpMethod = httpExchange.getRequestMethod();
        RequestHandler handler;
        boolean validRequest = true;
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
                    validRequest = false;
                    handler = new PostRequestHandler();
                    break;
            }
            if (validRequest) {
                handler.handle(httpExchange);
            } else {
                handler.returnResponse(httpExchange, RequestHandler.BAD_REQUEST_HTTP_CODE, RequestHandler.BAD_REQUEST);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }


}
