package com.mau.http.handler.impl;

import com.mau.http.handler.RequestHandler;
import com.mau.repository.EntityStorage;
import com.mau.model.RandomEntity;
import com.mau.util.JsonSerializer;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class PostRequestHandler extends RequestHandler {
    public void handle(HttpExchange httpExchange) throws IOException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        // parse request
        Map<String, Object> parameters = new HashMap<String, Object>();
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        try {
            RandomEntity requestObject = JsonSerializer.deserialize(RandomEntity.class, query);
            EntityStorage repo = EntityStorage.INSTANCE;
            Map<Integer, RandomEntity> records = repo.getRecords();
            if (!records.containsKey(requestObject.getId())) {
                records.put(requestObject.getId(), requestObject);
                String path = httpExchange.getHttpContext().getPath();
                httpExchange.getResponseHeaders().add("Location", path +"/" + requestObject.getId());
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
