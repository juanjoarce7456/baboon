package org.unc.lac.baboon.test.cases;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;
import org.unc.lac.baboon.test.utils.tasks.CustomCounter;
import org.unc.lac.baboon.test.utils.tasks.MockUserSystemObject;
import org.unc.lac.baboon.test.utils.tasks.CustomCounterChild;
import org.unc.lac.baboon.utils.MethodDictionary;

public class MethodDictionaryTest {
    /**
     * <li>Given I have a {@link MethodDictionary} object</li>
     * <li>When I try to resolve a public method of an object by using its name and parameters</li>
     * <li>Then the obtained {@link Method} object should return the same name as provided when calling {@link Method#getName()} </li>
     * <li>Then the obtained {@link Method} object should return the class of the used object when calling {@link Method#getDeclaringClass()} </li>
    */
    @Test
    public void getMethodTest() throws Exception {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String taskMethod = "mockTaskCounter";
        Method methodObj =  MethodDictionary.getMethod(mockUserSystemObj, taskMethod, CustomCounter.class);
        assertEquals(taskMethod, methodObj.getName());
        assertEquals(MockUserSystemObject.class, methodObj.getDeclaringClass());
    }
    
    /**
     * <li>Given I have a {@link MethodDictionary} object</li>
     * <li>When I try to resolve a public method of an object by using its name and a parameter 
     * that inherits from the type declared on the definition of the method</li>
     * <li>Then the obtained {@link Method} object should return the same name as provided when calling {@link Method#getName()} </li>
     * <li>Then the obtained {@link Method} object should return the class of the used object when calling {@link Method#getDeclaringClass()} </li>
    */
    @Test
    public void getMethodWithCompatibleParametersTest() throws NoSuchMethodException, SecurityException {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String taskMethod = "mockTaskCounter";
        Method methodObj =  MethodDictionary.getMethod(mockUserSystemObj, taskMethod, CustomCounterChild.class);
        assertEquals(taskMethod, methodObj.getName());
        assertEquals(MockUserSystemObject.class, methodObj.getDeclaringClass());
    }
    
    /**
     * <li>Given I have a {@link MethodDictionary} object</li>
     * <li>And I have resolved a public method of an object by using its name and parameters</li>
     * <li>When I try to resolve the same method again</li>
     * <li>Then the obtained {@link Method} object should return the same name as provided when calling {@link Method#getName()} </li>
     * <li>Then the obtained {@link Method} object should return the class of the used object when calling {@link Method#getDeclaringClass()} </li>
    */
    @Test
    public void getMethodFromCacheMapTest() throws NoSuchMethodException, SecurityException {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String taskMethod = "mockTask";
        MethodDictionary.getMethod(mockUserSystemObj, taskMethod);
        Method methodObj =  MethodDictionary.getMethod(mockUserSystemObj, taskMethod);
        assertEquals(taskMethod, methodObj.getName());
        assertEquals(MockUserSystemObject.class, methodObj.getDeclaringClass());
    }
    
    /**
     * <li>Given I have a {@link MethodDictionary} object</li>
     * <li>When I try to resolve a public static method of a class by using its name and parameters</li>
     * <li>Then the obtained {@link Method} object should return the same name as provided when calling {@link Method#getName()} </li>
     * <li>Then the obtained {@link Method} object should return the class of the used object when calling {@link Method#getDeclaringClass()} </li>
    */
    @Test
    public void getStaticMethodTest() throws NoSuchMethodException, SecurityException {
        final String taskMethod = "staticMockTask";
        Method methodObj =  MethodDictionary.getStaticMethod(MockUserSystemObject.class,taskMethod);
        assertEquals(taskMethod, methodObj.getName());
        assertEquals(MockUserSystemObject.class, methodObj.getDeclaringClass());
    }
    
    /**
     * <li>Given I have a {@link MethodDictionary} object</li>
     * <li>And I have resolved a public static method of a class by using its name and parameters</li>
     * <li>When I try to resolve the same method again</li>
     * <li>Then the obtained {@link Method} object should return the same name as provided when calling {@link Method#getName()} </li>
     * <li>Then the obtained {@link Method} object should return the class of the used object when calling {@link Method#getDeclaringClass()} </li>
    */
    @Test
    public void getStaticMethodFromCacheMapTest() throws NoSuchMethodException, SecurityException {
        final String taskMethod = "staticMockTask";
        MethodDictionary.getStaticMethod(MockUserSystemObject.class,taskMethod);
        Method methodObj =  MethodDictionary.getStaticMethod(MockUserSystemObject.class,taskMethod);
        assertEquals(taskMethod, methodObj.getName());
        assertEquals(MockUserSystemObject.class, methodObj.getDeclaringClass());
    }

}
