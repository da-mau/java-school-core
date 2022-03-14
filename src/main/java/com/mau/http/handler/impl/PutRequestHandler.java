package com.mau.http.handler.impl;

import com.mau.http.handler.RequestHandler;
import com.mau.model.RandomEntity;
import com.mau.model.RandomEntityRepository;
import com.mau.util.InputParser;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class PutRequestHandler extends RequestHandler {
    public void handle(HttpExchange httpExchange) throws IOException {
        // parse request
        Map<String, Object> parameters = new HashMap<String, Object>();
        URI requestedUri = httpExchange.getRequestURI();
        String[] segments = requestedUri.getPath().split("/");
        String pathId = segments[segments.length-1];
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        String response = "";
        int responseCode = 0;
        try {
            InputParser.parseQuery(query, parameters);
            Map<Integer, RandomEntity> repo = RandomEntityRepository.getRandomEntityRepo();
            int id =  Integer.parseInt(pathId);
            String name = (String) parameters.get("name");
            if(repo.containsKey(id)){
                responseCode = 200;
            }else{
                responseCode = 201;
            }
            RandomEntity aux = repo.get(id);
            aux = new RandomEntity(id, name);
            repo.put(id, aux);
            String path = httpExchange.getHttpContext().getPath();
            httpExchange.getResponseHeaders().add("Location", path +"/" + id);
            httpExchange.sendResponseHeaders(responseCode, response.length());
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
