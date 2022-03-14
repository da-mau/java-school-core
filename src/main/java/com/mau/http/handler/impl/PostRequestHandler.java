package com.mau.http.handler.impl;

import com.mau.http.MauServer;
import com.mau.http.handler.RequestHandler;
import com.mau.model.RandomEntity;
import com.mau.model.RandomEntityRepository;
import com.mau.util.InputParser;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpsServer;

import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class PostRequestHandler extends RequestHandler {
    public void handle(HttpExchange httpExchange) throws IOException {
        // parse request
        Map<String, Object> parameters = new HashMap<String, Object>();
        URI requestedUri = httpExchange.getRequestURI();
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        String response = "";
        try {
            InputParser.parseQuery(query, parameters);
            Map<Integer, RandomEntity> repo = RandomEntityRepository.getRandomEntityRepo();
            int id =  Integer.parseInt((String)parameters.get("id"));
            String name = (String) parameters.get("name");
            if (!repo.containsKey(id)) {
                RandomEntity aux = new RandomEntity(id, name);
                repo.put(id, aux);
                String path = httpExchange.getHttpContext().getPath();
                httpExchange.getResponseHeaders().add("Location", path +"/" + id);
                httpExchange.sendResponseHeaders(201, response.length());
            }else{
                httpExchange.sendResponseHeaders(409, response.length());
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
