package org.unc.lac.baboon.test.main;

import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.unc.lac.baboon.main.BaboonFramework;
import org.unc.lac.baboon.test.utils.LogCatcher;
import org.unc.lac.baboon.test.utils.appsetup.AbstractAppSetup;
import org.unc.lac.baboon.test.utils.appsetup.AppSetup1;
import org.unc.lac.baboon.test.utils.appsetup.AppSetup2;
import org.unc.lac.baboon.test.utils.appsetup.ConcreteAppSetupNotOverrides;
import org.unc.lac.baboon.test.utils.appsetup.ConcreteAppSetupOverrides;

public class BaboonMainApplicationSetupTest {

    final String expectedLogForDeclareMethod = "Declaring";
    final String expectedLogForSubscribeMethod = "Subscribing";
    LogCatcher logCatcher1, logCatcher2, logCatcher3, logCatcher4, logCatcher5;

    /**
     * <li><b>Background:</b></li>
     * <li>Given I initialize logCatcher1 for {@link AppSetup1} class</li>
     * <li>And I initialize logCatcher2 for {@link AppSetup2} class</li>
     * <li>And I initialize logCatcher3 for {@link ConcreteAppSetupOverrides}
     * class</li>
     * <li>And I initialize logCatcher4 for {@link AbstractAppSetup} class</li>
     * <li>And I initialize logCatcher4 for {@link ConcreteAppSetupNotOverrides}
     * class</li>
     * <li>And I run BaboonFramework's main</li>
     */
    @Before
    public void createLogCatcherObjectsAndRunMain() {
        // Log catchers must be instantiated before running
        // BaboonFramework.main()
        // otherwise log.getTestCapturedLog() will return an empty String.
        logCatcher1 = new LogCatcher(Logger.getLogger(AppSetup1.class.getName()));
        logCatcher2 = new LogCatcher(Logger.getLogger(AppSetup2.class.getName()));
        logCatcher3 = new LogCatcher(Logger.getLogger(ConcreteAppSetupOverrides.class.getName()));
        logCatcher4 = new LogCatcher(Logger.getLogger(AbstractAppSetup.class.getName()));
        logCatcher5 = new LogCatcher(Logger.getLogger(ConcreteAppSetupNotOverrides.class.getName()));
        BaboonFramework.main(null);
    }

    /**
     * <li>Given I have a class {@link AppSetup1} which implements
     * {@link BaboonApplication} interface</li>
     * <li>And {@link AppSetup1} creates an information log with string "Declare
     * 1" when its {@link AppSetup1#declare()} method runs</li>
     * <li>And logCatcher1 contains the log written by {@link AppSetup1}</li>
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
     * <li>Given I have a class named {@link AppSetup1} which implements
     * {@link BaboonApplication} interface</li>
     * <li>And {@link AppSetup1} creates an information log with string
     * "Subscribe 1" when its {@link AppSetup1#subscribe()} method runs</li>
     * <li>And logCatcher1 contains the log written by {@link AppSetup1}</li>
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
     * <li>Given I have a class named {@link AppSetup1} which implements
     * {@link BaboonApplication} interface</li>
     * <li>And {@link AppSetup1} creates an information log with string "Declare
     * 1" when its {@link AppSetup1#declare()} method runs</li>
     * <li>And {@link AppSetup1} creates an information log with string
     * "Subscribe 1" when its subscribe() method runs</li>
     * <li>And logCatcher1 contains the log written by {@link AppSetup1}</li>
     * <li>When I get the log from logCatcher1</li>
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
     * <li>Given I have a class named {@link AppSetup1} which implements
     * {@link BaboonApplication} interface</li>
     * <li>And I have a class named {@link AppSetup2} which implements
     * {@link BaboonApplication} interface</li>
     * <li>And {@link AppSetup1} creates an information log with string "Declare
     * 1" when its {@link AppSetup1#declare()} method runs</li>
     * <li>And {@link AppSetup1} creates an information log with string
     * "Subscribe 1" when its {@link AppSetup1#subscribe()} method runs</li>
     * <li>And {@link AppSetup2} creates an information log with string "Declare
     * 2" when its {@link AppSetup2#declare()} method runs</li>
     * <li>And {@link AppSetup2} creates an information log with string
     * "Subscribe 2" when its {@link AppSetup2#subscribe()} method runs</li>
     * <li>And logCatcher1 contains the log written by {@link AppSetup1}</li>
     * <li>And logCatcher2 contains the log written by {@link AppSetup2}</li>
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
     * <li>Given I have an abstract class named {@link AbstractAppSetup} which
     * implements {@link BaboonApplication} interface</li>
     * <li>And I have a concrete class named {@link ConcreteAppSetupOverrides}
     * which extends from {@link AbstractAppSetup}</li>
     * <li>And {@link AbstractAppSetup} creates an information log with string
     * "Declare Abstract from: {this object simple class name}" when its
     * {@link AbstractAppSetup#declare()} method runs</li>
     * <li>And {@link AbstractAppSetup} creates an information log with string
     * "Subscribe Abstract from: {this object simple class name}" when its
     * {@link AbstractAppSetup#subscribe()} method runs</li>
     * <li>And {@link ConcreteAppSetupOverrides} overrides
     * {@link AbstractAppSetup#declare()} method</li> *
     * <li>And {@link ConcreteAppSetupOverrides} overrides
     * {@link AbstractAppSetup#subscribe()} method</li>
     * <li>And {@link ConcreteAppSetupOverrides} creates an information log with
     * string "Declare Concrete" when its
     * {@link ConcreteAppSetupOverrides#declare()} method runs</li>
     * <li>And {@link ConcreteAppSetupOverrides} creates an information log with
     * string "Subscribe Concrete" when its
     * {@link ConcreteAppSetupOverrides#subscribe()} method runs</li>
     * <li>And logCatcher3 contains the log written by
     * {@link ConcreteAppSetupOverrides}</li>
     * <li>And logCatcher4 contains the log written by
     * {@link AbstractAppSetup}</li>
     * <li>When I get the log from logCatcher3</li>
     * <li>And I get the log from logCatcher4</li>
     * <li>Then the log from logCatcher3 contains "Declare Concrete"</li>
     * <li>And the log from logCatcher3 contains "Subscribe Concrete"</li>
     * <li>And the log from logCatcher4 does not contain "Declare Abstract from:
     * ConcreteAppSetupOverrides"</li>
     * <li>And the log from logCatcher4 does not contain "Subscribe Abstract
     * from: ConcreteAppSetupOverrides"</li>
     * <li>And the log from logCatcher4 does not contain "Declare Abstract from:
     * AbstractAppSetup"</li>
     * <li>And the log from logCatcher4 does not contain "Subscribe Abstract
     * from: AbstractAppSetup"</li>
     */
    @Test
    public void testMainCallsDeclareAndSubscribeMethodsForConcreteClassExtendingAbstractSetupClassOverridingThisMethods() {
        String capturedLog = logCatcher3.getTestCapturedLog();
        String capturedLog2 = logCatcher4.getTestCapturedLog();
        Assert.assertTrue(capturedLog.contains(expectedLogForDeclareMethod + " Concrete"));
        Assert.assertTrue(capturedLog.contains(expectedLogForSubscribeMethod + " Concrete"));
        Assert.assertFalse(
                capturedLog2.contains(expectedLogForDeclareMethod + " Abstract from: ConcreteAppSetupOverrides"));
        Assert.assertFalse(
                capturedLog2.contains(expectedLogForSubscribeMethod + " Abstract from: ConcreteAppSetupOverrides"));
        Assert.assertFalse(capturedLog2.contains(expectedLogForDeclareMethod + " Abstract from: AbstractAppSetup"));
        Assert.assertFalse(capturedLog2.contains(expectedLogForSubscribeMethod + " Abstract from: AbstractAppSetup"));
    }

    /**
     * <li>Given I have an abstract class named {@link AbstractAppSetup} which
     * implements {@link BaboonApplication} interface</li>
     * <li>And I have a concrete class named
     * {@link ConcreteAppSetupNotOverrides} which extends from
     * {@link AbstractAppSetup}</li>
     * <li>And {@link AbstractAppSetup} creates an information log with string
     * "Declare Abstract from: {this object simple class name}" when its
     * {@link AbstractAppSetup#declare()} method runs</li>
     * <li>And {@link AbstractAppSetup} creates an information log with string
     * "Subscribe Abstract from: {this object simple class name}" when its
     * {@link AbstractAppSetup#subscribe()} method runs</li>
     * <li>And {@link ConcreteAppSetupOverrides} creates an information log with
     * string "Instantiating Concrete" on its constructor</li>
     * <li>And logCatcher5 contains the log written by
     * {@link ConcreteAppSetupNotOverrides}</li>
     * <li>And logCatcher4 contains the log written by
     * {@link AbstractAppSetup}</li>
     * <li>When I get the log from logCatcher5</li>
     * <li>And I get the log from logCatcher4</li>
     * <li>Then the log from logCatcher5 contains "Instantiating Concrete"</li>
     * <li>And the log from logCatcher4 contains "Declare Abstract from:
     * ConcreteAppSetupNotOverrides"</li>
     * <li>And the log from logCatcher4 contains "Subscribe Abstract from:
     * ConcreteAppSetupNotOverrides"</li>
     * <li>And the log from logCatcher4 does not contain "Declare Abstract from:
     * AbstractAppSetup"</li>
     * <li>And the log from logCatcher4 does not contain "Subscribe Abstract
     * from: AbstractAppSetup"</li>
     */
    @Test
    public void testMainCallsDeclareAndSubscribeMethodsForConcreteClassExtendingAbstractSetupClassNotOverridingThisMethods() {
        String capturedLog = logCatcher5.getTestCapturedLog();
        String capturedLog2 = logCatcher4.getTestCapturedLog();
        Assert.assertTrue(capturedLog.contains("Instantiating Concrete"));
        Assert.assertTrue(
                capturedLog2.contains(expectedLogForDeclareMethod + " Abstract from: ConcreteAppSetupNotOverrides"));
        Assert.assertTrue(
                capturedLog2.contains(expectedLogForSubscribeMethod + " Abstract from: ConcreteAppSetupNotOverrides"));
        Assert.assertFalse(capturedLog2.contains(expectedLogForDeclareMethod + " Abstract from: AbstractAppSetup"));
        Assert.assertFalse(capturedLog2.contains(expectedLogForSubscribeMethod + " Abstract from: AbstractAppSetup"));
    }
}
