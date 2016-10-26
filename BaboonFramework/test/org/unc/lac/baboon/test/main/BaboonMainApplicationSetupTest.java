package org.unc.lac.baboon.test.main;

import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.unc.lac.baboon.main.BaboonFramework;
import org.unc.lac.baboon.test.utils.AppSetup1;
import org.unc.lac.baboon.test.utils.AppSetup2;
import org.unc.lac.baboon.test.utils.LogCatcher;

public class BaboonMainApplicationSetupTest {
	private static LogCatcher log1 = new LogCatcher(Logger.getLogger(AppSetup1.class.getName()));
	private static LogCatcher log2 = new LogCatcher(Logger.getLogger(AppSetup2.class.getName()));

	final String expectedLogForDeclareMethod = "Declaring";
	final String expectedLogForSubscribeMethod = "Subscribing";

	/**
	 * <li> Given I run BaboonFramework's main </li>
	 */
	@BeforeClass
	public static void runMain() {
		BaboonFramework.main(null);
	}
	
	/**
	 * <li> Given I have a class named AppSetup1 which implements BaboonApplicationSetup interface </li>
	 * <li> And AppSetup1 creates an information log with string "Declare 1" when its declare() method runs </li>
	 * <li> And log1 contains the log written by AppSetup1 </li>
	 * <li> And I get the log from log1</li>
	 * <li> When I check if the log contains "Declare"</li>
	 * <li> Then the result is true </li>
	 */
	@Test
	public void testMainCallsDeclareMethod() throws Exception {
		String capturedLog = log1.getTestCapturedLog();
		Assert.assertTrue(capturedLog.contains(expectedLogForDeclareMethod));
	}

	/**
	 * <li> Given I have a class named AppSetup1 which implements BaboonApplicationSetup interface </li>
	 * <li> And AppSetup1 creates an information log with string "Subscribe 1" when its subscribe() method runs </li>
	 * <li> And log1 contains the log written by AppSetup1 </li>
	 * <li> And I get the log from log1</li>
	 * <li> When I check if the log contains "Subscribe"</li>
	 * <li> Then the result is true </li>
	 */
	@Test
	public void testMainCallsSubscribeMethod() throws Exception {
		String capturedLog = log1.getTestCapturedLog();
		Assert.assertTrue(capturedLog.contains(expectedLogForSubscribeMethod));
	}

	/**
	 * <li> Given I have a class named AppSetup1 which implements BaboonApplicationSetup interface </li>
	 * <li> And AppSetup1 creates an information log with string "Declare 1" when its declare() method runs </li>
	 * <li> And AppSetup1 creates an information log with string "Subscribe 1" when its subscribe() method runs </li>
	 * <li> And log1 contains the log written by AppSetup1 </li>
	 * <li> And I get the log from log1</li>
	 * <li> And I check the index of last occurrence of a log message containing "Declare"</li>
	 * <li> And I check the index of the first occurrence of a log message containing "Subscribe"</li>
	 * <li> Then the index of last "Declare" message is lesser than the index of the first "Subscribe" message</li>
	 * <li> And the index of the first "Subscribe" message is greater than zero </li>
	 * <li> And the index of the last "Declare" message is greater than zero </li>
	 */
	@Test
	public void testMainCallsDeclareAndSubscribeInThatOrder() throws Exception {
		String capturedLog = log1.getTestCapturedLog();
		int dI = capturedLog.lastIndexOf(expectedLogForDeclareMethod);
		int sI = capturedLog.indexOf(expectedLogForSubscribeMethod);
		Assert.assertTrue((sI > dI) && (dI >= 0) && (sI >= 0));
	}

	/**
	 * <li> Given I have a class named AppSetup1 which implements BaboonApplicationSetup interface </li>
	 * <li> And I have a class named AppSetup2 which implements BaboonApplicationSetup interface </li>
	 * <li> And AppSetup1 creates an information log with string "Declare 1" when its declare() method runs </li>
	 * <li> And AppSetup1 creates an information log with string "Subscribe 1" when its subscribe() method runs </li>
	 * <li> And AppSetup2 creates an information log with string "Declare 2" when its declare() method runs </li>
	 * <li> And AppSetup2 creates an information log with string "Subscribe 2" when its subscribe() method runs </li>
	 * <li> And log1 contains the log written by AppSetup1 </li>
	 * <li> And log2 contains the log written by AppSetup2 </li>
	 * <li> And I get the log from log1</li>
	 * <li> And I get the log from log2</li>
	 * <li> When I check if the log1 contains "Subscribe"</li>
	 * <li> And I check if the log1 contains "Declare"</li>
	 * <li> And I check if the log2 contains "Subscribe"</li>
	 * <li> And I check if the log2 contains "Declare"</li>
	 * <li> Then the result is true</li>
	 */
	@Test
	public void testMainCallsDeclareMethodForAllSetupClasses() throws Exception {
		String capturedLog1 = log1.getTestCapturedLog();
		String capturedLog2 = log2.getTestCapturedLog();
		Assert.assertTrue(capturedLog1.contains(expectedLogForDeclareMethod + " 1"));
		Assert.assertTrue(capturedLog1.contains(expectedLogForSubscribeMethod + " 1"));
		Assert.assertTrue(capturedLog2.contains(expectedLogForDeclareMethod + " 2"));
		Assert.assertTrue(capturedLog2.contains(expectedLogForSubscribeMethod + " 2"));
	}

}
