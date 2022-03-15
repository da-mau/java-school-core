package com.mau.http.handler.impl;

import com.mau.http.handler.RequestHandler;
import com.mau.model.EntityRepository;
import com.mau.model.RandomEntity;
import com.sun.net.httpserver.HttpExchange;


import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class GetRequestHandler extends RequestHandler {
    public void handle(HttpExchange httpExchange) throws IOException {
        // parse request
        Map<String, Object> parameters = new HashMap<String, Object>();
        URI requestedUri = httpExchange.getRequestURI();
        String[] segments = requestedUri.getPath().split("/");
        String pathId = segments[segments.length - 1];
        EntityRepository repo = EntityRepository.INSTANCE;
        Map<Integer, RandomEntity> records = repo.getRecords();
        try {
            int id = Integer.parseInt(pathId);
            if (records.containsKey(id)) {
                RandomEntity aux = records.get(id);
                response = "Id = " + aux.getId() + "\n" + "Name = " + aux.getName();
                responseCode = RequestHandler.SUCCESS_HTTP_CODE;
            } else {
                responseCode = RequestHandler.NOT_FOUND_HTTP_CODE;
            }
            // send response

        } catch (NumberFormatException e) {
            responseCode = RequestHandler.BAD_REQUEST_HTTP_CODE;
            response = null;
        }
        returnResponse(httpExchange, responseCode, response);
    }
}
