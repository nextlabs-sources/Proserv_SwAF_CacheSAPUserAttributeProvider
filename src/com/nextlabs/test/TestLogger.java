package com.nextlabs.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.nextlabs.nxljco.utils.Logger;

public class TestLogger {

	@Test
	public void testFatalProperMessage() {
		String logMessage = Logger.fatal("testMessage");
		assertEquals("com.nextlabs.test.TestLogger::testFatalProperMessage : testMessage", logMessage);
	}

	@Test
	public void testFatalEmptyMessage() {
		String logMessage = Logger.fatal("");
		assertEquals("com.nextlabs.test.TestLogger::testFatalEmptyMessage : Empty message", logMessage);
	}

	@Test
	public void testFatalNullMessage() {
		String logMessage = Logger.fatal(null);
		assertEquals("com.nextlabs.test.TestLogger::testFatalNullMessage : Null message", logMessage);
	}

	@Test
	public void testErrorProperMessage() {
		String logMessage = Logger.error("testMessage");
		assertEquals("com.nextlabs.test.TestLogger::testErrorProperMessage : testMessage", logMessage);
	}

	@Test
	public void testErrorEmptyMessage() {
		String logMessage = Logger.error("");
		assertEquals("com.nextlabs.test.TestLogger::testErrorEmptyMessage : Empty message", logMessage);
	}

	@Test
	public void testErrorNullMessage() {
		String logMessage = Logger.error(null);
		assertEquals("com.nextlabs.test.TestLogger::testErrorNullMessage : Null message", logMessage);
	}

	@Test
	public void testWarningProperMessage() {
		String logMessage = Logger.warn("testMessage");
		assertEquals("com.nextlabs.test.TestLogger::testWarningProperMessage : testMessage", logMessage);
	}

	@Test
	public void testWarningEmptyMessage() {
		String logMessage = Logger.warn("");
		assertEquals("com.nextlabs.test.TestLogger::testWarningEmptyMessage : Empty message", logMessage);
	}

	@Test
	public void testWarningNullMessage() {
		String logMessage = Logger.warn(null);
		assertEquals("com.nextlabs.test.TestLogger::testWarningNullMessage : Null message", logMessage);
	}

	@Test
	public void testInfoProperMessage() {
		String logMessage = Logger.info("testMessage");
		assertEquals("com.nextlabs.test.TestLogger::testInfoProperMessage : testMessage", logMessage);
	}

	@Test
	public void testInfoEmptyMessage() {
		String logMessage = Logger.info("");
		assertEquals("com.nextlabs.test.TestLogger::testInfoEmptyMessage : Empty message", logMessage);
	}

	@Test
	public void testInfoNullMessage() {
		String logMessage = Logger.info(null);
		assertEquals("com.nextlabs.test.TestLogger::testInfoNullMessage : Null message", logMessage);
	}

	@Test
	public void testDebugProperMessage() {
		String logMessage = Logger.debug("testMessage");
		assertEquals("com.nextlabs.test.TestLogger::testDebugProperMessage : testMessage", logMessage);
	}

	@Test
	public void testDebugEmptyMessage() {
		String logMessage = Logger.debug("");
		assertEquals("com.nextlabs.test.TestLogger::testDebugEmptyMessage : Empty message", logMessage);
	}

	@Test
	public void testDebugNullMessage() {
		String logMessage = Logger.debug(null);
		assertEquals("com.nextlabs.test.TestLogger::testDebugNullMessage : Null message", logMessage);
	}

}
