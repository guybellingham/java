package com.gus;

import java.io.Serializable;
import java.util.List;
/**
 * An interface that describes what a Primary Key implementation must 
 * do in order to be useful.
 * <li>PrimaryKeys must override the <code>public boolean equals(Object other)</code> method.
 * <li>PrimaryKeys must provide hash keys by overriding <code>public int hashCode()</code>.
 * <li>Use the <code>PrimaryKey.of(myKey, newValue)</code> method to 
 * add a <code>newValue</code> to an existing primary key and return 
 * a new (composite) primary key.  
 * <li>It's good practice for PrimaryKeys to be {@link Comparable} and 
 * for them to specify their 'natural' ordering in a 
 * <code>public int compareTo(PrimaryKey other)</code> method.
 * @see PrimaryKeyTest
 * @see PrimaryKeyLong
 * @see PrimaryKeyLongLong
 * @author guybe
 */
public interface PrimaryKey extends Serializable {
	/**
	 * @return every PrimaryKey must provide hash keys.
	 */
	public int hashCode();
	/**
	 * @return every PrimaryKey must provide a way to get 
	 * an ordered List of its values.
	 */
	public List getValues();
	/**
	 * @param other - the other PrimaryKey to test for equality
	 * @return every PrimaryKey must provide an equals method 
	 */
	public boolean equals(Object other);
	
	/**
	 * Given a <code>newValue</code> to add to an existing primary key (or <code>null</code>)
	 * it will return a new 'promoted'  PrimaryKey implementation.
	 * @param currentPrimaryKey - the existing primary key or <code>null</code> if 
	 * you are creating a new primary key this way.
	 * @param newValue - the value (usually a Long) to 'add' to the key
	 * @return a new {@link PrimaryKey} based off the given currentPrimaryKey 
	 * and the given <code>newValue</code> to add to the key. 
	 */
	public static PrimaryKey of(PrimaryKey currentPrimaryKey, Object newValue) {
		if(newValue instanceof Long) {
			long keyValue = (Long)newValue;
			if(currentPrimaryKey == null) {
				return new PrimaryKeyLong(keyValue);
			}
			else if(currentPrimaryKey instanceof PrimaryKeyLong) {
				PrimaryKeyLong currentPrimaryKeyLong = (PrimaryKeyLong)currentPrimaryKey;
				return new PrimaryKeyLongLong(currentPrimaryKeyLong.getId(), keyValue);
			} 
		}
		//TODO else - unsupported primary key error!
		throw new IllegalArgumentException("Unsupported newValue for PrimaryKey.of("+currentPrimaryKey+","+newValue+")");
	}
}
