package com.gus.util;

import java.util.EnumSet;
import java.util.Iterator;

import com.gus.util.LabelValueBean;
/**
 * Collection of static utility methods for enum manipulation and conversion.
 * @author bellinghamsmith.guy
 * @see EnumUtilsTest
 */
public class EnumUtils {

	/**
	 * Generic method takes in an enum Class and iterates through all its enums.
	 * <li>It maps the <code>name</code> of the enum to the <code>value</code> of each bean.
	 * <li>It maps the <code>toString</code> value of the enum to the <code>label</code> of each bean. 
	 * @param clazz
	 * @return Array LabelValueBean
	 */
	public static <E extends Enum<E>> LabelValueBean[] enumToLabelValueBeans(Class<E> clazz){
		EnumSet<E> enums = EnumSet.allOf(clazz);
		LabelValueBean[] lvbs = new LabelValueBean[enums.size()];
		Iterator<E> itor = enums.iterator();
		for (int i = 0; i < enums.size(); i++) {
			E en = itor.next();
			LabelValueBean lvb = new LabelValueBean(en.name(),en.toString());
			lvbs[i] = lvb;
		}
		return lvbs;
	}
}
