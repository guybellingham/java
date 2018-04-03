package com.gus;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of a primary key that combines  
 * two long integer values to make a unique key value.
 * @see PrimaryKeyTest
 */
public class PrimaryKeyLongLong implements PrimaryKey, Comparable<PrimaryKeyLongLong> {

	private static final long serialVersionUID = 1L;
	private long id;
	private long subId;
	/**
	 * Construct as new composite key from 2 long integers.
	 * @param id - primary id value
	 * @param subId - secondary id value
	 */
	public PrimaryKeyLongLong(long id, long subId) {
		this.id = id;
		this.subId = subId;
	}
	/**
	 * @return a List containing the <code>id</code> followed by 
	 * the <code>subId</code>.
	 */
	@Override
	public List getValues() {
		List values = new ArrayList();
		values.add(id);
		values.add(subId);
		return values;
	}
	/**
	 * @return the hash code integer (may be negative) that 
	 * represents this keys values.
	 */
	@Override
	public int hashCode() {
		int result = 1;
		result = (31 * result) + Long.hashCode(id);
		result = (31 * result) + Long.hashCode(subId);
		return result;
	}

	/**
	 * Two PrimaryKeyLongLongs are equal if they have the 
	 * same <code>id</code> and <code>subId</code> values. 
	 */
	@Override
	public boolean equals(Object other) {
		if(other instanceof PrimaryKeyLongLong) {
			PrimaryKeyLongLong otherPrimaryKey = (PrimaryKeyLongLong)other;
			if(otherPrimaryKey == this) {
				return true;
			}
			if(otherPrimaryKey.getId() == this.getId() && otherPrimaryKey.getSubId() == this.getSubId()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "{id:"+id+", subId:"+subId+"}";
	}

	@Override
	public int compareTo(PrimaryKeyLongLong other) {
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
		if(this.getSubId() > other.getSubId()) {
			return 1;
		}
		if(this.getSubId() < other.getSubId()) {
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

	public long getSubId() {
		return subId;
	}

	protected void setSubId(long subId) {
		this.subId = subId;
	}
}
