package com.gus.collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ComparableBeanTest {

	private static final ComparableBean bean1 = new ComparableBean(1,"First");
	private static final ComparableBean bean2 = new ComparableBean(2,"Second");
	private static final ComparableBean bean3 = new ComparableBean(3,"Third");
	private static final ComparableBean bean4 = new ComparableBean(4,"Fourth");
	private static final ComparableBean bean5 = new ComparableBean(5,"First");
	
	private static final BeanDescriptionComparator descriptionComparator = new BeanDescriptionComparator();
			
	private static final ComparableBean[] beanArray = {bean1, bean2, bean3, bean4};
	
	private static final List<ComparableBean> beanList = Arrays.asList(bean1, bean2, bean3, bean4);
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testCompareToEquals() {
		//beans have the same description	
		assertTrue(descriptionComparator.compare(bean1, bean5) == 0);
	}
	@Test
	public void testCompareToLessThan() {
		assertTrue(descriptionComparator.compare(bean1, bean3) < 0);
	}
	@Test
	public void testCompareToGreaterThan() {
		assertTrue(descriptionComparator.compare(bean3, bean1) > 0);
	}
	@Test
	public void testEqualsObject() {
		assertTrue(bean1.equals(bean1));
	}
	@Test
	public void testNotEqualsObject() {
		assertFalse(bean1.equals(bean2));
	}
	@Test
	public void testSortedSet() {
		//Uses natural ordering with no Comparator
		TreeSet<ComparableBean> treeSet = new TreeSet<ComparableBean>(beanList);  
		assertTrue(bean1.equals(treeSet.first()));
		assertTrue(bean4.equals(treeSet.last()));
	}
	@Test
	public void testSortedSetComparator() {
		//Uses natural ordering with no Comparator
		TreeSet<ComparableBean> treeSet = new TreeSet<ComparableBean>(descriptionComparator);  
		treeSet.addAll(beanList);
		assertTrue(bean1.equals(treeSet.first()));
		assertTrue(bean3.equals(treeSet.last()));
	}
}
