package com.mau.http.handler;

import com.mau.context.ApplicationContext;
import com.mau.context.annotation.Component;
import com.mau.model.RandomEntity;
import com.mau.util.JsonSerializer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;

@Component("/mauserver")
public class MainHandler implements HttpHandler {

    /**
     * Handles http requests
     */
    @Override
    public void handle(HttpExchange httpExchange) throws UnsupportedEncodingException {
        String httpMethod = httpExchange.getRequestMethod();
        URI requestedUri = httpExchange.getRequestURI();
        //Get rid of Mauserver
        String[] segments = requestedUri.getPath().split("/");
        String pathId = segments[segments.length - 1];
        Integer id = null;
        boolean containsId = isNumeric(pathId);
        if (containsId) {
            id = Integer.parseInt(pathId);
            pathId = "{id}";
        }else{
            pathId = "";
        }
        String controllerPath = "/" + segments[2];
        String methodPath =  controllerPath + "/" + httpMethod + "/" + pathId;
        RequestHandler handler;
        boolean validRequest = true;
        //Get Controller
        Class<?> controllerClass = ApplicationContext.getController(controllerPath);
        Object controllerInstance = ApplicationContext.getBean(controllerClass);
        //Get method
        Method method = ApplicationContext.getMethod(methodPath);
        //Parse object
        try (InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8")) {
            BufferedReader br = new BufferedReader(isr);
            String query = "";
            query = br.readLine();
            RandomEntity requestObject = JsonSerializer.deserialize(RandomEntity.class, query);
            //Invoke method on object with parameters
            int paramCount = method.getParameterCount();
            Object returnObject;
            if (paramCount == 1) {
                if(containsId){
                    returnObject = method.invoke(controllerInstance, id);
                }else{
                    returnObject = method.invoke(controllerInstance, requestObject);
                }
            } else {
                returnObject = method.invoke(controllerInstance, id, requestObject);
            }
            //serialize response
            String response = null;
            int responseCode = 0;
            if( returnObject instanceof  Boolean){
                if (((Boolean) returnObject).booleanValue()){
                    responseCode = RequestHandler.CREATED_HTTP_CODE;
                    httpExchange.getResponseHeaders().add("Location", controllerPath +"/" + requestObject.getId());
                }else{
                    responseCode = RequestHandler.CONFLICT_HTTP_CODE;
                }
            }else{
                response = JsonSerializer.serialize(returnObject);
            }
            //send response
            RequestHandler.returnResponse(httpExchange, responseCode, response);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
