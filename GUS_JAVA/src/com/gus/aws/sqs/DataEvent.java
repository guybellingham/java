package com.gus.aws.sqs;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;
/**
 * Interface for a 'data event'. Can be one of the {@linkplain DataEventType}s. 
 * <li>Always involves data belonging to a customer.
 * <li>Always involves a 'user' who either made the change or is sending this 'event'. 
 * <li>Always has a date and time (to the millisecond) at which it occurred.
 * <li>Must be one of the {@linkplain DataEventType}s.
 * <li>Always involves an entity of a particular type. 
 * @author guybe
 *
 */
public interface DataEvent {
	
	static final DateTimeFormatter ISO_DATE_FORMATTER = ISODateTimeFormat.basicDateTime();
	/**
	 * For deduplication all events must return a unique String <code>id</code>. 
	 * @return a unique String used for identification and deduplication
	 */
	String getId();

	long getCustomerId();

	long getUserId();

	Date getTime();
	
	default String getISODateTime() {
		if (getTime() != null) {
			return ISO_DATE_FORMATTER.print(getTime().getTime());
		}
		return "";
	}
	
	DataEventType getType();

	long getEntityId();

	long getEntityTypeId();
}