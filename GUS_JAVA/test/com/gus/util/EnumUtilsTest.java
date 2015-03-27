package com.gus.util;

import org.junit.Test;

import com.gus.util.LabelValueBean;
import com.gus.constants.TrailerAction;

import junit.framework.TestCase;

public class EnumUtilsTest extends TestCase {
	@Test
	public void testConvertEnumToLVB() {
		LabelValueBean[] lvbs = EnumUtils.enumToLabelValueBeans(TrailerAction.class);
		assertNotNull(lvbs);
		assertTrue("Returned empty LabelValueBean array", lvbs.length > 0);
		assertTrue("LabelValueBean[0] has value="+lvbs[0].getValue(),lvbs[0].getValue() instanceof String);
		assertTrue("LabelValueBean[0] has label="+lvbs[0].getLabel(),lvbs[0].getLabel() instanceof String);
	}
}
