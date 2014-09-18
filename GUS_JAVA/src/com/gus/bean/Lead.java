package com.gus.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Lead implements Serializable, Comparable<Lead> {

	private static final long serialVersionUID = 1L;
	
	private String _id, email, firstName, lastName, address, entryDate;
	private transient Date date;   //null until you call getDate()!
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+00:00'");
	
	/**
	 * JavaBeans no arg constructor
	 */
	public Lead(){
		super();
	}
	/**
	 * Full constructor.
	 * @param id
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param address
	 * @param entryDate
	 * @throws ParseException if an invalid entryDate is given (programming error)
	 */
	public Lead(String id,String email,String firstName,String lastName,String address,String entryDate) {
		set_id(id);
		setEmail(email);
		setFirstName(firstName);
		setLastName(lastName);
		setAddress(address);
		setEntryDate(entryDate);
	}
	/**
	 * The 'natural' ordering of a Lead is by its entryDate. 
	 * @return -1 0 or +1 as this.entryDate is less than, equal or greater 
	 * than the other.entryDate String.
	 */
	@Override
	public int compareTo(Lead other) {
		assert(null!=other);
		int rc = getEntryDate().compareTo(other.getEntryDate());
		return rc;
	}
	/**
	 * Hash value used to store this bean in hashmaps and sets.
	 * @return hashCode value of _id.
	 */
	public int hashCode(){
		return get_id().hashCode();
	}
	/**
	 * 
	 * @return true if this Lead and the 'other' lead have the same _id   
	 */
	public boolean equals(Object other){
		if(this == other) { return true; }
		if(other instanceof Lead) {
			return get_id().equals(((Lead)other).get_id()) ;
		} else {
			return false;
		}
	}
	/**
	 * @return me as a JSON String
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		sb.append("{ \n");
		sb.append("\"_id\":\"").append(get_id()).append("\",\n");
		sb.append("\"email\":\"").append(getEmail()).append("\",\n");
		sb.append("\"firstName\":\"").append(getFirstName()).append("\",\n");
		sb.append("\"lastName\":\"").append(getLastName()).append("\",\n");
		sb.append("\"address\":\"").append(getAddress()).append("\",\n");
		sb.append("\"entryDate\":\"").append(getEntryDate()).append("\"\n");
		sb.append("}");
		return sb.toString();
	}
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	/**
	 * Synthetic date property derived from the entryDate String.
	 * @return Date representing the entryDate
	 * @throws ParseException
	 */
	public Date getDate()  throws ParseException {
		if(null==date) {
			this.date = sdf.parse(entryDate);
		}
		return date;
	}
	
}
