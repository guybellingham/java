package com.gus.enumeration;

import java.util.EnumSet;
import java.util.Set;
/**
 * Interface implemented by all 'internal' (immutable) enumerations. 
 * <li>They all have a unique integer <code>id</code>.
 * <li>They all have some kind of <code>title</code> or value.
 * <li>When comparing enums compare them by their built-in <code>name</code> with the equals <code>==</code> binary op.
 * @author guybe
 *
 * @param <E> the enumeration type that implements this interface.
 */
public interface ImmutableGlobalData<E extends Enum<E>> {
	/**
	 * All internal enums must have a unique <code>id</code> (a database constraint) 
	 * with which other entities (stored in the database) may reference them.
	 * @return the unique id number of an enum
	 */
	public int getId(); 
	/**
	 * All internal enums must have a <code>title</code> (or value) 
	 * of some kind.
	 * @return the title or value of this enum
	 */
	public String getTitle();
	/**
	 * @param id unique integer that identifies an internal enum
	 * @return the Internal enum with the given <code>id</code>.
	 */
	public E forId(int id);
	/**
	 * @param title (or value) that identifies zero or more internal enums. 
	 * @return the Set of internal enum/s (zero or more) with the given <code>title</code> (or value).
	 */
	public Set<E> forTitle(String title);
	/**
	 * @return the entire set of enums
	 */
	public EnumSet<E> getEnumSet();
}
