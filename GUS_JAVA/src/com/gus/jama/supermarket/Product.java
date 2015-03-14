package com.gus.jama.supermarket;

/**
 * Enumeration of the types of product we sell in our {@link Supermarket}.
 * Use EnumSet if you want to do Collection operations on Products. 
 * @author Gus
 */
public enum Product {
	A("Product A",20.00),
	B("Product B",50.00),
	C("Product C",30.00);

	String description;
	double price;
	
	Product(String description,double price){
		setDescription(description);
		setPrice(price);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
