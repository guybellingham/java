package com.gus.lambda;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LambdaTests {
	
	private Object[] objArray;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testComparatorLambdaSort() {
		List<String> heroes = Arrays.asList("Clark Kent","Bruce Wayne","Jimmy Olsen","Steven Strange","Otto Octavius","Peter Parker","Harry Osborn","Tony Stark","Ben Grimm");
		//sort takes a Collection and an Anonymous class implementing Comparator<String>
		Collections.sort(heroes, new Comparator<String>() {
		    @Override
		    public int compare(String name1, String name2) {
		    	String lastName1 = name1.substring(name1.lastIndexOf(" ")+1);
		    	String lastName2 = name2.substring(name2.lastIndexOf(" ")+1);
		        return lastName1.compareTo(lastName2);
		    }
		});
		System.out.print("Sort heroes by lastName=");
		heroes.forEach(n -> System.out.print(n+",")); 
		//sort using a Lambda expression
		Collections.sort(heroes, (String name1, String name2) -> {
		    return name2.compareTo(name1);
		});
		System.out.println("");
		System.out.print("Sort heroes by firstName descending=");
		heroes.forEach(n -> System.out.print(n+",")); 
		//IF the expression is a single statement, you don't need the braces and return keyword!
		//AND the Compiler already 'knows' the types in the List
		Collections.sort(heroes, (name1, name2) -> name1.compareTo(name2));
		System.out.println("");
		System.out.print("Sort heroes by firstName ascending=");
		heroes.forEach(n -> System.out.print(n+","));
		System.out.println("");
	}
	/**
	 * java.util.Comparator is an example of a built-in @FunctionalInterface.  
	 * <li>It describes exactly ONE abstract method for a type of Lambda expression.
	 * <li>You can write your own lambda interface:
	 * <li>You can add <code>default</code> methods but Lambdas can't access them 
	 */
	
	@FunctionalInterface
	interface Converter<F, T> {
	    T convert(F from);
	}
	/**
	 * <li>Assigns various Lambdas to the Converter functional interface and exercises them.
	 * <li>Lambdas have access to <code>final</code> local variables 
	 * <li>Lambdas have read and write access to instance and static variables 
	 * of the containing class (like anonymous objects). 
	 */
	@Test
	public void testConverterFunctionalInterface() {
		Converter<String, Integer> converter1 = (from) -> Integer.valueOf(from);
		Integer converted = converter1.convert("123");
		System.out.println("Converted \"123\" to "+converted);
		
		//a lambda has access to local (effectively final) variables
		//the value of the local variable can't be changed
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		Converter<Date, String> dateConverter = (date) -> formatter.format(date);
		Date today = new Date();
		String formatted = dateConverter.convert(today);
		System.out.println("Converted java.util.Date "+today+" to \""+formatted+"\"");
		
		//a functional interface -> to a static method
		Converter<Object[],String> converter2 = (objArray) -> convertArrayToCSVString(objArray);
		Object[] beanArray = { new LabelValue(), null, new LabelValue("PI",3.14159D), new LabelValue("id",7251963L) };
		String csv = converter2.convert(beanArray);
		System.out.println("Converted LabelValue array "+beanArray+" to String \""+csv+"\"");
		
		//a functional interface -> Arrays.asList()
		Converter<Object[],List> converter3 = (objArray) -> Arrays.asList(objArray);
		List labelValueList = converter3.convert(beanArray);
		System.out.println("Converted LabelValue array "+beanArray+" to List "+labelValueList+"");
		
		//lambda using an instance variable
		Converter<List<LabelValue>,Object[]> converter4 = (list) -> {
			int size = list.size();
			List<Object> values = new ArrayList<>(size);
			Iterator<LabelValue> itr = list.iterator();
			while (itr.hasNext()) {
				LabelValue lv = itr.next();  		//may be null
				if(lv != null) {
					values.add(lv.value);
				}
			}
			objArray = new Object[values.size()];
			return values.toArray(objArray);
		};
		converter4.convert(labelValueList);
		System.out.println("Converted List "+labelValueList+" to objArray "+Arrays.toString(objArray));
	}
	/**
	 * Predicates are methods or Lambdas (functions) that take one argument and return a boolean value. 
	 * The interface contains various default methods for composing predicates into 
	 * complex logical terms (<code>and</code>, <code>or</code>, <code>negate</code>).
	 */
	@Test
	public void testPredicates() { 
		String nullText = null;
		Predicate<String> isEmpty = String::isEmpty;     						//:: is a method reference
		System.out.println("isEmpty(\"\") is "+isEmpty.test(""));   			//true
		try {
			System.out.println("isEmpty(null) is "+isEmpty.test(null));   	
		} catch(NullPointerException npe) {
			System.out.println("isEmpty(null) throws "+npe);
		}
		System.out.println("isEmpty(\" \") is "+isEmpty.test(" "));   			//false
		Predicate<String> isNotEmpty = isEmpty.negate();
		System.out.println("isNotEmpty(\"\") is "+isNotEmpty.test(""));   		//false
		Predicate<String> isNullOrEmpty = (text) -> text == null || text.length() == 0;
		System.out.println("isNullOrEmpty(\"\") is "+isNullOrEmpty.test(""));   			//true
		System.out.println("isNullOrEmpty(null) is "+isNullOrEmpty.test(nullText));   		//true
		System.out.println("isNullOrEmpty(\"abc\") is "+isNullOrEmpty.test("abc"));   		//false
	}
	
	public static class LabelValue {
		private String label;
		private Object value;
		/** default empty constructor */
		public LabelValue() {
		}
		/** construct with the given lbl and val */
		public LabelValue(String lbl, Object val) {
			this.label = lbl;
			this.value = val;
		}
		public String toString() {
			return "{\"label\":\""+this.label+"\",\"value\":"+value+"}";
		}
	}
	
	public static String convertArrayToCSVString(Object[] objects) {
		StringBuilder sb = new StringBuilder(objects.length * 8);
		sb.append("array:[");
		for (int i = 0; i < objects.length; i++) {
			Object object = objects[i];
			if(i > 0) {  sb.append(", ");  }
			sb.append(String.valueOf(object));
		}
		sb.append("]");
		return sb.toString();
	}
}
