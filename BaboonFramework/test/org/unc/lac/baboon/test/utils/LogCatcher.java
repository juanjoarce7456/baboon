package org.unc.lac.baboon.test.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

public class LogCatcher {
	private OutputStream logCapturingStream;
	private StreamHandler customLogHandler;

	public LogCatcher() {
	}

	public LogCatcher(Logger log) {
		attachLogCapturer(log);
	}

	public void attachLogCapturer(Logger log) {
		logCapturingStream = new ByteArrayOutputStream();
		Handler[] handlers = log.getParent().getHandlers();
		customLogHandler = new StreamHandler(logCapturingStream, handlers[0].getFormatter());
		log.addHandler(customLogHandler);
	}

	public String getTestCapturedLog() throws IOException {
		customLogHandler.flush();
		String result = logCapturingStream.toString();
		return result;
	}
}
