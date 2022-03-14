package com.mau.http.handler.impl;

import com.mau.http.handler.RequestHandler;
import com.mau.model.RandomEntity;
import com.mau.model.RandomEntityRepository;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class DeleteRequestHandler extends RequestHandler {
    public void handle(HttpExchange httpExchange) throws IOException {
        // parse request
        Map<String, Object> parameters = new HashMap<String, Object>();
        URI requestedUri = httpExchange.getRequestURI();
        String[] segments = requestedUri.getPath().split("/");
        String pathId = segments[segments.length-1];
        int responseLength;
        int responseCode;
        try {
            Map<Integer, RandomEntity> repo = RandomEntityRepository.getRandomEntityRepo();
            int id =  Integer.parseInt(pathId);
            if (repo.containsKey(id)) {
                repo.remove(id);
                responseCode = 204;
                responseLength = -1;
            } else {
                responseCode = 404;
                responseLength = 0;
            }
            // send response
            httpExchange.sendResponseHeaders(responseCode, responseLength);
            OutputStream os = httpExchange.getResponseBody();
            os.close();
        } catch (UnsupportedEncodingException e) {
            //Ideally we should log the exception here before returning the bad request response
            returnBadRequest(httpExchange, "Bad Request");
        }

    }
}
