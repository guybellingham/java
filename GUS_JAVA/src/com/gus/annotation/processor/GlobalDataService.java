package com.gus.annotation.processor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.gus.PrimaryKey;

/**
 * Service Interface that allows a client to obtain global immutable data (aka internal or seed data). 
 * The service allows us to separate retrieval and processing of global data from 
 * the {@link GlobalImmutableData}  (enumeration) itself and provide different implementations. 
 * <ol>
 * <li>Call the {@link #of(Class)} method with the <code>Class</code> of your {@link GlobalImmutableData}
 * to get the correct global data service implementation and instance. For example:
 * <pre>GlobalDataService.of(CustomerClass.class);</pre>
 * <li>The service provides methods to retrieve global data of different types 
 * using their unique {@link PrimaryKey}s.
 * <li>The service provides methods to retrieve lists of global data of different types 
 * sorted or filtered in different ways.
 * </ol>
 * @author guybe
 *
 * @param <E> the Enum<E> enumeration (only Enums are currently supported) that this interface returns.
 * @see GlobalDataServiceTest
 */
public interface GlobalDataService<E extends GlobalImmutableData<E>> {

	/**
	 * Map of the Class of the GlobalImmutableData --> the service implementation for that class.
	 */
	static final Map<Class<? extends GlobalImmutableData<?>>,GlobalDataService<?>> cache = new HashMap<>();
	
	/**
	 * @return the type/class of GlobalImmutableData returned by this service implementation.
	 */
	public Class<E> getGlobalImmutableDataClass();
	
	/**
	 * Provide the enumeration instance for a given unique <code>key</code>.
	 * @param key the {@link PrimaryKey} of the global data you're looking for
	 * @return the unique <code>instance</code> that has the given <code>key</code> 
	 * that you can compare with the binary <code>==</code> equals op
	 */
	public E forKey(PrimaryKey key); 
	
	/**
	 * Provide the (immutable) list of all instances 
	 * in the order in which they are defined.
	 * @return the (immutable) list of all values
	 */
	public List<E> getAll();
	/**
	 * Provide the (immutable) list of all instances 
	 * ordered using the given <code>comparator</code>.
	 * @return the (immutable) list of sorted values
	 */
	public List<E> getAll(Comparator<E> comparator);
	/**
	 * Provide the (immutable) list of all instances 
	 * filtered using the given <code>predicate</code>.
	 * @return the (immutable) list of filtered values
	 */
	public List<E> getAll(Predicate<E> predicate);
	
	/**
	 * Factory method to get or build the correct GlobalImmutableDataService for the given Class. 
	 * @param clazz - the Class of GlobalImmutableData you want a service for. 
	 * Currently ONLY Enums are supported.
	 * @return the (cached) GlobalDataService implementation for the given <code>clazz</code>
	 */
	public static GlobalDataService of(Class<? extends GlobalImmutableData<?>> clazz) {
		GlobalDataService<? extends GlobalImmutableData<?>> service = cache.get(clazz);
		if(service != null) {
			return service;
		}
		if(clazz.isEnum()) {
			GlobalImmutableData<?>[] data = clazz.getEnumConstants();
			if(data != null) {
				service = new GlobalDataServiceEnumImpl(clazz, data);
				cache.put(clazz, service);
			}
		} else {
			throw new IllegalArgumentException("of("+clazz+") only Enums are currently supported");
		}
		return service;
	}
}
