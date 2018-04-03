package com.gus.datetime;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DateTimeFormatTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testParseShortEnglishDate() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.ENGLISH);
		LocalDate date = dateFormatter.parse("02/28/18", LocalDate::from);  		// MM/dd/yy is US English
		assertNotNull(date);
		assertEquals(28, date.getDayOfMonth());
		assertEquals(2, date.getMonth());
		assertEquals(2018, date.getYear());
	}

	@Test
	public void testParseISODateTime() {
		DateTimeFormatter isoDateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
		LocalDateTime dateTime = isoDateTimeFormatter.parse("2018-02-28T15:45:10-08:00", LocalDateTime::from);
		assertNotNull(dateTime);
		assertEquals(28, dateTime.getDayOfMonth());
		assertEquals(Month.FEBRUARY, dateTime.getMonth());
		assertEquals(2018, dateTime.getYear());
		assertEquals(15, dateTime.getHour());
		assertEquals(45, dateTime.getMinute());
		assertEquals(10, dateTime.getSecond());
	}
}
