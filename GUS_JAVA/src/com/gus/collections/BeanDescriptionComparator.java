package com.gus.collections;

import java.util.Comparator;

public class BeanDescriptionComparator implements Comparator<ComparableBean> {
	/**
	 * Compares its two arguments for order. Returns a negative integer, zero, or a positive integer 
	 * as the first argument is less than, equal to, or greater than the second.
	 */
	@Override
	public int compare(ComparableBean o1, ComparableBean o2) {
		return o1.getDescription().compareToIgnoreCase(o2.getDescription());
	}

}
