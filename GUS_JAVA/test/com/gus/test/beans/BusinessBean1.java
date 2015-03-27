package com.gus.test.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BusinessBean1 implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Log logger = LogFactory.getLog(BusinessBean1.class);
	
	public static final String NAME = "BusinessBean1";
	public static final Integer[] PRIMES = {new Integer(1),new Integer(3),new Integer(5),new Integer(7),new Integer(11)};
	public static DateFormat DATE_FOMATTER = new SimpleDateFormat("yyyyMMddHHmmss");
	
	TimeZone timezone = TimeZone.getTimeZone("America/Los Angeles");
	
	Calendar calendar = Calendar.getInstance();
	
	BigDecimal bigDecimal = new BigDecimal(3.14159);
	
	String string = "Hello I am BusinessBean1!";
	
	boolean flag = true;
	
	int wholeNumber = 2048;
	
	double decimalNumber = 2.13567E+4;
	
	String[] stringArray = {"Monday","Tuesday","Wednesday","Thursday","Friday"};
	
	List<BusinessBean2> beanList = new ArrayList<BusinessBean2>();
	
	public BusinessBean1(){
		
	}
	/**
	 * Full constructor.
	 */
	public BusinessBean1(TimeZone timezone,
			Calendar calendar,
			BigDecimal bigDecimal,
			String string,
			boolean flag, 
			int wholeNumber,
			double decimalNumber,
			String[] stringArray,
			List<BusinessBean2> beanList
			){
		setTimezone(timezone);
		setCalendar(calendar);
		setBigDecimal(bigDecimal);
		setString(string);
		setFlag(flag);
		setWholeNumber(wholeNumber);
		setDecimalNumber(decimalNumber);
		setStringArray(stringArray);
		setBeanList(beanList);
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
	public String getDateTimeAsString(){
		String result = "";
		if(null!=calendar){
			result=DATE_FOMATTER.format(calendar.getTime());
		}
		return result;
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

	public List<BusinessBean2> getBeanList() {
		return beanList;
	}

	public void setBeanList(List<BusinessBean2> beanList) {
		this.beanList = beanList;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("BusinessBean1: {");
		sb.append(" timezone:");
		sb.append(getTimezone().getDisplayName());
		sb.append(",calendar:");
		sb.append(getDateTimeAsString());
		sb.append(",bigDecimal:");
		sb.append(getBigDecimal().toPlainString());
		sb.append(",beanList:");
		sb.append(getBeanList());
		sb.append(",flag:");
		sb.append(isFlag());
		sb.append(",string:");
		sb.append(getString());
		sb.append(",stringArray:");
		sb.append(getStringArray());
		return sb.toString();
	}
}
