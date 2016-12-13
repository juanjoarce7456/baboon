package org.unc.lac.baboon.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import org.unc.lac.baboon.annotations.GuardProvider;
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
    private static HashMap<Pair<Object, String>, Method> methodDict = new HashMap<Pair<Object, String>, Method>();

    /**
     * Given an object instance and a method's name returns the Method object
     * corresponding to the method name given. The method name provided should
     * be of a method that does not take parameters in its signature.
     * 
     * @param object
     *            an object of the class declaring the method to resolve.
     * @param methodName
     *            the name of the method to resolve.
     * @return a {@link Method} object resolved using object's class and
     *         methodName.
     */
    public static Method getMethod(Object object, String methodName) throws NoSuchMethodException, SecurityException {
        Class<?> c = object.getClass();
        Pair<Object, String> key = new Pair<Object, String>(object, methodName);
        if (methodDict.containsKey(key)) {
            return methodDict.get(key);
        } else {
            Method m = c.getMethod(methodName);
            methodDict.put(key, m);
            return m;
        }
    }

    /**
     * Given an object instance and a guard's name returns the
     * {@link GuardProvider} annotated Method object corresponding to the guard
     * name given. The method name provided should be of a method that does not
     * take parameters in its signature.
     * 
     * @param object
     *            an object of the class declaring the method to resolve.
     * @param guardName
     *            the name of the guard used as value on the
     *            {@link GuardProvider} annotation of the method to resolve.
     * @return a {@link Method} object resolved using object's class and
     *         methodName.
     */
    public static Method getGuardProviderMethod(Object object, String guardName) throws NoSuchMethodException {
        Class<?> c = object.getClass();
        for(Method m : c.getMethods()){
            if(m.isAnnotationPresent(GuardProvider.class)){
                if(m.getAnnotation(GuardProvider.class).value().equals(guardName)){
                    return m;
                }
            }
        }
        
        throw new NoSuchMethodException("There is not a GuardProvider annotated method with value " + guardName);
    }
}
