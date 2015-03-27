package com.gus.test.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.TimeZone;

public class BusinessBean2DTO implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "BusinessBean2";

//	TimeZone timezone; 
	
	Calendar calendar;
	
	BigDecimal bigDecimal;
	
	String string;
	
	Boolean flag;
	
	Integer wholeNumber;
	
	Double decimalNumber;
	
	String[] stringArray;
	
	/**
	 * Full constructor
	 * @param timezone
	 * @param calendar
	 * @param bigDecimal
	 * @param string
	 * @param flag
	 * @param wholeNumber
	 * @param decimalNumber
	 * @param stringArray
	 */
	public BusinessBean2DTO(TimeZone timezone,
			Calendar calendar,
			BigDecimal bigDecimal,
			String string,
			boolean flag,
			int wholeNumber,
			double decimalNumber,
			String[] stringArray){
		setCalendar(calendar);
		setBigDecimal(bigDecimal);
		setString(string);
		setFlag(flag);
		setWholeNumber(wholeNumber);
		setDecimalNumber(decimalNumber);
		setStringArray(stringArray);
	}
	/**
	 * JavaBeans constructor.
	 */
	public BusinessBean2DTO(){
		
	}

	public Calendar getCalendar() {
		return calendar;
	}
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
	public BigDecimal getBigDecimal() {
		return bigDecimal;
	}
	public void setBigDecimal(BigDecimal bigDecimal) {
		this.bigDecimal = bigDecimal;
	}
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	public Boolean getFlag() {
		return flag;
	}
	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
	public Integer getWholeNumber() {
		return wholeNumber;
	}
	public void setWholeNumber(Integer wholeNumber) {
		this.wholeNumber = wholeNumber;
	}
	public Double getDecimalNumber() {
		return decimalNumber;
	}
	public void setDecimalNumber(Double decimalNumber) {
		this.decimalNumber = decimalNumber;
	}
	public String[] getStringArray() {
		return stringArray;
	}
	public void setStringArray(String[] stringArray) {
		this.stringArray = stringArray;
	}
	public String toString(){
		StringBuilder sb = new StringBuilder("BusinessBean2DTO: {");
		sb.append(" calendar:");
		sb.append(getCalendar());
		sb.append(",bigDecimal:");
		sb.append(getBigDecimal().toPlainString());
		sb.append(",flag:");
		sb.append(getFlag());
		sb.append(",string:");
		sb.append(getString());
		sb.append(",stringArray:");
		sb.append(getStringArray());
		return sb.toString();
	}
}
