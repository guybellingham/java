package com.gus.collections;

public class ComparableBean implements Comparable<ComparableBean>{
	private int id;   //primary key
	private String description;
	
	public ComparableBean(){
		
	}
	public ComparableBean(int id, String description){
		setId(id);
		setDescription(description);
	}
	/**
	 *  Returns a negative integer, zero, or a positive integer 
	 *  as this object is less than, equal to, or greater than the other object.
	 */
	@Override
	public int compareTo(ComparableBean other) {
		// compare the 'natural keys'
		return id - other.id;
	}
	@Override
	public boolean equals(Object other){
		if(other instanceof ComparableBean){
			ComparableBean otherBean = (ComparableBean)other;
			return id == otherBean.id;
		}
		return false;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String toString() {
		return new StringBuilder("{")
				.append("id:").append(getId())
				.append(", desc:\"").append(getDescription()).append("\"")
				.append("}").toString();
	}
}
