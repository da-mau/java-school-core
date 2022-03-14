package com.mau.http.handler.impl;

import com.mau.http.handler.RequestHandler;
import com.mau.model.RandomEntity;
import com.mau.model.RandomEntityRepository;
import com.mau.util.InputParser;
import com.sun.net.httpserver.HttpExchange;


import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GetRequestHandler extends RequestHandler {
    public void handle(HttpExchange httpExchange) throws IOException {
        // parse request
        Map<String, Object> parameters = new HashMap<String, Object>();
        URI requestedUri = httpExchange.getRequestURI();
        String[] segments = requestedUri.getPath().split("/");
        String pathId = segments[segments.length-1];
        String response = "";
        try {
            Map<Integer, RandomEntity> repo = RandomEntityRepository.getRandomEntityRepo();
            int id =  Integer.parseInt(pathId);
            if (repo.containsKey(id)) {
                RandomEntity aux = repo.get(id);
                response = "Id = " + aux.getId() + "\n" + "Name = " + aux.getName();
                httpExchange.sendResponseHeaders(200, response.length());
            } else {
                httpExchange.sendResponseHeaders(404, response.length());
            }
            // send response
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        } catch (UnsupportedEncodingException e) {
            //Ideally we should log the exception here before returning the bad request response
            returnBadRequest(httpExchange, "Bad Request");
        }

    }
}
