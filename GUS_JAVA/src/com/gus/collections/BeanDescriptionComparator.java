package com.gus.collections;

import java.util.Comparator;

public class BeanDescriptionComparator implements Comparator<ComparableBean> {
	/**
	 * Compares its two arguments for order. Returns a negative integer, zero, or a positive integer 
	 * as the first argument is less than, equal to, or greater than the second.
	 * <li>Sorts <code>description</code> in 'reverse' (descending) order
	 */
	@Override
	public int compare(ComparableBean o1, ComparableBean o2) {
		return o2.getDescription().compareToIgnoreCase(o1.getDescription());
	}

}
