package com.gus.annotation.processor;

import com.gus.PrimaryKey;
import com.gus.PrimaryKeyLong;
import com.gus.PrimaryKeyLongLong;


/**
 * Interface implemented by all global immutable data (aka internal or seed data). 
 * <li>They all have a unique {@link PrimaryKey}. 
 * You can either compare <code>id</code>s or the instances with the equals <code>==</code> binary operation. 
 * <li>They all have some kind of title or value to display to the end user.
 * <li>The actual implementation may be an enumeration or an immutable java bean.
 * @author guybe
 * @see GlobalDataService
 * @see PrimaryKey
 */
public interface GlobalImmutableData<E> {
	
	/**
	 * All global enumerations must have a unique {@link PrimaryKey}  
	 * with which other entities may reference them (within the database).
	 * @return the UNIQUE {@link PrimaryKey} implementation that identifies  
	 * this instance of global immutable data.
	 */
	public PrimaryKey getPrimaryKey(); 
	/**
	 * All global enumerations  must have a human readable display text.
	 * @return the title or value of this data as a (I18N??) string
	 */
	public String getDisplayText();
	
	/**
	 * Helper method to get an instance of GlobalImmutableData 
	 * identified by the given <code>id</code> of the given <code>clazz</code>.
	 * @param id - the long numeric that identifies the global data constant
	 * @param clazz - enum or bean class that implements GlobalImmutableData
	 * @return the global data constant (of the given <code>clazz</code>) with the given <code>id</code>.
	 * @throws IllegalArgumentException if the given <code>id</code> does not match any of the constants.
	 */
	static <T extends GlobalImmutableData<T>> T forId(long id, Class<T> clazz) {
		return forId(GlobalImmutableData.getPrimaryKey(id), clazz);
	}
	/**
	 * Helper method to get an instance of GlobalImmutableData 
	 * identified by the given <code>key</code> of the given <code>clazz</code>.
	 * @param key - the {@link PrimaryKey} that identifies the global data constant
	 * @param clazz - enum or bean class that implements GlobalImmutableData
	 * @return the global data constant (of the given <code>clazz</code>) with the given <code>key</code>.
	 * @throws IllegalArgumentException if the given <code>key</code> does not match any of the constants.
	 */
	static <T extends GlobalImmutableData<T>> T forId(PrimaryKey key, Class<T> clazz) {
		GlobalDataService globalDataService = GlobalImmutableData.getGlobalDataService(clazz);
    	GlobalImmutableData globalData = globalDataService.forKey(key);
    	if(globalData != null) {
    		return (T)globalData;
    	} else {
        	throw new IllegalArgumentException("No global data constant exists of type="+clazz.getName()+" with key="+key+"?");
        }
	}
	/**
	 * Helper method to get an instance of GlobalDataService for the given 
	 * <code>clazz</code> which must implement GlobalImmutableData.
	 * @param clazz - enum or bean class that implements GlobalImmutableData
	 * @return the GlobalDataService Impl for the given <code>clazz</code>
	 */
	static GlobalDataService getGlobalDataService(Class<? extends GlobalImmutableData<?>> clazz) {
		GlobalDataService globalDataService = GlobalDataService.of(clazz);
		return globalDataService;
	}
	/**
	 * Helper method to build a {@link PrimaryKey} for a given 
	 * <code>id</code>
	 * @param id - long integer
	 * @return a {@link PrimaryKeyLong}
	 */
	static PrimaryKey getPrimaryKey(long id) {
		return new PrimaryKeyLong(id);
	}
	/**
	 * Helper method to build a composite {@link PrimaryKey} for a given 
	 * <code>id</code> and <code>subId</code>.
	 * @param id - long integer
	 * @param subId - long integer
	 * @return a {@link PrimaryKeyLongLong}
	 */
	static PrimaryKey getPrimaryKey(long id, long subId) {
		return new PrimaryKeyLongLong(id, subId);
	}
}
