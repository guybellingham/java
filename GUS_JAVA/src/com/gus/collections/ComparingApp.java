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
			new ComparableBean(4, "April"),
			new ComparableBean(5, "May"),
			new ComparableBean(6, "June"),
			new ComparableBean(7, "July"),
		};
	private static List<ComparableBean> MONTHS =  Arrays.asList(ARRAY);
	
	public static void main(String[] args) {
		System.out.println("Sorting months by their names using Comparator.comparing(Function)...");
		Collections.sort(MONTHS, Comparator.comparing(ComparableBean::getDescription));
		MONTHS.forEach(System.out::println);
		
		System.out.println("Sorting months by their natural order (by id) in a stream().sorted() ...");
		MONTHS.stream().sorted().forEach(System.out::println);
		
		System.out.println("Sorting months using BeanDescriptionComparator ...");
		MONTHS.stream().sorted(new BeanDescriptionComparator()).forEach(System.out::println);
		
	}

}
