package org.unc.lac.baboon.test.utils.tasks;

import org.unc.lac.baboon.annotations.GuardProvider;
import org.unc.lac.baboon.annotations.HappeningController;
import org.unc.lac.baboon.annotations.TaskController;

/**
 * {@link MockUserSystemObjWithBadReturnGuardProviders} is used for testing purposes. 
 * It simulates a malformed user system object which has multiple {@GuardProvider} methods for the same guard. 
 * This error should be noticed, rejected and informed by the framework on execution time.
 * a controller using this class.
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * 
 * @see TaskController
 * @see HappeningController
 * @see @GuardProvider
 */
public class MockUserSystemObjWithMultipleGuardProvidersProblem {
    
    @TaskController
    public void mockTask(){
        
    }
    
    
    @GuardProvider("g0")
    public boolean guardProvider(){
        return false;
    }
    
    @GuardProvider("g0")
    public boolean extraGuardProvider(){
        return false;
    }
    
    @GuardProvider("")
    public boolean emptyGuardProvider(){
        return false;
    }

}
