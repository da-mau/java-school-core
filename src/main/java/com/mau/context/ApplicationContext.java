package com.mau.context;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    private static ApplicationContext instance;
    private ApplicationContext(){};
    private static Map<String, Class<?>> controllers = new HashMap<>();
    private static Map<Class <?>, Object> beans = new HashMap<>();
    private static Map<String, Method> actions = new HashMap<>();
    static { init(); }

    public static ApplicationContext getInstance(){
        if(instance == null ){
           instance = new ApplicationContext();
        }
        return instance;
    }

    public static void init(){
        /**
         * Procesar las anotaciones que tenga la aplicacion
         *
         * Leer controladores, repositorios, inicializar
         */
        System.out.println("Calling init method");
        loadAllBeans();
        loadControllers();
        loadRepositories();
    }

    private static void loadRepositories() {

    }

    private static void loadControllers() {

    }

    private static void loadAllBeans() {

    }

    public static <T> T getBean(Class <?> clazz){
        /**
         * Crear las instancias
         */

        return (T) beans.get(clazz);
    }


}
