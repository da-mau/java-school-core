package com.mau.http.handler.impl;

import com.mau.http.handler.RequestHandler;
import com.mau.model.EntityRepository;
import com.mau.model.RandomEntity;
import com.mau.util.InputParser;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PostRequestHandler extends RequestHandler {
    public void handle(HttpExchange httpExchange) throws IOException {
        // parse request
        Map<String, Object> parameters = new HashMap<String, Object>();
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        try {
            InputParser.parseQuery(query, parameters);
            EntityRepository repo = EntityRepository.INSTANCE;
            Map<Integer, RandomEntity> records = repo.getRecords();
            int id =  Integer.parseInt((String)parameters.get("id"));
            String name = (String) parameters.get("name");
            if (!records.containsKey(id)) {
                RandomEntity aux = new RandomEntity(id, name);
                records.put(id, aux);
                String path = httpExchange.getHttpContext().getPath();
                httpExchange.getResponseHeaders().add("Location", path +"/" + id);
                responseCode = RequestHandler.CREATED_HTTP_CODE;
            }else{
                responseCode = RequestHandler.CONFLICT_HTTP_CODE;
            }
            // send response
            returnResponse(httpExchange, responseCode, response);
        } catch (UnsupportedEncodingException e) {
            //Ideally we should log the exception here before returning the bad request response
            returnResponse(httpExchange, RequestHandler.BAD_REQUEST_HTTP_CODE,RequestHandler.BAD_REQUEST);
        }

    }
}
