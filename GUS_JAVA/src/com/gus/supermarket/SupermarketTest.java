package com.gus.supermarket;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class SupermarketTest {
	
	private static Supermarket supermarket;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		supermarket = new Supermarket("Freddys Hollywood NE Portland OR");
	}
	/**
	 * checkout string qualifies for the Product.B discount!
	 */
	@Test
	public void testCheckoutWithDiscount() {
		double totalDue = supermarket.checkout("BABACBBAB");
		assertEquals(240.00, totalDue, 0.00);
	}
	/**
	 * checkout string does not qualify for the Product.B discount!
	 */
	@Test
	public void testCheckoutWithoutDiscount() {
		double totalDue = supermarket.checkout("ABBACBBAC");
		assertEquals(320.00, totalDue, 0.00);
	}
	/**
	 * Product.B discount is only applied once and not twice! 
	 */
	@Test
	public void testCheckoutWithDiscountOnlyOnce() {
		double totalDue = supermarket.checkout("BABACBBABBABBACBB");
		assertEquals(560.00, totalDue, 0.00);
	}
}
