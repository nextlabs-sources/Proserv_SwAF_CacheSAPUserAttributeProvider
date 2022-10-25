package com.nextlabs.attributeprovider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.bluejungle.framework.expressions.EvalValue;
import com.bluejungle.framework.expressions.IEvalValue;
import com.bluejungle.framework.expressions.IMultivalue;
import com.bluejungle.framework.expressions.Multivalue;
import com.bluejungle.pf.domain.destiny.serviceprovider.ISubjectAttributeProvider;
import com.bluejungle.pf.domain.destiny.subject.IDSubject;
import com.nextlabs.attributeprovider.cache.ILocalCacheManager;
import com.nextlabs.attributeprovider.cache.LocalCacheManager;
import com.nextlabs.attributeprovider.utils.PropertyLoader;
import com.nextlabs.nxljco.sap.IJCoFunctionHandler;
import com.nextlabs.nxljco.sap.JCoFunctionHandler;
import com.nextlabs.nxljco.utils.Logger;

import net.sf.ehcache.CacheException;

/**
 * @author ypoh
 */
public class CacheSAPUserAttributeProvider implements ISubjectAttributeProvider {
	private static final int TEN_MILLISECONDS = 10;
	private static final String MRPAREA = "mrparea";

	private static final String LOG_ATTRIBUTE_IS_NOT_MRPAREA = "Attribute is not mrparea, skipping the call to SAP...";
	private static final String LOG_NULL_ATTRIBUTE = "Null Attribute";
	private static final String LOG_NULL_SUBJECT = "Null Subject";
	private static final String LOG_CACHE_DURATION_FORMAT = "Cache duration: TimeToLive - %d, TimeToIdle - %d";
	private static final String LOG_EMPTY_USER_ID = "Empty User Id";
	private static final String LOG_NULL_USER_ID = "Null User Id";
	private static final String LOG_LOADING_FROM_SAP = "Loaded from SAP";
	private static final String LOG_LOADING_FROM_CACHE = "Loaded from Cache";
	private static final String LOG_LOADING_FROM_CACHE_NO_WAIT = "Loaded from Cache (No Wait)";
	private static final String LOG_UNABLE_TO_LOAD_PROPERTIES_FORMAT = "Unable to load properties: %s";
	private static final String LOG_RETURNED_VALUE_FORMAT = "Returned value: %s :: %s";
	private static final String LOG_INIT_STARTED = "Cached SAP User Attribute Provider init started.";
	private static final String LOG_INIT_COMPLETED = "Cached SAP User Attribute Provider init completed.";
	private static final String LOG_CACHE_ENABLED = "Cache enabled.";
	private static final String LOG_CACHE_DISABLED = "Cache disabled.";

	private Properties allProperties;
	private IJCoFunctionHandler functionHandler;
	private ILocalCacheManager localCacheManager;
	private static Set<String> inProgressUsers;

	public void init() throws Exception {
		Logger.info(LOG_INIT_STARTED);

		loadProperties();
		loadLocalCacheManager();
		inProgressUsers = new HashSet<String>();

		Logger.info(LOG_INIT_COMPLETED);
	}

	@Override
	public IEvalValue getAttribute(IDSubject subject, String attribute) {
		long start = System.nanoTime();
		try {
			if (attribute == null) {
				Logger.warn(LOG_NULL_ATTRIBUTE);
				long end = System.nanoTime();
				Logger.debug("Time Taken (Null Attribute): " + computeTimeTaken(start, end) + " ms");
				return EvalValue.EMPTY;
			}

			if (subject == null) {
				Logger.warn(LOG_NULL_SUBJECT);
				long end = System.nanoTime();
				Logger.debug("Time Taken (Null Subject): " + computeTimeTaken(start, end) + " ms");
				return EvalValue.EMPTY;
			}

			if (attribute.equalsIgnoreCase(MRPAREA)) {
				// Check user Id
				String userId = subject.getUid();
				if (userId == null) {
					Logger.warn(LOG_NULL_USER_ID);
					return EvalValue.EMPTY;
				}
				if (userId.isEmpty()) {
					Logger.warn(LOG_EMPTY_USER_ID);
					return EvalValue.EMPTY;
				}

				// To Match SAP System
				String upperCaseUserId = userId.toUpperCase();

				IEvalValue value = loadUserAttribute(attribute, upperCaseUserId);

				long end = System.nanoTime();
				Logger.debug("Time Taken (" + attribute + "): " + computeTimeTaken(start, end) + " ms");

				return value;
			} else {
				// Not handling other attributes
				Logger.debug(LOG_ATTRIBUTE_IS_NOT_MRPAREA);
				long end = System.nanoTime();
				Logger.debug("Time Taken (" + attribute + "): " + computeTimeTaken(start, end) + " ms");

				return EvalValue.EMPTY;

			}
		} catch (Exception e) {
			Logger.error(e.getMessage());
			long end = System.nanoTime();
			Logger.debug("Time Taken (" + attribute + " exception): " + computeTimeTaken(start, end) + " ms");

			return EvalValue.EMPTY;
		}

	}
	
	private void loadProperties() {
		// Check Path
		String path = Constants.PROPERTIES_FILE_PATH;

		// Load Properties
		allProperties = PropertyLoader.loadProperties(path);

		// Check if Properties is properly loaded
		if (allProperties == null) {
			Logger.error(String.format(LOG_UNABLE_TO_LOAD_PROPERTIES_FORMAT, path));

			// Use Empty Properties
			allProperties = new Properties();
		}
	}

	private void loadLocalCacheManager() {

		// Check if cache is used
		if (useCache()) {
			Logger.debug(LOG_CACHE_ENABLED);

			int timeToIdleDuration = Constants.DEFAULT_INTEGER_TIME_TO_IDLE_CACHE_DURATION;
			int timeToLiveDuration = Constants.DEFAULT_INTEGER_TIME_TO_LIVE_CACHE_DURATION;

			String timeToIdleDurationString = this.allProperties.getProperty(
					Constants.PROPERTIES_TIME_TO_IDLE_CACHE_DURATION, Constants.DEFAULT_TIME_TO_IDLE_CACHE_DURATION);
			String timeToLiveDurationString = this.allProperties.getProperty(
					Constants.PROPERTIES_TIME_TO_LIVE_CACHE_DURATION, Constants.DEFAULT_TIME_TO_LIVE_CACHE_DURATION);
			try {
				timeToIdleDuration = Integer.valueOf(timeToIdleDurationString);
				timeToLiveDuration = Integer.valueOf(timeToLiveDurationString);
				Logger.debug(String.format(LOG_CACHE_DURATION_FORMAT, timeToLiveDuration, timeToIdleDuration));
			} catch (NumberFormatException e) {
				Logger.error(e.getMessage());
			}

			this.localCacheManager = new LocalCacheManager(timeToIdleDuration, timeToLiveDuration);

		} else {
			Logger.debug(LOG_CACHE_DISABLED);
		}
	}

	private IEvalValue loadUserAttribute(String attribute, String userId) {

		List<String> userValue = getFromCache(userId);

		if (userValue != null && !userValue.isEmpty()) {
			// Loaded from Cache
			Logger.debug(LOG_LOADING_FROM_CACHE_NO_WAIT);
		} else {
			// Wait
			waitWhileInProgress(userId);

			// Load
			userValue = getData(userId, attribute);
		}

		// Evaluate to Plugin Class
		IEvalValue value = evaluateValue(userValue);

		// Debug Log
		Logger.debug(String.format(LOG_RETURNED_VALUE_FORMAT, attribute, value.getValue()));

		return value;
	}

	private void waitWhileInProgress(String userId) {
		while (inProgressUsers.contains(userId)) {
			try {
				Thread.sleep(TEN_MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private synchronized List<String> getData(String userId, String attribute) {

		List<String> userValue = getFromCache(userId);

		if (userValue != null && !userValue.isEmpty()) {
			// Loaded from Cache
			Logger.debug(LOG_LOADING_FROM_CACHE);
		} else {
			// Set InProgress flag
			setUserInProgress(userId);

			// Loaded from Live SAP
			userValue = getFromSAP(userId, Constants.EXPORT_SWAF_DATA);

			// Save To Cache if not empty
			if (!userValue.isEmpty()) {
				putToCache(userId, userValue);
			}

			// Clear InProgress flag
			clearUserInProgress(userId);

			Logger.debug(LOG_LOADING_FROM_SAP);
		}

		return userValue;
	}

	private List<String> getFromCache(String userId) {
		// Skip if cache is not used
		if (!useCache()) {
			return new ArrayList<String>();
		}

		// Try to load from cache
		try {
			List<String> userValue = this.localCacheManager.getFromCache(userId);
			return userValue;
		} catch (CacheException e) {
			Logger.error(e.getMessage());
			return new ArrayList<String>();
		}
	}

	private List<String> getFromSAP(String userId, String attribute) {
		// Setup Input
		Map<String, Object> importsMap = setupInput(userId);

		// Call function
		Map<String, Object> outputValues = callFunction(importsMap);

		// Extract values
		List<String> userValues = extractData(outputValues);

		return userValues;
	}

	private boolean useCache() {
		String useCacheValue = this.allProperties.getProperty(Constants.PROPERTIES_USE_CACHE,
				Constants.DEFAULT_USE_CACHE);

		if (useCacheValue == null) {
			return false;
		}

		return useCacheValue.equalsIgnoreCase(Constants.YES);
	}

	private void putToCache(String userId, List<String> userValue) {
		// Skip if cache is not used
		if (!useCache()) {
			return;
		}

		// Try to save to cache
		try {
			this.localCacheManager.putToCache(userId, userValue);
		} catch (CacheException e) {
			Logger.error(e.getMessage());
		}
	}

	private Map<String, Object> setupInput(String userId) {
		String authValue = this.allProperties.getProperty(Constants.PROPERTIES_AUTH_VALUE,
				Constants.DEFAULT_AUTH_VALUE);
		Map<String, Object> importsMap = new HashMap<String, Object>();

		importsMap.put(Constants.IMPORT_SWAF_USERNAME, userId);
		importsMap.put(Constants.IMPORT_SWAF_AUTH_OBJECT, authValue);

		return importsMap;
	}

	private Map<String, Object> callFunction(Map<String, Object> importsMap) {
		Map<String, Object> outputValues = new HashMap<>();

		try {
			String serverPrefix = allProperties.getProperty(Constants.PROPERTIES_SERVER_PREFIX,
					Constants.DEFAULT_SERVER_PREFIX);
			String name = allProperties.getProperty(Constants.PROPERTIES_HANDLER, Constants.DEFAULT_HANDLER);

			IJCoFunctionHandler handler = createFunctionHandler(serverPrefix);
			handler.callFunction(name, importsMap);
			outputValues = handler.getRfcChanging();
		} catch (IllegalArgumentException e) {
			Logger.error(e.getMessage());
		} catch (IOException e) {
			Logger.error(e.getMessage());
		}

		return outputValues;
	}

	@SuppressWarnings("unchecked")
	private List<String> extractData(Map<String, Object> outputValues) {
		List<String> userValues = new ArrayList<String>();

		// Check attribute existence
		if (!outputValues.containsKey(Constants.EXPORT_SWAF_DATA)) {
			return userValues;
		}

		// Check attribute for null
		if (outputValues.get(Constants.EXPORT_SWAF_DATA) == null) {
			return userValues;
		}

		// Extract
		Object extractedValues = outputValues.get(Constants.EXPORT_SWAF_DATA);
		if (extractedValues instanceof List) {
			userValues = (List<String>) extractedValues;
		}

		return userValues;
	}

	@SuppressWarnings("unchecked")
	private IEvalValue evaluateValue(Object attributeValue) {
		List<String> attributeList = (List<String>) attributeValue;
		Logger.info("Values: " + attributeList.toString());

		if (attributeList.isEmpty()) {
			return EvalValue.EMPTY;
		}

		// Evaluate
		IEvalValue value = EvalValue.EMPTY;
		IMultivalue multiValue = Multivalue.create(attributeList);
		value = EvalValue.build(multiValue);
		return value;
	}

	private void setUserInProgress(String userId) {
		inProgressUsers.add(userId);
	}

	private void clearUserInProgress(String userId) {
		inProgressUsers.remove(userId);
	}

	private IJCoFunctionHandler createFunctionHandler(String serverPrefix) {
		if (this.functionHandler == null) {
			this.functionHandler = new JCoFunctionHandler(serverPrefix);
		}

		return this.functionHandler;
	}

	private String computeTimeTaken(long start, long end){
		long difference = end - start;
		long differenceInMilli = difference / 1000000;
		return Long.toString(differenceInMilli);
	}

	public void setFunctionHandler(IJCoFunctionHandler functionHandler) {
		this.functionHandler = functionHandler;
	}

	public void setLocalCacheManager(ILocalCacheManager localCacheManager) {
		this.localCacheManager = localCacheManager;
	}

	public void setAllProperties(Properties properties) {
		this.allProperties = properties;
	}
}
