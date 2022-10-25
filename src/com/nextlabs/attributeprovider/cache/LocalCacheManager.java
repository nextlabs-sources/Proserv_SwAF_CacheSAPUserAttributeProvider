package com.nextlabs.attributeprovider.cache;

import java.util.ArrayList;
import java.util.List;

import com.nextlabs.nxljco.utils.Logger;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.ObjectExistsException;

public class LocalCacheManager implements ILocalCacheManager {
	private static final String LOG_CACHED_DATA_NOT_EXIST = "Cached data not exist.";

	private static final String CACHE_NAME = "SAP_Cache";

	private static final int NUMBER_OF_USERS = 8000;
	private static final int MEMORY_SIZE_BUFFER = 100;

	private static final boolean NOT_ETERNAL = false;
	private static final boolean NOT_ALLOW_OVERFLOW_TO_DISK = false;
	private static final int MAXIMUM_NUMBER_OF_ELEMENT_BEFORE_EVICTION = NUMBER_OF_USERS + MEMORY_SIZE_BUFFER;

	private int timeToLive;
	private int timeToIdle;

	private Cache cache;

	public LocalCacheManager(int timeToLiveDuration, int timeToIdleDuration) {
		this.timeToIdle = timeToIdleDuration;
		this.timeToLive = timeToLiveDuration;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getFromCache(String userId) throws CacheException, ObjectExistsException {
		Cache cache = getCacheInstance();
		List<String> userValue = new ArrayList<String>();

		// Get element
		Element element = getElement(userId, cache);

		// Check element
		if (!isElementValid(element)) {
			return userValue;
		}

		// Extract value
		userValue = (List<String>) element.getValue();

		return userValue;
	}

	@Override
	public boolean putToCache(String userId, List<String> userValue) throws ObjectExistsException, CacheException {
		Cache cache = getCacheInstance();

		// Generate Element
		Element element = new Element(userId, (ArrayList<String>) userValue);
		element.setCreateTime();

		// Put to Cache
		cache.put(element);

		return true;
	}

	private Cache getCacheInstance() throws CacheException, ObjectExistsException {
		if (this.cache == null) {
			CacheManager cacheManager = CacheManager.getInstance();
			if (!cacheManager.cacheExists(CACHE_NAME)) {
				createCache(cacheManager);
			}

			this.cache = cacheManager.getCache(CACHE_NAME);
		}

		return cache;
	}

	private void createCache(CacheManager cacheManager) throws ObjectExistsException, CacheException {
		// Create New Cache
		Cache newCache = new Cache(CACHE_NAME, MAXIMUM_NUMBER_OF_ELEMENT_BEFORE_EVICTION, NOT_ALLOW_OVERFLOW_TO_DISK,
				NOT_ETERNAL, timeToLive, timeToIdle);

		// Add to Cache Manager
		cacheManager.addCache(newCache);
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	private Element getElement(String userId, Cache cache) throws CacheException {
		// Sleep until real value is available
		Element element = cache.get(userId);
		return element;
	}

	private boolean isElementValid(Element element) {
		if (element == null) {
			Logger.warn(LOG_CACHED_DATA_NOT_EXIST);
			return false;
		}

		return true;
	}
}