package com.gus.annotation.processor;

import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.gus.PrimaryKey;
/**
 * See {@link GlobalDataService}. This is the implementation to use when your 
 * 'global' immutable data 'lives' in an Enum. Normally you won't care about or 
 * interact with this class directly. Instead you should do the following to get a service implementation: 
 * <pre>
 * GlobalDataService service = GlobalDataService.of(CustomerClass.class);
 * </pre>  
 * @author guybe
 * @param <E> the Enum class containing all the constants
 */
public class GlobalDataServiceEnumImpl<E extends GlobalImmutableData<E>> implements GlobalDataService<E> {

	/**
	 * The type/class of GlobalImmutableData returned by this service implementation.
	 */
	Class<E> clazz;
	
	/**
	 * private map of <code>id</code> --> <code>E</code> (currently an Enum constant)
	 */
	private Map<PrimaryKey,E> immutableData = new HashMap<>(); 
	
	GlobalDataServiceEnumImpl(Class<E> clazz, E[] data) {
		this.clazz = clazz;
		for (E globalImmutableData : data) {
			immutableData.put(globalImmutableData.getPrimaryKey(), globalImmutableData);
		}
	}

	@Override
	public E forKey(PrimaryKey key) {
		return immutableData.get(key);
	}

	@Override
	public List<E> getAll() {
		return immutableData.values().stream().collect(toList());
	}

	@Override
	public List<E> getAll(Comparator<E> comparator) {
		return immutableData.values().stream().sorted(comparator).collect(toList());
	}

	@Override
	public List<E> getAll(Predicate<E> predicate) {
		return immutableData.values().stream().filter(predicate).collect(toList());
	}

	@Override
	public Class<E> getGlobalImmutableDataClass() {
		return clazz;
	}
}
