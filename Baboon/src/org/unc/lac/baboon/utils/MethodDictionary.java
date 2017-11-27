package org.unc.lac.baboon.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import org.javatuples.Pair;

/**
 * Given an object instance and a method's name, resolves and returns the
 * {@link Method} object corresponding to the method name given. This class only
 * resolves methods that does not take parameters in its signature.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public class MethodDictionary {
    /**
     * Map of {@link Method} objects already solved
     */
    private static HashMap<Pair<Object, String>, Method> methodDict = new HashMap<>();

    /**
     * Given an object instance and a method's name returns the Method object
     * corresponding to the method name given. The given method must not take
     * any arguments.
     * 
     * @param object
     *            an object of the class declaring the method to resolve.
     * @param methodName
     *            the name of the method to resolve.
     * @return a {@link Method} object resolved using object's class and
     *         methodName.
     */
    public static Method getMethod(Object object, String methodName, Class<?>... parameterClasses)
            throws NoSuchMethodException, SecurityException {
        Class<?> c = object.getClass();
        Pair<Object, String> key = new Pair<>(object, methodName);
        if (methodDict.containsKey(key)) {
            return methodDict.get(key);
        } else {

            Method m = resolveMethod(c, methodName, parameterClasses);
            methodDict.put(key, m);
            return m;
        }
    }
    
    public static Method getStaticMethod(Class<?> methodsClass, String methodName, Class<?>... parameterClasses) throws NoSuchMethodException, SecurityException{
    	Pair<Object, String> key = new Pair<>(null, methodName);
    	 if (methodDict.containsKey(key)) {
             return methodDict.get(key);
         } else {
             Method m = resolveMethod(methodsClass, methodName, parameterClasses);
             methodDict.put(key, m);
             return m;
         }
    }

    private static Method resolveMethod(Class<?> objClass, String methodName, Class<?>... parameterClasses)
            throws NoSuchMethodException, SecurityException {
        try {
            return objClass.getMethod(methodName, parameterClasses);
        } catch (NoSuchMethodException e) {
            for (Method m : objClass.getMethods()) {
                if (m.getName().equals(methodName)) {
                    Class<?>[] paramTypes = m.getParameterTypes();
                    if (paramTypes.length == parameterClasses.length) {
                        boolean isWantedMethod = true;
                        for (int i = 0; i < paramTypes.length; i++) {
                            if (!(isWantedMethod &= paramTypes[i].isAssignableFrom(parameterClasses[i]))) {
                                break;
                            }
                        }
                        if (isWantedMethod) {
                            return m;
                        }
                    }
                }
            }
            throw new NoSuchMethodException(e.getMessage());
        }
    }

}
