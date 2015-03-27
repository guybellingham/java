package com.gus.test.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class BusinessBean1DTO implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "BusinessBean1DTO";
	
	Calendar calendar;
	
	BigDecimal bigDecimal;
	
	String string;
	
	Boolean flag;
	
	Integer wholeNumber;
	
	Double decimalNumber;
	
	String[] stringArray;
	
	List<BusinessBean2DTO> beanList;
	
	public BusinessBean1DTO(){
		
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

	public List<BusinessBean2DTO> getBeanList() {
		return beanList;
	}

	public void setBeanList(List<BusinessBean2DTO> beanList) {
		this.beanList = beanList;
	}
	public String toString(){
		StringBuilder sb = new StringBuilder("BusinessBean1DTO: {");

		sb.append(" calendar:");
		sb.append(getCalendar());
		sb.append(",bigDecimal:");
		sb.append(getBigDecimal().toPlainString());
		sb.append(",beanList:");
		sb.append(getBeanList());
		sb.append(",flag:");
		sb.append(getFlag());
		sb.append(",string:");
		sb.append(getString());
		sb.append(",stringArray:");
		sb.append(getStringArray());
		return sb.toString();
	}
}
