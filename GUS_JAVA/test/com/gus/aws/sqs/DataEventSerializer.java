package com.gus.aws.sqs;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.Map;
/**
 * Experimental 'custom' Jackson Serializer.
 * @author guybe
 *
 */
public class DataEventSerializer extends StdSerializer<AbstractDataEvent> {
	
	static final DateTimeFormatter formatter = ISODateTimeFormat.basicDateTime();
	
    public DataEventSerializer(Class t) {
        super(t);
    }

	@Override
	public void serialize(AbstractDataEvent event, JsonGenerator generator, SerializerProvider provider) throws IOException {
		
		generator.writeStartObject();
		generator.writeNumberField("customerId", event.getCustomerId());
		generator.writeNumberField("userId", event.getUserId());
		generator.writeStringField("time", formatter.print(event.getTime().getTime()));
		generator.writeStringField("type", event.getType().name());
		generator.writeNumberField("entityId", event.getEntityId());
		generator.writeNumberField("entityTypeId", event.getEntityTypeId());
		
		generator.writeArrayFieldStart("properties");
		for(Map.Entry<String, Object> entry : event.getProperties().entrySet()) {
			generator.writeStartObject();
			Object value = entry.getValue();
			if (value == null) {
				generator.writeNullField(entry.getKey());
			} else
			if (value.getClass().isArray()) {
				generator.writeArrayFieldStart(entry.getKey());
				writeFieldArray(entry, generator);
				generator.writeEndArray();
			} else {
				writeField(entry, generator);
			}
			generator.writeEndObject();
		}
		generator.writeEndArray();
		generator.writeEndObject();
	}
	
	protected void writeField(Map.Entry<String, Object> entry, JsonGenerator generator) throws IOException {
		Object value = entry.getValue();
		if (value instanceof Long) {
			generator.writeNumberField(entry.getKey(), (Long)value);	
		} else 
		if (value instanceof String) {
			generator.writeStringField(entry.getKey(), value.toString());	
		} else
		if (value instanceof Boolean) {
			generator.writeBooleanField(entry.getKey(), (Boolean)value);
		} else
		if (value instanceof Date) {
			generator.writeStringField(entry.getKey(), formatter.print( ((Date)value).getTime()) );
		} else {
			generator.writeObjectField(entry.getKey(), value);
		}
		
	}
	
	protected void writeFieldArray(Map.Entry<String, Object> entry, JsonGenerator generator) throws IOException {
		Object array = entry.getValue();
		if (array instanceof String[]) {
			String[] stringArray = (String[]) array;
			// iterate the values
			for (int i = 0; i < stringArray.length; i++) {
				generator.writeString(stringArray[i]);
			}
		}
		
	}
}
