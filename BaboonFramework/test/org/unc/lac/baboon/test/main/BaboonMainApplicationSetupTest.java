package org.unc.lac.baboon.test.main;

import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.unc.lac.baboon.main.BaboonFramework;
import org.unc.lac.baboon.test.utils.LogCatcher;
import org.unc.lac.baboon.test.utils.appsetup.AppSetup1;
import org.unc.lac.baboon.test.utils.appsetup.AppSetup2;
import org.unc.lac.baboon.test.utils.appsetup.ConcreteAppSetup;

public class BaboonMainApplicationSetupTest {

    final String expectedLogForDeclareMethod = "Declaring";
    final String expectedLogForSubscribeMethod = "Subscribing";
    LogCatcher logCatcher1, logCatcher2, logCatcher3;

    /**
     * <li><b>Background:</b></li>
     * <li>Given I initialize logCatcher1 for AppSetup1 class</li>
     * <li>And I initialize logCatcher2 for AppSetup2 class</li>
     * <li>And I initialize logCatcher3 for ConcreteAppSetup class</li>
     * <li>And I run BaboonFramework's main</li>
     */
    @Before
    public void createLogCatcherObjectsAndRunMain() {
        // Log catchers must be instantiated before running
        // BaboonFramework.main()
        // otherwise log.getTestCapturedLog() will return an empty String.
        logCatcher1 = new LogCatcher(Logger.getLogger(AppSetup1.class.getName()));
        logCatcher2 = new LogCatcher(Logger.getLogger(AppSetup2.class.getName()));
        logCatcher3 = new LogCatcher(Logger.getLogger(ConcreteAppSetup.class.getName()));
        BaboonFramework.main(null);
    }

    /**
     * <li>Given I have a class named AppSetup1 which implements
     * BaboonApplicationSetup interface</li>
     * <li>And AppSetup1 creates an information log with string "Declare 1" when
     * its declare() method runs</li>
     * <li>And logCatcher1 contains the log written by AppSetup1</li>
     * <li>When I get the log from logCatcher1</li>
     * <li>Then the log contains "Declare"</li>
     */
    @Test
    public void testMainCallsDeclareMethod() {
        String capturedLog = logCatcher1.getTestCapturedLog();
        System.out.println("capturedLog: \n" + capturedLog);
        Assert.assertTrue(capturedLog.contains(expectedLogForDeclareMethod));
    }

    /**
     * <li>Given I have a class named AppSetup1 which implements
     * BaboonApplicationSetup interface</li>
     * <li>And AppSetup1 creates an information log with string "Subscribe 1"
     * when its subscribe() method runs</li>
     * <li>And logCatcher1 contains the log written by AppSetup1</li>
     * <li>When I get the log from logCatcher1</li>
     * <li>Then the log contains "Subscribe"</li>
     */
    @Test
    public void testMainCallsSubscribeMethod() {
        String capturedLog = logCatcher1.getTestCapturedLog();
        System.out.println("capturedLog: \n" + capturedLog);
        Assert.assertTrue(capturedLog.contains(expectedLogForSubscribeMethod));
    }

    /**
     * <li>Given I have a class named AppSetup1 which implements
     * BaboonApplicationSetup interface</li>
     * <li>And AppSetup1 creates an information log with string "Declare 1" when
     * its declare() method runs</li>
     * <li>And AppSetup1 creates an information log with string "Subscribe 1"
     * when its subscribe() method runs</li>
     * <li>And logCatcher1 contains the log written by AppSetup1</li>
     * <li>And I get the log from logCatcher1</li>
     * <li>And I check the index of last occurrence of a log message containing
     * "Declare"</li>
     * <li>And I check the index of the first occurrence of a log message
     * containing "Subscribe"</li>
     * <li>Then the index of last "Declare" message is lesser than the index of
     * the first "Subscribe" message</li>
     * <li>And the index of the first "Subscribe" message is greater than zero
     * </li>
     * <li>And the index of the last "Declare" message is greater than zero</li>
     */
    @Test
    public void testMainCallsDeclareAndSubscribeInThatOrder() {
        String capturedLog = logCatcher1.getTestCapturedLog();
        int dI = capturedLog.lastIndexOf(expectedLogForDeclareMethod);
        int sI = capturedLog.indexOf(expectedLogForSubscribeMethod);
        Assert.assertTrue((sI > dI) && (dI >= 0) && (sI >= 0));
    }

    /**
     * <li>Given I have a class named AppSetup1 which implements
     * BaboonApplicationSetup interface</li>
     * <li>And I have a class named AppSetup2 which implements
     * BaboonApplicationSetup interface</li>
     * <li>And AppSetup1 creates an information log with string "Declare 1" when
     * its declare() method runs</li>
     * <li>And AppSetup1 creates an information log with string "Subscribe 1"
     * when its subscribe() method runs</li>
     * <li>And AppSetup2 creates an information log with string "Declare 2" when
     * its declare() method runs</li>
     * <li>And AppSetup2 creates an information log with string "Subscribe 2"
     * when its subscribe() method runs</li>
     * <li>And logCatcher1 contains the log written by AppSetup1</li>
     * <li>And logCatcher2 contains the log written by AppSetup2</li>
     * <li>When I get the log from logCatcher1</li>
     * <li>And I get the log from logCatcher2</li>
     * <li>Then the log from logCatcher1 contains "Subscribe 1"</li>
     * <li>And the log from logCatcher1 contains "Declare 1"</li>
     * <li>And the log from logCatcher2 contains "Subscribe 2"</li>
     * <li>And the log from logCatcher2 contains "Declare 2"</li>
     * <li>Then the result is true</li>
     */
    @Test
    public void testMainCallsDeclareAndSubscribeMethodsForAllSetupClasses() {
        String capturedLog1 = logCatcher1.getTestCapturedLog();
        String capturedLog2 = logCatcher2.getTestCapturedLog();
        Assert.assertTrue(capturedLog1.contains(expectedLogForDeclareMethod + " 1"));
        Assert.assertTrue(capturedLog1.contains(expectedLogForSubscribeMethod + " 1"));
        Assert.assertTrue(capturedLog2.contains(expectedLogForDeclareMethod + " 2"));
        Assert.assertTrue(capturedLog2.contains(expectedLogForSubscribeMethod + " 2"));
    }

    /**
     * <li>Given I have an abstract class named AbstractAppSetup which
     * implements BaboonApplicationSetup interface</li>
     * <li>And I have a concrete class named ConcreteAppSetup which extends from
     * AbstractAppSetup</li>
     * <li>And ConcreteAppSetup creates an information log with string "Declare
     * Concrete" when its declare() method runs</li>
     * <li>And ConcreteAppSetup creates an information log with string
     * "Subscribe Concrete" when its subscribe() method runs</li>
     * <li>And logCatcher3 contains the log written by ConcreteAppSetup</li>
     * <li>When I get the log from logCatcher3</li>
     * <li>Then the log contains "Declare Concrete"</li>
     * <li>And the log contains "Subscribe Concrete"</li>
     */
    @Test
    public void testMainCallsDeclareAndSubscribeMethodsForConcreteClassExtendingAbstractSetupClass() {
        String capturedLog = logCatcher3.getTestCapturedLog();
        Assert.assertTrue(capturedLog.contains(expectedLogForDeclareMethod + " Concrete"));
        Assert.assertTrue(capturedLog.contains(expectedLogForSubscribeMethod + " Concrete"));
    }
}
