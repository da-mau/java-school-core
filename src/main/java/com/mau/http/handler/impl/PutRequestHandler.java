package com.mau.http.handler.impl;

import com.mau.http.handler.RequestHandler;
import com.mau.model.EntityRepository;
import com.mau.model.RandomEntity;
import com.mau.util.JsonSerializer;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class PutRequestHandler extends RequestHandler {
    public void handle(HttpExchange httpExchange) throws IOException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        // parse request
        Map<String, Object> parameters = new HashMap<String, Object>();
        URI requestedUri = httpExchange.getRequestURI();
        String[] segments = requestedUri.getPath().split("/");
        String pathId = segments[segments.length-1];
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        try {
            RandomEntity requestObject = JsonSerializer.deserialize(RandomEntity.class, query);
            EntityRepository repo  = EntityRepository.INSTANCE;
            Map<Integer, RandomEntity> records = repo.getRecords();
            int id =  Integer.parseInt(pathId);
            if(records.containsKey(id)){
                responseCode = RequestHandler.SUCCESS_HTTP_CODE;
            }else{
                responseCode = RequestHandler.CREATED_HTTP_CODE;
            }
            records.put(id, requestObject);
            String path = httpExchange.getHttpContext().getPath();
            httpExchange.getResponseHeaders().add("Location", path +"/" + id);
        }catch(NumberFormatException e){
            responseCode = RequestHandler.BAD_REQUEST_HTTP_CODE;
            response = null;
        }
        returnResponse(httpExchange, responseCode, response);
    }
}
