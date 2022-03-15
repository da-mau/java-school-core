package com.mau.http.handler.impl;

import com.mau.http.handler.RequestHandler;
import com.mau.model.EntityRepository;
import com.mau.model.RandomEntity;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class DeleteRequestHandler extends RequestHandler {
    public void handle(HttpExchange httpExchange) throws IOException {
        // parse request
        Map<String, Object> parameters = new HashMap<String, Object>();
        URI requestedUri = httpExchange.getRequestURI();
        String[] segments = requestedUri.getPath().split("/");
        try {
            String pathId = segments[segments.length - 1];
            EntityRepository repo = EntityRepository.INSTANCE;
            Map<Integer, RandomEntity> records = repo.getRecords();
            int id = Integer.parseInt(pathId);
            if (records.containsKey(id)) {
                records.remove(id);
                responseCode = RequestHandler.NO_CONTENT_HTTP_CODE;
            } else {
                responseCode = RequestHandler.NOT_FOUND_HTTP_CODE;
            }
        } catch (NumberFormatException ex) {
            responseCode = RequestHandler.BAD_REQUEST_HTTP_CODE;
            response = null;
        }
        // send response
        returnResponse(httpExchange, responseCode, null);
    }
}
