package org.unc.lac.baboon.utils;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.javatuples.Pair;

public class MethodDictionary {
    private static HashMap<Pair<Object, String>, Method> methodDict = new HashMap<Pair<Object, String>, Method>();

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
}
