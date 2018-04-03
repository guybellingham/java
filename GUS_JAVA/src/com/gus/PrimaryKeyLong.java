package com.gus;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple implementation of a primary key that has 
 * a single long integer value.
 */
public class PrimaryKeyLong implements PrimaryKey, Comparable<PrimaryKeyLong> {

	private static final long serialVersionUID = 1L;
	private long id;
	/**
	 * Construct a new simple key from 1 long integer.
	 * @param id - the primary id value
	 */
	public PrimaryKeyLong(long id) {
		this.id = id;
	}
	/**
	 * @return a List containing the <code>id</code> value of this key.
	 */
	@Override
	public List getValues() {
		List values = new ArrayList();
		values.add(id);
		return values;
	}
	/**
	 * Two PrimaryKeyLongs are equal if they have the 
	 * same <code>id</code> value. 
	 */
	@Override
	public boolean equals(Object other) {
		if(other instanceof PrimaryKeyLong) {
			PrimaryKeyLong otherPrimaryKey = (PrimaryKeyLong)other;
			if(otherPrimaryKey == this) {
				return true;
			}
			if(otherPrimaryKey.getId() == this.getId()) {
				return true;
			}
		}
		return false;
	}
	/**
	 * @return the hash code integer (may be negative) that 
	 * represents this keys value.
	 */
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}
	@Override
	public String toString() {
		return "{id:"+id+"}";
	}

	@Override
	public int compareTo(PrimaryKeyLong other) {
		if(other == null) {
			return 1;
		}
		if(this == other) {
			return 0;
		}
		if(this.getId() > other.getId()) {
			return 1;
		}
		if(this.getId() < other.getId()) {
			return -1;
		}
		return 0;
	}

	public long getId() {
		return id;
	}

	protected void setId(long id) {
		this.id = id;
	}
}
