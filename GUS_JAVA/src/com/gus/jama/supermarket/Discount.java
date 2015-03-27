package com.gus.jama.supermarket;

import java.io.Serializable;
/**
 * Simple bean to contain: 
 * <li>A regex pattern to look for in the checkout String
 * <li>A discount amount to apply if the pattern matches the checkout String 
 * @author Gus
 *
 */
public class Discount implements Serializable{

	private static final long serialVersionUID = 1L;
	String regex; 
	double amount;
	
	Discount(String regexPattern,double amount) {
		setRegex(regexPattern);
		setAmount(amount);
	}

	public double getAmount() {
		return amount;
	}

	void setAmount(double amount) {
		this.amount = amount;
	}

	public String getRegex() {
		return regex;
	}

	void setRegex(String regex) {
		this.regex = regex;
	}
}
