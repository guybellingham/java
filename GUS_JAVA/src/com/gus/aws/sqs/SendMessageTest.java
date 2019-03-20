package com.gus.aws.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SendMessageTest extends AbstractSQSTest {
	
	static final long customerId = 1714159722;
	static final long userId = 1949230461;
	static final String Q_GROUP_ID = "1714159722";
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		
		AmazonSQS sqs = getAmazonSQS();
		
		Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
		messageAttributes.put("Version", new MessageAttributeValue()
		  .withStringValue("1")
		  .withDataType("Number"));    		//"Binary", "Number" or "String"
		
		Map<String, Object> properties = new HashMap<>();
		properties.put("description", "This is my › new Projœct description.");  	//field id 405 String
		properties.put("LLPhaseId", 207113L); 										// 407 Long
		properties.put("targetDate", new Date(2019,03,15)); 						// Date
		properties.put("arrayOfStrings", new String[] {"id","title","sequence"}); 	// Array
		properties.put("nothing", null); 											// null
		properties.put("isActive", true); 											// boolean
		long projectId = 1949478201; 
		AbstractDataEvent event = new AbstractDataEvent(customerId, userId, DataEventType.update, projectId, 4, properties); 
		
		try {
			//Simple Jackson serializer
//			ObjectMapper mapper = new ObjectMapper(); 
//			String jsonResult = mapper.writeValueAsString(event);
			//
			SimpleModule module = new SimpleModule();
			module.addSerializer(new DataEventSerializer(AbstractDataEvent.class));
			ObjectMapper mapper = new ObjectMapper();
			String jsonResult = mapper.registerModule(module)
//			  .writer(new DefaultPrettyPrinter())
			  .writeValueAsString(event);
			
			logger.info( "Sending message:"+ jsonResult);
			SendMessageRequest sendMessageFifoQueue = new SendMessageRequest()
					  .withQueueUrl(Q_URL)
					  .withMessageBody(jsonResult)
					  .withMessageGroupId(Q_GROUP_ID)
					  .withMessageAttributes(messageAttributes);
			
			SendMessageResult result = sqs.sendMessage(sendMessageFifoQueue); 
			
	        logger.info( "Message sent id=" + result.getMessageId() + " sequence#=" + result.getSequenceNumber() ); 
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			sqs.shutdown();
		}
		
	}
	
	@Test
	public void testQAttributes() {
		logger.info( "QAttributes:" );
		Map<String,String> qAttributes = getQueueAttributes();
		// "FifoQueue=true" , "ApproximateNumberOfMessages=2", "ApproximateNumberOfMessagesNotVisible=0", 
		// "QueueArn=arn:aws:sqs:us-west-2:252218482207:r2-devvm-gus.fifo"
	    if (qAttributes != null) {
	    	for(Map.Entry<String, String> attribute : qAttributes.entrySet()) {
	       		logger.info(attribute.getKey() + "=" + attribute.getValue()); 
	       	}
	       	
	    }
	}
}
