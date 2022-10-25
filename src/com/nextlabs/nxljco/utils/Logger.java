package com.nextlabs.nxljco.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Logger {
	private static final String EMPTY_MESSAGE = "Empty message";
	private static final String NULL_MESSAGE = "Null message";
	private static final String LOG_FORMAT = "%s::%s : %s";

	public static String debug(String message) {
		Log log = LogFactory.getLog(Logger.class);
		String logMessage = buildMessage(message);
		log.debug(logMessage);
		return logMessage;
	}

	public static String info(String message) {
		Log log = LogFactory.getLog(Logger.class);
		String logMessage = buildMessage(message);
		log.info(logMessage);
		return logMessage;
	}

	public static String warn(String message) {
		Log log = LogFactory.getLog(Logger.class);
		String logMessage = buildMessage(message);
		log.warn(logMessage);
		return logMessage;
	}

	public static String error(String message) {
		Log log = LogFactory.getLog(Logger.class);
		String logMessage = buildMessage(message);
		log.error(logMessage);
		return logMessage;
	}

	public static String fatal(String message) {
		Log log = LogFactory.getLog(Logger.class);
		String logMessage = buildMessage(message);
		log.fatal(logMessage);
		return logMessage;
	}

	private static String buildMessage(String message) {
		StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[3];
		String className = stackTrace.getClassName();
		String methodName = stackTrace.getMethodName();

		message = processMessage(message);

		String logMessage = String.format(LOG_FORMAT, className, methodName, message);
		return logMessage;
	}

	private static String processMessage(String message) {
		// Check for null
		if (message == null) {
			message = NULL_MESSAGE;
		}

		// Check for empty
		if (message.isEmpty()) {
			message = EMPTY_MESSAGE;
		}
		return message;
	}
}
