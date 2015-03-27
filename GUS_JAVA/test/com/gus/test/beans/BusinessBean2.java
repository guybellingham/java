package com.gus.test.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.TimeZone;

public class BusinessBean2 implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "BusinessBean2";
	public static final Integer[] TWOS = {new Integer(2),new Integer(4),new Integer(8),new Integer(16),new Integer(32)};

	TimeZone timezone = TimeZone.getTimeZone("America/Chicago");
	
	Calendar calendar = Calendar.getInstance();
	
	BigDecimal bigDecimal = new BigDecimal(9024190210.90211);
	
	String string = "Hi, I am BusinessBean2!";
	
	boolean flag = false;
	
	int wholeNumber = 4096;
	
	double decimalNumber = 2.13567E+14;
	
	String[] stringArray = {"Saturday","Sunday"};
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
	public BusinessBean2(TimeZone timezone,
			Calendar calendar,
			BigDecimal bigDecimal,
			String string,
			boolean flag,
			int wholeNumber,
			double decimalNumber,
			String[] stringArray){
		setTimezone(timezone);
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
	public BusinessBean2(){
		
	}
	public TimeZone getTimezone() {
		return timezone;
	}

	public void setTimezone(TimeZone timezone) {
		this.timezone = timezone;
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

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public int getWholeNumber() {
		return wholeNumber;
	}

	public void setWholeNumber(int wholeNumber) {
		this.wholeNumber = wholeNumber;
	}

	public double getDecimalNumber() {
		return decimalNumber;
	}

	public void setDecimalNumber(double decimalNumber) {
		this.decimalNumber = decimalNumber;
	}

	public String[] getStringArray() {
		return stringArray;
	}

	public void setStringArray(String[] stringArray) {
		this.stringArray = stringArray;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("BusinessBean2: {");
		sb.append(" timezone:");
		sb.append(getTimezone().getDisplayName());
		sb.append(",calendar:");
		sb.append(getCalendar());
		sb.append(",bigDecimal:");
		sb.append(getBigDecimal().toPlainString());
		sb.append(",flag:");
		sb.append(isFlag());
		sb.append(",string:");
		sb.append(getString());
		sb.append(",stringArray:");
		sb.append(getStringArray());
		return sb.toString();
	}
}
