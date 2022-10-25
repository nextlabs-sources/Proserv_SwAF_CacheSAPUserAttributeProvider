package com.nextlabs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.nextlabs.attributeprovider.cache.ILocalCacheManager;
import com.nextlabs.attributeprovider.cache.LocalCacheManager;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;

public class TestLocalCacheManager {


	@Test
	public void allOKShouldReturnOKListForGetFromCache() throws IllegalStateException, CacheException {
		ArrayList<String> mockList = new ArrayList<String>();
		mockList.add("testValue1");
		mockList.add("testValue2");
		mockList.add("testValue3");

		Cache mockCache = mock(Cache.class);
		Element mockElement = mock(Element.class);

		when(mockCache.get(any(String.class))).thenReturn(mockElement);
		when(mockCache.isExpired(mockElement)).thenReturn(false);
		when(mockElement.getValue()).thenReturn(mockList);

		ILocalCacheManager localCacheManager = new LocalCacheManager(300, 300);
		((LocalCacheManager) localCacheManager).setCache(mockCache);
		List<String> returnList = localCacheManager.getFromCache("YPOH");

		ArrayList<String> expectedList = new ArrayList<String>();
		expectedList.add("testValue1");
		expectedList.add("testValue2");
		expectedList.add("testValue3");

		assertEquals(expectedList, returnList);
	}

	@Test
	public void notExistShouldReturnEmptyListForGetFromCache() throws IllegalStateException, CacheException {
		ArrayList<String> mockList = new ArrayList<String>();
		mockList.add("testValue1");
		mockList.add("testValue2");
		mockList.add("testValue3");

		Cache mockCache = mock(Cache.class);
		Element mockElement = mock(Element.class);

		when(mockCache.get(any(String.class))).thenReturn(null);
		when(mockCache.isExpired(mockElement)).thenReturn(false);
		when(mockElement.getValue()).thenReturn(mockList);

		ILocalCacheManager localCacheManager = new LocalCacheManager(300, 300);
		((LocalCacheManager) localCacheManager).setCache(mockCache);
		List<String> returnList = localCacheManager.getFromCache("YPOH");

		ArrayList<String> expectedList = new ArrayList<String>();

		assertEquals(expectedList, returnList);
	}

	@Test
	public void allOKShouldReturnTrueForPutToCache() throws IllegalStateException, CacheException {
		ArrayList<String> dataList = new ArrayList<String>();

		Cache mockCache = mock(Cache.class);

		ILocalCacheManager localCacheManager = new LocalCacheManager(300, 300);
		((LocalCacheManager) localCacheManager).setCache(mockCache);
		boolean saved = localCacheManager.putToCache("YPOH", dataList);

		assertTrue(saved);
	}
}
