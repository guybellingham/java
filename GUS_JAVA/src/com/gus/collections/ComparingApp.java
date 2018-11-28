package com.gus.collections;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ComparingApp {

	private static ComparableBean[] ARRAY = {
			new ComparableBean(1, "January"),
			new ComparableBean(2, "February"),
			new ComparableBean(3, "March"),
			new ComparableBean(4, "April")
		};
	private static List<ComparableBean> MONTHS =  Arrays.asList(ARRAY);
	
	public static void main(String[] args) {
		System.out.println("Sorting months by their names ...");
		Collections.sort(MONTHS, Comparator.comparing((ComparableBean bean) -> bean.getDescription()));
		MONTHS.forEach(System.out::println);
		
		System.out.println("Sorting months by their natural order (id) ...");
		Collections.sort(MONTHS);
		MONTHS.forEach(System.out::println);
		
		System.out.println("Sorting months using BeanDescriptionComparator ...");
		Collections.sort(MONTHS, new BeanDescriptionComparator());
		MONTHS.forEach(System.out::println);
		
	}

}
