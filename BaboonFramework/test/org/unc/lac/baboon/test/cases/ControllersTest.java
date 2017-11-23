package org.unc.lac.baboon.test.cases;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;
import org.unc.lac.baboon.actioncontroller.ActionController;
import org.unc.lac.baboon.actioncontroller.HappeningActionController;
import org.unc.lac.baboon.actioncontroller.TaskActionController;
import org.unc.lac.baboon.exceptions.InvalidGuardProviderMethod;
import org.unc.lac.baboon.exceptions.MultipleGuardProvidersException;
import org.unc.lac.baboon.test.utils.tasks.MockUserSystemObject;
import org.unc.lac.baboon.test.utils.tasks.MockUserSystemObjWithBadParamGuardProviders;
import org.unc.lac.baboon.test.utils.tasks.MockUserSystemObjWithBadReturnGuardProviders;
import org.unc.lac.baboon.test.utils.tasks.MockUserSystemObjWithMultipleGuardProvidersProblem;
import org.unc.lac.baboon.utils.MethodDictionary;
import org.unc.lac.baboon.annotations.*;

public class ControllersTest {

    /**
     * <li>Given I have a user's system object with a {@link TaskController} method</li>
     * <li>When I use a null object as actionObject on {@link TaskActionController#TaskActionController(Object, Method, Object...)} </li>
     * <li>Then an {@link IllegalArgumentException } should be thrown</li>
     */
    @Test (expected=IllegalArgumentException.class)
    public void creatingActionControllerWithNullActionObjectShouldThrowException() throws Exception{
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String taskMethod = "mockTask";
        Method methodObj = MethodDictionary.getMethod(mockUserSystemObj, taskMethod); 
        @SuppressWarnings("unused")
        ActionController actionController = new TaskActionController(null,methodObj);
        fail("Exception should have been thrown before this point");
    }
    
    /**
     * <li>Given I have a user's system object with a {@link TaskController} method</li>
     * <li>When I use a null object as actionMethod on {@link TaskActionController#TaskActionController(Object, Method, Object...)} </li>
     * <li>Then an {@link IllegalArgumentException } should be thrown</li>
     */
    @Test (expected=IllegalArgumentException.class)
    public void creatingActionControllerWithNullActionMethodShouldThrowException() throws Exception {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        @SuppressWarnings("unused")
        ActionController actionController = new TaskActionController(mockUserSystemObj,null);
        fail("Exception should have been thrown before this point");
    }
    
    /**
     * <li>Given I have a user's system object with a method that is not annotated</li>
     * <li>When I use this method as actionMethod on {@link TaskActionController#TaskActionController(Object, Method, Object...)} </li>
     * <li>Then an {@link IllegalArgumentException } should be thrown</li>
     */
    @Test (expected=IllegalArgumentException.class)
    public void creatingTaskActionControllerUsingNotAnnotatedMethodShouldThrowException() throws Exception {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String taskMethod = "mockNotSubscribableMethod";
        Method methodObj = MethodDictionary.getMethod(mockUserSystemObj, taskMethod); 
        @SuppressWarnings("unused")
        ActionController actionController = new TaskActionController(mockUserSystemObj,methodObj);
        fail("Exception should have been thrown before this point");
    }
    
    /**
     * <li>Given I have a user's system object with a method that is not annotated</li>
     * <li>When I use this method as actionMethod on {@link HappeningActionController#HappeningActionController(Object, Method)} </li>
     * <li>Then an {@link IllegalArgumentException } should be thrown</li>
     */
    @Test (expected=IllegalArgumentException.class)
    public void creatingHappeningActionControllerUsingNotAnnotatedMethodShouldThrowException() throws Exception {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String taskMethod = "mockNotSubscribableMethod";
        Method methodObj = MethodDictionary.getMethod(mockUserSystemObj, taskMethod); 
        @SuppressWarnings("unused")
        ActionController actionController = new HappeningActionController(mockUserSystemObj,methodObj);
        fail("Exception should have been thrown before this point");
    }
    
    /**
     * <li>Given I have a user's system object with a {@link TaskController} annotated method</li>
     * <li>And the same object has a {@link GuardProvider} annotated method, whose return type is not bool </li>
     * <li>When I use this object as actionObject and this method as actionMethod on {@link TaskActionController#TaskActionController(Object, Method, Object...)} </li>
     * <li>Then an {@link InvalidGuardProviderMethod } should be thrown</li>
     */
    @Test (expected=InvalidGuardProviderMethod.class)
    public void creatingActionControllerWithGuardProviderThatDoesntReturnBooleanShouldThrowException() throws Exception {
        final MockUserSystemObjWithBadReturnGuardProviders mockUserSystemObj = new MockUserSystemObjWithBadReturnGuardProviders();
        final String taskMethod = "mockTask";
        Method methodObj = MethodDictionary.getMethod(mockUserSystemObj, taskMethod);
        
        @SuppressWarnings("unused")
        ActionController actionController = new TaskActionController(mockUserSystemObj,methodObj);
        fail("Exception should have been thrown before this point");
    }
    
    /**
     * <li>Given I have a user's system object with a {@link TaskController} annotated method</li>
     * <li>And the same object has a {@link GuardProvider} annotated method, whose declared parameters list is not void </li>
     * <li>When I use this object as actionObject and this method as actionMethod on {@link TaskActionController#TaskActionController(Object, Method, Object...)} </li>
     * <li>Then an {@link InvalidGuardProviderMethod } should be thrown</li>
     */
    @Test (expected=InvalidGuardProviderMethod.class)
    public void creatingActionControllerWithGuardProviderThatAcceptsParametersShouldThrowException() throws Exception {
        final MockUserSystemObjWithBadParamGuardProviders mockUserSystemObj = new MockUserSystemObjWithBadParamGuardProviders();
        final String taskMethod = "mockTask";
        Method methodObj = MethodDictionary.getMethod(mockUserSystemObj, taskMethod);
        
        @SuppressWarnings("unused")
        ActionController actionController = new TaskActionController(mockUserSystemObj,methodObj);
        fail("Exception should have been thrown before this point");
    }
    
    /**
     * <li>Given I have a user's system object with a static method  annotated with{@link TaskController}</li>
     * <li>And the same object has a {@link GuardProvider} annotated method, which is not static </li>
     * <li>When I use this object as actionObject and this method as actionMethod on {@link TaskActionController#TaskActionController(Object, Method, Object...)} </li>
     * <li>Then an {@link InvalidGuardProviderMethod } should be thrown</li>
     */
    @Test (expected=InvalidGuardProviderMethod.class)
    public void creatingStaticActionControllerWithNonStaticGuardProviderShouldThrowException() throws Exception {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String taskMethod = "staticMockTask";
        Method methodObj = MethodDictionary.getStaticMethod(MockUserSystemObject.class, taskMethod);
        
        @SuppressWarnings("unused")
        ActionController actionController = new TaskActionController(mockUserSystemObj,methodObj);
        fail("Exception should have been thrown before this point");
    }
    
    /**
     * <li>Given I have a user's system object with a {@link TaskController} annotated method</li>
     * <li>And the same object has multiple {@link GuardProvider} annotated methods and they have the same {@link GuardProvider#value()} on the annotation </li>
     * <li>When I use this object as actionObject and this method as actionMethod on {@link TaskActionController#TaskActionController(Object, Method, Object...)} </li>
     * <li>Then an {@link MultipleGuardProvidersException } should be thrown</li>
     */
    @Test (expected=MultipleGuardProvidersException.class)
    public void creatingStaticActionControllerWithMultipleGuardProvidersForTheSameGuardShouldThrowException() throws Exception {
        final MockUserSystemObjWithMultipleGuardProvidersProblem mockUserSystemObj = new MockUserSystemObjWithMultipleGuardProvidersProblem();
        final String taskMethod = "mockTask";
        Method methodObj = MethodDictionary.getMethod(mockUserSystemObj, taskMethod);
        
        @SuppressWarnings("unused")
        ActionController actionController = new TaskActionController(mockUserSystemObj,methodObj);
        fail("Exception should have been thrown before this point");
    }

}
