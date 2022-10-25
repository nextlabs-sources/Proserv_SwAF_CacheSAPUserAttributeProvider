package com.nextlabs.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import com.bluejungle.framework.expressions.EvalValue;
import com.bluejungle.framework.expressions.IEvalValue;
import com.bluejungle.framework.expressions.IMultivalue;
import com.bluejungle.framework.expressions.Multivalue;
import com.bluejungle.pf.domain.destiny.serviceprovider.ServiceProviderException;
import com.bluejungle.pf.domain.destiny.subject.IDSubject;
import com.nextlabs.attributeprovider.CacheSAPUserAttributeProvider;
import com.nextlabs.attributeprovider.Constants;
import com.nextlabs.attributeprovider.cache.ILocalCacheManager;
import com.nextlabs.nxljco.sap.IJCoFunctionHandler;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.ObjectExistsException;

public class TestAttributeProvider {

	@Test
	public void testNonMRPAreaAttribute() throws ServiceProviderException {
		IDSubject subject = generateSubjectMock();

		CacheSAPUserAttributeProvider uap = new CacheSAPUserAttributeProvider();
		IEvalValue value = uap.getAttribute(subject, "blablabla");
		assertEquals(IEvalValue.EMPTY, value);
	}

	@Test
	public void testNullAttribute() throws ServiceProviderException {
		IDSubject subject = generateSubjectMock();

		CacheSAPUserAttributeProvider uap = new CacheSAPUserAttributeProvider();
		IEvalValue value = uap.getAttribute(subject, null);
		assertEquals(IEvalValue.EMPTY, value);
	}

	@Test
	public void allOKShouldReturnLocal()
			throws ObjectExistsException, CacheException, IOException, ServiceProviderException {
		boolean hasCacheKey = true;
		boolean useCache = true;
		boolean hasCacheUser = true;

		List<String> localListReturn = generateLocalReturn();
		Map<String, Object> remoteMapReturn = generateRemoteReturn();
		IEvalValue expectedValue = generateExpectedLocalReturn();

		IDSubject subject = generateSubjectMock();
		Properties properties = generatePropertiesMock(hasCacheKey, useCache);
		ILocalCacheManager localCacheManager = generateCacheMock(hasCacheUser, localListReturn);
		IJCoFunctionHandler functionHandler = generateFunctionHandlerMock(remoteMapReturn);

		CacheSAPUserAttributeProvider uap = new CacheSAPUserAttributeProvider();
		uap.setAllProperties(properties);
		uap.setLocalCacheManager(localCacheManager);
		uap.setFunctionHandler(functionHandler);
		IEvalValue value = uap.getAttribute(subject, "mrparea");

		assertEquals(expectedValue.getValue(), value.getValue());
	}

	@Test
	public void cacheOKNullMDLGContentShouldReturnLocal()
			throws ObjectExistsException, CacheException, IOException, ServiceProviderException {
		boolean hasCacheKey = true;
		boolean useCache = true;
		boolean hasCacheUser = true;

		List<String> localListReturn = generateLocalReturn();
		Map<String, Object> remoteMapReturn = generateNullMDLGReturn();
		IEvalValue expectedValue = generateExpectedLocalReturn();

		IDSubject subject = generateSubjectMock();
		Properties properties = generatePropertiesMock(hasCacheKey, useCache);
		ILocalCacheManager localCacheManager = generateCacheMock(hasCacheUser, localListReturn);
		IJCoFunctionHandler functionHandler = generateFunctionHandlerMock(remoteMapReturn);

		CacheSAPUserAttributeProvider uap = new CacheSAPUserAttributeProvider();
		uap.setAllProperties(properties);
		uap.setLocalCacheManager(localCacheManager);
		uap.setFunctionHandler(functionHandler);
		IEvalValue value = uap.getAttribute(subject, "mrparea");

		assertEquals(expectedValue.getValue(), value.getValue());
	}

	@Test
	public void cacheOKNonListMDLGContentShouldReturnLocal()
			throws ObjectExistsException, CacheException, IOException, ServiceProviderException {
		boolean hasCacheKey = true;
		boolean useCache = true;
		boolean hasCacheUser = true;

		List<String> localListReturn = generateLocalReturn();
		Map<String, Object> remoteMapReturn = generateNonListMDLGReturn();
		IEvalValue expectedValue = generateExpectedLocalReturn();

		IDSubject subject = generateSubjectMock();
		Properties properties = generatePropertiesMock(hasCacheKey, useCache);
		ILocalCacheManager localCacheManager = generateCacheMock(hasCacheUser, localListReturn);
		IJCoFunctionHandler functionHandler = generateFunctionHandlerMock(remoteMapReturn);

		CacheSAPUserAttributeProvider uap = new CacheSAPUserAttributeProvider();
		uap.setAllProperties(properties);
		uap.setLocalCacheManager(localCacheManager);
		uap.setFunctionHandler(functionHandler);
		IEvalValue value = uap.getAttribute(subject, "mrparea");

		assertEquals(expectedValue.getValue(), value.getValue());
	}

	@Test
	public void cacheOKemptyMDLGContentShouldReturnLocal()
			throws ObjectExistsException, CacheException, IOException, ServiceProviderException {
		boolean hasCacheKey = true;
		boolean useCache = true;
		boolean hasCacheUser = true;

		List<String> localListReturn = generateLocalReturn();
		Map<String, Object> remoteMapReturn = generateEmptyMDLGReturn();
		IEvalValue expectedValue = generateExpectedLocalReturn();

		IDSubject subject = generateSubjectMock();
		Properties properties = generatePropertiesMock(hasCacheKey, useCache);
		ILocalCacheManager localCacheManager = generateCacheMock(hasCacheUser, localListReturn);
		IJCoFunctionHandler functionHandler = generateFunctionHandlerMock(remoteMapReturn);

		CacheSAPUserAttributeProvider uap = new CacheSAPUserAttributeProvider();
		uap.setAllProperties(properties);
		uap.setLocalCacheManager(localCacheManager);
		uap.setFunctionHandler(functionHandler);
		IEvalValue value = uap.getAttribute(subject, "mrparea");

		assertEquals(expectedValue.getValue(), value.getValue());
	}

	@Test
	public void cacheOKNoMDLGKeyShouldReturnLocal()
			throws ObjectExistsException, CacheException, IOException, ServiceProviderException {
		boolean hasCacheKey = true;
		boolean useCache = true;
		boolean hasCacheUser = true;

		List<String> localListReturn = generateLocalReturn();
		Map<String, Object> remoteMapReturn = new HashMap<String, Object>();
		IEvalValue expectedValue = generateExpectedLocalReturn();

		IDSubject subject = generateSubjectMock();
		Properties properties = generatePropertiesMock(hasCacheKey, useCache);
		ILocalCacheManager localCacheManager = generateCacheMock(hasCacheUser, localListReturn);
		IJCoFunctionHandler functionHandler = generateFunctionHandlerMock(remoteMapReturn);

		CacheSAPUserAttributeProvider uap = new CacheSAPUserAttributeProvider();
		uap.setAllProperties(properties);
		uap.setLocalCacheManager(localCacheManager);
		uap.setFunctionHandler(functionHandler);
		IEvalValue value = uap.getAttribute(subject, "mrparea");

		assertEquals(expectedValue.getValue(), value.getValue());
	}

	@Test
	public void cacheNoUserShouldReturnRemote()
			throws ObjectExistsException, CacheException, IOException, ServiceProviderException {
		boolean hasCacheKey = true;
		boolean useCache = true;
		boolean hasCacheUser = false;

		List<String> localListReturn = generateLocalReturn();
		Map<String, Object> remoteMapReturn = generateRemoteReturn();
		IEvalValue expectedValue = generateExpectedRemoteReturn();

		IDSubject subject = generateSubjectMock();
		Properties properties = generatePropertiesMock(hasCacheKey, useCache);
		ILocalCacheManager localCacheManager = generateCacheMock(hasCacheUser, localListReturn);
		IJCoFunctionHandler functionHandler = generateFunctionHandlerMock(remoteMapReturn);

		CacheSAPUserAttributeProvider uap = new CacheSAPUserAttributeProvider();
		uap.setAllProperties(properties);
		uap.setLocalCacheManager(localCacheManager);
		uap.setFunctionHandler(functionHandler);
		IEvalValue value = uap.getAttribute(subject, "mrparea");

		assertEquals(expectedValue.getValue(), value.getValue());
	}

	@Test
	public void notUsingCacheShouldReturnRemote()
			throws ObjectExistsException, CacheException, IOException, ServiceProviderException {
		boolean hasCacheKey = true;
		boolean useCache = false;
		boolean hasCacheUser = true;

		List<String> localListReturn = generateLocalReturn();
		Map<String, Object> remoteMapReturn = generateRemoteReturn();
		IEvalValue expectedValue = generateExpectedRemoteReturn();

		IDSubject subject = generateSubjectMock();
		Properties properties = generatePropertiesMock(hasCacheKey, useCache);
		ILocalCacheManager localCacheManager = generateCacheMock(hasCacheUser, localListReturn);
		IJCoFunctionHandler functionHandler = generateFunctionHandlerMock(remoteMapReturn);

		CacheSAPUserAttributeProvider uap = new CacheSAPUserAttributeProvider();
		uap.setAllProperties(properties);
		uap.setLocalCacheManager(localCacheManager);
		uap.setFunctionHandler(functionHandler);
		IEvalValue value = uap.getAttribute(subject, "mrparea");

		assertEquals(expectedValue.getValue(), value.getValue());
	}

	@Test
	public void noCacheKeyShouldReturnRemote()
			throws ObjectExistsException, CacheException, IOException, ServiceProviderException {
		boolean hasCacheKey = false;
		boolean useCache = true;
		boolean hasCacheUser = true;

		List<String> localListReturn = generateLocalReturn();
		Map<String, Object> remoteMapReturn = generateRemoteReturn();
		IEvalValue expectedValue = generateExpectedRemoteReturn();

		IDSubject subject = generateSubjectMock();
		Properties properties = generatePropertiesMock(hasCacheKey, useCache);
		ILocalCacheManager localCacheManager = generateCacheMock(hasCacheUser, localListReturn);
		IJCoFunctionHandler functionHandler = generateFunctionHandlerMock(remoteMapReturn);

		CacheSAPUserAttributeProvider uap = new CacheSAPUserAttributeProvider();
		uap.setAllProperties(properties);
		uap.setLocalCacheManager(localCacheManager);
		uap.setFunctionHandler(functionHandler);
		IEvalValue value = uap.getAttribute(subject, "mrparea");

		assertEquals(expectedValue.getValue(), value.getValue());
	}

	@Test
	public void noCacheUserAndNullMDLGShouldReturnEmpty()
			throws ObjectExistsException, CacheException, IOException, ServiceProviderException {
		boolean hasCacheKey = false;
		boolean useCache = true;
		boolean hasCacheUser = true;

		List<String> localListReturn = generateLocalReturn();
		Map<String, Object> remoteMapReturn = generateNullMDLGReturn();
		IEvalValue expectedValue = generateExpectedEmptyReturn();

		IDSubject subject = generateSubjectMock();
		Properties properties = generatePropertiesMock(hasCacheKey, useCache);
		ILocalCacheManager localCacheManager = generateCacheMock(hasCacheUser, localListReturn);
		IJCoFunctionHandler functionHandler = generateFunctionHandlerMock(remoteMapReturn);

		CacheSAPUserAttributeProvider uap = new CacheSAPUserAttributeProvider();
		uap.setAllProperties(properties);
		uap.setLocalCacheManager(localCacheManager);
		uap.setFunctionHandler(functionHandler);
		IEvalValue value = uap.getAttribute(subject, "mrparea");

		assertEquals(expectedValue.getValue(), value.getValue());
	}

	@Test
	public void noCacheUserAndNonListMDLGShouldReturnEmpty()
			throws ObjectExistsException, CacheException, IOException, ServiceProviderException {
		boolean hasCacheKey = false;
		boolean useCache = true;
		boolean hasCacheUser = true;

		List<String> localListReturn = generateLocalReturn();
		Map<String, Object> remoteMapReturn = generateNonListMDLGReturn();
		IEvalValue expectedValue = generateExpectedEmptyReturn();

		IDSubject subject = generateSubjectMock();
		Properties properties = generatePropertiesMock(hasCacheKey, useCache);
		ILocalCacheManager localCacheManager = generateCacheMock(hasCacheUser, localListReturn);
		IJCoFunctionHandler functionHandler = generateFunctionHandlerMock(remoteMapReturn);

		CacheSAPUserAttributeProvider uap = new CacheSAPUserAttributeProvider();
		uap.setAllProperties(properties);
		uap.setLocalCacheManager(localCacheManager);
		uap.setFunctionHandler(functionHandler);
		IEvalValue value = uap.getAttribute(subject, "mrparea");

		assertEquals(expectedValue.getValue(), value.getValue());
	}

	@Test
	public void noCacheUserAndEmptyMDLGShouldReturnEmpty()
			throws ObjectExistsException, CacheException, IOException, ServiceProviderException {
		boolean hasCacheKey = false;
		boolean useCache = true;
		boolean hasCacheUser = true;

		List<String> localListReturn = generateLocalReturn();
		Map<String, Object> remoteMapReturn = generateEmptyMDLGReturn();
		IEvalValue expectedValue = generateExpectedEmptyReturn();

		IDSubject subject = generateSubjectMock();
		Properties properties = generatePropertiesMock(hasCacheKey, useCache);
		ILocalCacheManager localCacheManager = generateCacheMock(hasCacheUser, localListReturn);
		IJCoFunctionHandler functionHandler = generateFunctionHandlerMock(remoteMapReturn);

		CacheSAPUserAttributeProvider uap = new CacheSAPUserAttributeProvider();
		uap.setAllProperties(properties);
		uap.setLocalCacheManager(localCacheManager);
		uap.setFunctionHandler(functionHandler);
		IEvalValue value = uap.getAttribute(subject, "mrparea");

		assertEquals(expectedValue.getValue(), value.getValue());
	}

	@Test
	public void noCacheUserAndNoMDLGKeyShouldReturnEmpty()
			throws ObjectExistsException, CacheException, IOException, ServiceProviderException {
		boolean hasCacheKey = false;
		boolean useCache = true;
		boolean hasCacheUser = true;

		List<String> localListReturn = generateLocalReturn();
		Map<String, Object> remoteMapReturn = new HashMap<String, Object>();
		IEvalValue expectedValue = generateExpectedEmptyReturn();

		IDSubject subject = generateSubjectMock();
		Properties properties = generatePropertiesMock(hasCacheKey, useCache);
		ILocalCacheManager localCacheManager = generateCacheMock(hasCacheUser, localListReturn);
		IJCoFunctionHandler functionHandler = generateFunctionHandlerMock(remoteMapReturn);

		CacheSAPUserAttributeProvider uap = new CacheSAPUserAttributeProvider();
		uap.setAllProperties(properties);
		uap.setLocalCacheManager(localCacheManager);
		uap.setFunctionHandler(functionHandler);
		IEvalValue value = uap.getAttribute(subject, "mrparea");

		assertEquals(expectedValue.getValue(), value.getValue());
	}

	private IEvalValue generateExpectedRemoteReturn() {
		List<String> expectedRemoteData = new ArrayList<>();
		expectedRemoteData.add("remoteValue1");
		expectedRemoteData.add("remoteValue2");
		expectedRemoteData.add("remoteValue3");
		expectedRemoteData.add("remoteValue4");
		IMultivalue remoteMultivalue = Multivalue.create(expectedRemoteData);
		IEvalValue expectedRemoteValue = EvalValue.build(remoteMultivalue);
		return expectedRemoteValue;
	}

	private IEvalValue generateExpectedLocalReturn() {
		List<String> expectedLocalData = new ArrayList<>();
		expectedLocalData.add("localValue1");
		expectedLocalData.add("localValue2");
		expectedLocalData.add("localValue3");
		expectedLocalData.add("localValue4");
		IMultivalue localMultivalue = Multivalue.create(expectedLocalData);
		IEvalValue expectedLocalValue = EvalValue.build(localMultivalue);
		return expectedLocalValue;
	}

	private IEvalValue generateExpectedEmptyReturn() {
		List<String> expectedLocalData = new ArrayList<>();
		IMultivalue localMultivalue = Multivalue.create(expectedLocalData);
		IEvalValue expectedLocalValue = EvalValue.build(localMultivalue);
		return expectedLocalValue;
	}

	@SuppressWarnings("unchecked")
	private IJCoFunctionHandler generateFunctionHandlerMock(Map<String, Object> remoteMapReturn) throws IOException {
		IJCoFunctionHandler functionHandler = mock(IJCoFunctionHandler.class);
		when(functionHandler.callFunction(eq("ZNXL_GET_USER_BERID"), any(Map.class))).thenReturn(remoteMapReturn);
		when(functionHandler.getRfcChanging()).thenReturn(remoteMapReturn);
		return functionHandler;
	}

	private ILocalCacheManager generateCacheMock(boolean hasCacheUser, List<String> localListReturn)
			throws ObjectExistsException, CacheException {
		ILocalCacheManager localCacheManager = mock(ILocalCacheManager.class);
		when(localCacheManager.getFromCache("YPOH")).thenReturn(hasCacheUser ? localListReturn : new ArrayList<String>());
		return localCacheManager;
	}

	private Properties generatePropertiesMock(boolean hasCacheKey, boolean useCache) {
		Properties properties = mock(Properties.class);
		when(properties.containsKey(Constants.PROPERTIES_USE_CACHE)).thenReturn(hasCacheKey);
		when(properties.getProperty(Constants.PROPERTIES_USE_CACHE, Constants.DEFAULT_USE_CACHE))
				.thenReturn(hasCacheKey ? (useCache ? Constants.YES : Constants.NO) : Constants.DEFAULT_USE_CACHE);
		when(properties.getProperty(Constants.PROPERTIES_TIME_TO_LIVE_CACHE_DURATION, Constants.DEFAULT_TIME_TO_LIVE_CACHE_DURATION)).thenReturn(Constants.DEFAULT_TIME_TO_LIVE_CACHE_DURATION);
		when(properties.getProperty(Constants.PROPERTIES_TIME_TO_IDLE_CACHE_DURATION, Constants.DEFAULT_TIME_TO_IDLE_CACHE_DURATION)).thenReturn(Constants.DEFAULT_TIME_TO_IDLE_CACHE_DURATION);
		when(properties.getProperty(Constants.PROPERTIES_AUTH_VALUE, Constants.DEFAULT_AUTH_VALUE)).thenReturn("DF_BERID");
		when(properties.getProperty(Constants.PROPERTIES_SERVER_PREFIX, Constants.DEFAULT_SERVER_PREFIX)).thenReturn("SERV4_");
		when(properties.getProperty(Constants.PROPERTIES_HANDLER, Constants.DEFAULT_HANDLER)).thenReturn("ZNXL_GET_USER_BERID");
		return properties;
	}

	private IDSubject generateSubjectMock() {
		IDSubject subject = mock(IDSubject.class);
		when(subject.getUid()).thenReturn("YPOH");
		return subject;
	}

	private List<String> generateLocalReturn() {
		List<String> localListReturn = new ArrayList<String>();
		localListReturn.add("localValue1");
		localListReturn.add("localValue2");
		localListReturn.add("localValue3");
		localListReturn.add("localValue4");
		return localListReturn;
	}

	private Map<String, Object> generateRemoteReturn() {
		List<String> remoteListReturn = new ArrayList<>();
		remoteListReturn.add("remoteValue1");
		remoteListReturn.add("remoteValue2");
		remoteListReturn.add("remoteValue3");
		remoteListReturn.add("remoteValue4");
		Map<String, Object> remoteMapReturn = new HashMap<String, Object>();
		remoteMapReturn.put(Constants.EXPORT_SWAF_DATA, remoteListReturn);
		return remoteMapReturn;
	}

	private Map<String, Object> generateEmptyMDLGReturn() {
		Map<String, Object> remoteMapReturn = new HashMap<String, Object>();
		return remoteMapReturn;
	}

	private Map<String, Object> generateNullMDLGReturn() {
		Map<String, Object> remoteMapReturn = new HashMap<String, Object>();
		remoteMapReturn.put(Constants.EXPORT_SWAF_DATA, null);
		return remoteMapReturn;
	}

	private Map<String, Object> generateNonListMDLGReturn() {
		Map<String, Object> remoteMapReturn = new HashMap<String, Object>();
		remoteMapReturn.put(Constants.EXPORT_SWAF_DATA, "hello");
		return remoteMapReturn;
	}

}
