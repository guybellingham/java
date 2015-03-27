package com.gus.jama.supermarket;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an instance of a Supermarket store. 
 * A Supermarket might have a unique id or name, an address, 
 * timezone, number of checkouts ...etc
 * @author Gus
 *
 */
public class Supermarket {
	
	String name;  //unique name
	
	/**
	 * List of Discounts to apply after {@link #checkout(String)}
	 * TODO Use a fancy MultiMap keyed on Product - and only apply 
	 * the discounts for products that are in the checkout String. 
	 * Product is an enum so we could use  Product.B == Map.key 
	 * instead of .equals() calls.  
	 */
	List<Discount> productDiscounts = new ArrayList<Discount>();
	
	public Supermarket(String name) {
		setName(name);
		//Load up the product discounts for this Supermarket!  
		loadDiscounts(name);
	
	}
	
	private void loadDiscounts(String name) {
		//Discounts could be supermarket specific?
		//B at least 5 times - onetime discount a 100 bucks ... could be read from a file!
		productDiscounts.add(new Discount("(.*B.*){5,}",100.00));
	}
	/**
	 * Sums the price of each Product. 
	 * Then applies any Discounts that qualify. 
	 * @param items
	 * @return total due amount (which should be a double not an int)
	 */
	public double checkout(String items) {
		double totalDue = 0.0D;
		char[] productCodes = items.toCharArray();
		for (int i = 0; i < productCodes.length; i++) {
			char productCode = productCodes[i];
			Product product = Product.valueOf(Character.toString(productCode));
			totalDue += product.price;
		}
		totalDue = applyDiscounts(totalDue,items);
		return totalDue;
	}
	
	/**
	 * Built in types are passed <i>by value</i> - so we have to return 
	 * the <i>discounted</i> totalDue.
	 * @param total
	 * @return discounted totalDue
	 */
	private double applyDiscounts(double total,String items) {
		for (Discount discount : productDiscounts) {
			if(items.matches(discount.getRegex())) {
				total -= discount.amount;
			}
		}
		return total;
	}

	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	List<Discount> getProductDiscounts() {
		return productDiscounts;
	}

	void setProductDiscounts(List<Discount> productDiscounts) {
		this.productDiscounts = productDiscounts;
	}
}
