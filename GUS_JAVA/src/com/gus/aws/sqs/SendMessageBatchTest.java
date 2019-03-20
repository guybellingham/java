package com.gus.aws.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.BatchResultErrorEntry;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.SendMessageBatchResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;

public class SendMessageBatchTest extends AbstractSQSTest {
	
	static final String SQL_CUSTOMERS = "SELECT ttcustomer_id FROM arena.t_ttcustomer WHERE current_entry = 1";
	
	static final String SQL_PROJECTS = "SELECT project_id, owner_id, parent_id, parent_type_id, start_date, title, confidential, modified_by, modify_date "
			+ "FROM arena.t_project WHERE ttcustomer_id = ?";
	
	static final long MAX_LENGTH = 260000L;   //~256KB
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		
		List<Long> customerIds = getCustomerIds();
		logger.info("Processing " + customerIds.size() + " customers...");
		SimpleModule module = new SimpleModule();
		module.addSerializer(new DataEventSerializer(AbstractDataEvent.class));
		ObjectMapper mapper = new ObjectMapper();
		
		long start = System.currentTimeMillis(), stop = 0L;
		long totalSent = 0L, totalError = 0L;
		AmazonSQS sqs = getAmazonSQS();
		
		for (Long customerId : customerIds) {
			
			logger.info("Getting projects for customerId " + customerId + "...");
			List<AbstractDataEvent> projectEvents = getProjectDataEvents(customerId);
			if (projectEvents.isEmpty()) {
				continue;
			}
			
			Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
			messageAttributes.put("Customer", new MessageAttributeValue()
			  .withStringValue(String.valueOf(customerId))
			  .withDataType("Number"));    		//"Binary", "Number" or "String"
			String groupId = String.valueOf(customerId);

			ListIterator<AbstractDataEvent> eventsIter = projectEvents.listIterator();
			
			while (eventsIter.hasNext())  {
				List<SendMessageBatchRequestEntry> batchRequests = new ArrayList<>();
				long length = 0L;
				// MAX 10 at a time!  
				for (int i = 0; (eventsIter.hasNext() && i < 10); i++) {
					AbstractDataEvent event = eventsIter.next();
					try {
						String eventJson = mapper.registerModule(module)
							  .writeValueAsString(event);
						// MAX 256KB at a time!
						if ( (length += eventJson.length()) < MAX_LENGTH ) {
							SendMessageBatchRequestEntry batchRequestEntry = new SendMessageBatchRequestEntry()
								.withId(event.getId())
								.withMessageGroupId(groupId)
								.withMessageAttributes(messageAttributes)
								.withMessageBody(eventJson);
							batchRequests.add(batchRequestEntry);
							eventsIter.remove(); 
						} else {
							logger.info("Message # "+i+" with event "+event+" exceeds max_length "+MAX_LENGTH+" ending batch prematurely");
							break;
						}
					} catch (JsonProcessingException e) {
						logger.log(Level.SEVERE, "FAILED to create JSON from event "+event, e);
					}
				}
				
				try {
					if ( !batchRequests.isEmpty() ) {
						SendMessageBatchRequest batchRequest = new SendMessageBatchRequest(Q_URL, batchRequests);
						SendMessageBatchResult result = sqs.sendMessageBatch(batchRequest);
						List<BatchResultErrorEntry> errors = result.getFailed();
						if (errors != null && !errors.isEmpty()) {
							int httpStatus = result.getSdkHttpMetadata().getHttpStatusCode();
							totalError += errors.size();
							logger.log(Level.SEVERE, errors.size() +" ERRORS occurred in the message batch! HTTP status=" + httpStatus + ".");
							ListIterator<BatchResultErrorEntry> errorIter = errors.listIterator();
							while (errorIter.hasNext()) {
								//TODO retry logic to sqs.sendMessage() each failed message again somehow?
								BatchResultErrorEntry batchResultErrorEntry = errorIter.next();
								logger.log(Level.SEVERE, "FAILED message id="+batchResultErrorEntry.getId()+
										" code="+batchResultErrorEntry.getCode()+
										" message="+batchResultErrorEntry.getMessage());
							}
						} else {
							totalSent += result.getSuccessful().size();
							logger.info(result.getSuccessful().size() + " messages sent OK out of Total="+totalSent);
						}
					}
				} catch (Exception e) {
					logger.log(Level.SEVERE, "FAILED to send message batch", e);
				} 
			} //-end while
		}  //-next customer
		
		sqs.shutdown();
		
		stop = System.currentTimeMillis();
		logger.info("Time elapsed = "+ (stop - start) + " msec");
		logger.log(Level.SEVERE, "Finished with "+totalError+" total errors and "+totalSent+" messages sent.");
	}

	List<Long> getCustomerIds() {
		List<Long> customerIdList = new ArrayList<>();
		Connection conn = null; 
		PreparedStatement stmt = null; 
		ResultSet result = null;
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(SQL_CUSTOMERS);
			
			result = stmt.executeQuery();
			while(result.next()) {
				customerIdList.add(result.getLong("ttcustomer_id"));
			}
		} catch(SQLException e) {
			logger.log(Level.SEVERE, "FAILED to getCustomerIds()", e);
		} finally {
			close(result, stmt, conn);
		}
		return customerIdList;
	}
	
	List<AbstractDataEvent> getProjectDataEvents(long customerId) {
		List<AbstractDataEvent> projectEvents = new ArrayList<>();
		Connection conn = null; 
		PreparedStatement stmt = null; 
		ResultSet result = null;
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(SQL_PROJECTS);
			stmt.setLong(1, customerId);
			
			result = stmt.executeQuery();
			
			while(result.next()) {
				Long projectId = result.getLong("project_id");
				Long userId = result.getLong("modified_by");
				Map<String, Object> properties = new HashMap<>();
				properties.put("id", projectId); 	
				properties.put("owner_id", result.getLong("owner_id"));
				properties.put("parent_id", result.getLong("parent_id"));
				properties.put("parent_type_id", result.getLong("parent_type_id"));
				properties.put("modified_by", userId);
				properties.put("title", result.getString("title"));
				properties.put("start_date", result.getDate("start_date"));
				properties.put("modify_date", result.getTimestamp("modify_date"));
				properties.put("confidential", (result.getInt("confidential") > 0 ? true : false));
				AbstractDataEvent event = new AbstractDataEvent(customerId, userId, DataEventType.update, projectId, 4, properties); 
				projectEvents.add(event);
			}
		} catch(SQLException e) {
			logger.log(Level.SEVERE, "FAILED to get projects for customerId="+customerId, e);
		} finally {
			close(result, stmt, conn);
		}
		return projectEvents;
	}
	
	void close(ResultSet result, Statement stmt, Connection conn) {
		if(result != null) {
			try {
				result.close();
			} catch(SQLException ignored) {
			}
		}
		if(stmt != null) {
			try {
				stmt.close();
			} catch(SQLException ignored) {
			}
		}
		if(conn != null) {
			try {
				conn.close();
			} catch(SQLException ignored) {
			}
		}
	}
}
