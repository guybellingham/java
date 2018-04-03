package com.gus.regex;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FindReplaceQuestionMarks {


	public static final Pattern REGEX_QUESTION_MARK = Pattern.compile(Pattern.quote("?")); 
	

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReplaceQuestionMarks() {
		String[] array = {"One","Two","Three","123"};
		List<String> values = new ArrayList<>(Arrays.asList(array));
		String theString = "UPDATE tablename SET COL1=?, COL2=?, COL3=? WHERE id = ?";
		
		Matcher matcher = REGEX_QUESTION_MARK.matcher(theString);
    	StringBuffer sb = new StringBuffer();
    	for (Iterator<String> iterator = values.iterator(); iterator.hasNext();) {
			String value = iterator.next();
			if(matcher.find()) {
				matcher.appendReplacement(sb, value);
			}
		}
    	matcher.appendTail(sb);
    	assertEquals("UPDATE tablename SET COL1=One, COL2=Two, COL3=Three WHERE id = 123", sb.toString());
	}

}
