package com.nextlabs.attributeprovider.cache;

import java.util.List;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.ObjectExistsException;

public interface ILocalCacheManager {

	/**
	 * Loads User Attributes from Cache.
	 * 
	 * @param userId
	 *            User ID of the related User subject.
	 * @return The attributes list of the related User subject, Null if relevant
	 *         data is not available in cache or has expired.
	 * @throws CacheException
	 * @throws ObjectExistsException
	 */
	public List<String> getFromCache(String userId) throws CacheException, ObjectExistsException;

	/**
	 * Saves User Attributes to Cache.
	 * 
	 * @param userId
	 *            User ID of the related User subject.
	 * @param userValue
	 *            The attributes map of the related User subject. This value
	 *            should not be Null.
	 * @return Boolean variable stating the success of the inserting the data.
	 *         True if data is successfully inserted and False if otherwise.
	 * @throws ObjectExistsException
	 * @throws CacheException
	 */
	public boolean putToCache(String userId, List<String> userValue) throws ObjectExistsException, CacheException;
}
