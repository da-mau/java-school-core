package com.mau.context;

import com.mau.context.annotation.*;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ApplicationContext {

    private static ApplicationContext instance = new ApplicationContext();
    private static Map<String, Class<?>> controllers = new HashMap<>();
    private static Map<Class<?>, Object> beans = new HashMap<>();
    private static Map<String, Method> actions = new HashMap<>();
    private static Predicate<Class> componentFilter = clazz -> clazz.isAnnotationPresent(Component.class);
    private static Predicate<Class> repoFilter = clazz -> clazz.isAnnotationPresent(Repository.class);
    private static Predicate<Class> controllerFilter = clazz -> clazz.isAnnotationPresent(RestController.class);
    private static Predicate<Class> serviceFilter = clazz -> clazz.isAnnotationPresent(Service.class);
    private static String packageName = new String();


    public static void init(Class<?> clazz) throws IOException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        /**
         * Procesar las anotaciones que tenga la aplicacion
         *
         * Leer controladores, repositorios, inicializar
         */
        System.out.println("Calling init method");
        packageName = clazz.getPackage().getName();
        loadAllBeans();
        loadControllers();
        loadRepositories();
        System.out.println("Finishing init method");
    }

    private static void loadRepositories() {
        for (Map.Entry<String, Class<?>> entry : controllers.entrySet()) {
            String wholePath;
            String controllerPath = entry.getKey();
            Class controllerClass = entry.getValue();
            Method[] methods = controllerClass.getDeclaredMethods();
            for (Method method : methods) {
                Annotation[] annotations = method.getAnnotations();
                for (Annotation annotation : annotations) {
                    Class annotationClass = annotation.annotationType();
                    String simpleName = annotationClass.getSimpleName();
                    String annotationPath = getAnnotationPath(annotation);
                    wholePath = controllerPath + "/" + simpleName  + annotationPath;
                    actions.put(wholePath, method);
                }
            }
        }
    }

    private static void loadControllers() {
        List<Class<?>> tempControllers = beans.keySet().stream().filter(controllerFilter).collect(Collectors.toList());
        for (Class<?> clazz : tempControllers) {
            RestController temp = clazz.getAnnotation(RestController.class);
            controllers.put(temp.value(), clazz);
        }
    }

    private static void loadAllBeans() throws IOException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Class> classes = getClasses(packageName);
        //Instantiate beans that are marked with annotations
        //component, repository, restController,Service
        classes = classes.stream().
                filter(componentFilter.or(repoFilter).or(controllerFilter).or(serviceFilter))
                .collect(Collectors.toList());
        for (Class clazz : classes) {
            //Get autowired stuff
            Set<Class<?>> stack = new HashSet<Class<?>>();
            stack.add(clazz);
            Object instance = getInstanceBean(clazz, stack);
            beans.put(clazz, instance);
        }
    }


    private static Object getInstanceBean(Class<?> clazz, Set<Class<?>> stack) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }
        Class<?> instance = Class.forName(clazz.getName());
        Constructor<?> defaultConstructor = getDefaultConstructor(instance.getConstructors());
        Object objectInstance = defaultConstructor.newInstance(null);
        for (Field field : clazz.getDeclaredFields()) {
            Object fieldInstance;
            //No circular dependencies
            if (stack.contains(field.getType())) {
                throw new IllegalArgumentException("Circular Dependency detected");
            }
            if (field.isAnnotationPresent(Autowired.class)) {
                stack.add(field.getType());
                fieldInstance = getInstanceBean(field.getType(), stack);
            } else {
                fieldInstance = Class.forName(field.getName());
            }
            beans.put(field.getType(), fieldInstance);
            field.setAccessible(true);
            field.set(objectInstance, fieldInstance);

        }
        //return instance with all of its fields
        return objectInstance;
    }

    public static <T> T getBean(Class<?> clazz) {
        return (T) beans.get(clazz);
    }

    public static <T> T getController(String path) {
        return (T) controllers.get(path);
    }

    public static <T> T getMethod(String path) {
        return (T) actions.get(path);
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private static List<Class> getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    private static Constructor<?> getDefaultConstructor(Constructor<?>[] constructors) {
        Constructor<?> defaultConstructor = null;
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                defaultConstructor = constructor;
            }
        }
        return defaultConstructor;
    }

    private static String getAnnotationPath(Annotation annotation ){
        String result = "";
        if(annotation instanceof GET){
            GET holder = (GET)annotation;
            result = holder.path();
        }
        if(annotation instanceof POST){
            POST holder = (POST)annotation;
            result = holder.path();
        }
        if(annotation instanceof PUT){
            PUT holder = (PUT)annotation;
            result = holder.path();
        }
        if(annotation instanceof DELETE){
            DELETE holder = (DELETE)annotation;
            result = holder.path();
        }
        return result;
    }
    public static Object getNewObjectInstance(Class<?> clazz) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> instance = Class.forName(clazz.getName());
        Constructor<?> defaultConstructor = getDefaultConstructor(instance.getConstructors());
        Object objectInstance = defaultConstructor.newInstance(null);
        return objectInstance;
    }
}
