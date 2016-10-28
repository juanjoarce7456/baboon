package org.unc.lac.baboon.test.utils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/**
 * <b>LogCatcher</b>
 * 
 * @author Ariel Ivan Rabinovich & Juan Jose Arce Giacobbe
 *         <p>
 *         Test utility. This class uses a handler to obtain logs from a
 *         {@link java.util.logging.Logger} object
 *         </p>
 */
public class LogCatcher {
    private OutputStream logCapturingStream;
    private StreamHandler customLogHandler;

    /**
     * Constructor of LogCatcher Object.
     * 
     * @param logger
     */
    public LogCatcher(Logger logger) {
        attachLogCapturer(logger);
    }

    /**
     * Attaches a handler to the output of the logger given as parameter and
     * starts to capture its logs.
     * 
     * @param logger
     *            A {@link java.util.logging.Logger} object from where the logs
     *            will be captured.
     */
    public void attachLogCapturer(Logger logger) {
        logCapturingStream = new ByteArrayOutputStream();
        Handler[] handlers = logger.getParent().getHandlers();
        customLogHandler = new StreamHandler(logCapturingStream, handlers[0].getFormatter());
        logger.addHandler(customLogHandler);
    }

    /**
     * Returns the captured log as a String object
     * 
     * @return the log captured from the moment
     *         {@link #attachLogCapturer(Logger)} was called
     */
    public String getTestCapturedLog() {
        customLogHandler.flush();
        return logCapturingStream.toString();
    }
}
