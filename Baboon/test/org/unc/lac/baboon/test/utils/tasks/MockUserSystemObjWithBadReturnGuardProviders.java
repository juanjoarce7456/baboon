package org.unc.lac.baboon.test.utils.tasks;

import org.unc.lac.baboon.annotations.GuardProvider;
import org.unc.lac.baboon.annotations.HappeningController;
import org.unc.lac.baboon.annotations.TaskController;
/**
 * {@link MockUserSystemObjWithBadReturnGuardProviders} is used for testing purposes. 
 * It simulates a malformed user system object which has an incorrect {@GuardProvider} method structure. 
 * This error should be noticed, rejected and informed by the framework on execution time.
 * a controller using this class.
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * 
 * @see TaskController
 * @see HappeningController
 * @see @GuardProvider
 */
public class MockUserSystemObjWithBadReturnGuardProviders {
    
    @TaskController
    public void mockTask(){
        
    }
    
    @TaskController
    public static void staticMockTask(){
        
    }
    
    @GuardProvider("g1")
    public int badReturnGuardProvider(){
        return 0;
    }

}
