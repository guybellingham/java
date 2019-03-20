package com.gus.aws.sqs;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
/**
 * Represents a generic {@linkplain DataEvent} for a business object. 
 * <li>This impl uses a HashMap to record the property names and values of the data change. 
 *  
 * @author guybe
 *
 */
public class AbstractDataEvent implements DataEvent {
	private long customerId;
	private long userId;
	
	private Date time = Date.from(Instant.now());     		//TTLocalDate causes infinite recursion in Jackson on "startOfYear"!
	private DataEventType type;
	
	private long entityId;
	private long entityTypeId;
	/**
	 * Keep It Simple. 
	 * TODO Could be a map of Field/Property info --> field value
	 */
	private Map<String, Object> properties;
	
	public AbstractDataEvent(long customerId, long userId, DataEventType type, long entityId, long entityTypeId, Map<String, Object> properties) {
		this.customerId = customerId; 
		this.userId = userId;
		this.type = type;
		this.entityId = entityId;
		this.entityTypeId = entityTypeId;
		this.properties = properties;
	}
	
	/* (non-Javadoc)
	 * @see com.innotas.aws.sqs.DataEvent#getId()
	 */
	@Override
	public String getId() {
		return customerId+"_"+entityId;
	}
	
	/* (non-Javadoc)
	 * @see com.innotas.aws.sqs.DataEvent#getCustomerId()
	 */
	@Override
	public long getCustomerId() {
		return customerId;
	}

	void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	/* (non-Javadoc)
	 * @see com.innotas.aws.sqs.DataEvent#getUserId()
	 */
	@Override
	public long getUserId() {
		return userId;
	}

	void setUserId(long userId) {
		this.userId = userId;
	}

	/* (non-Javadoc)
	 * @see com.innotas.aws.sqs.DataEvent#getTime()
	 */
	@Override
	public Date getTime() {
		return time;
	}
	
	void setTime(Date time) {
		this.time = time;
	}

	/* (non-Javadoc)
	 * @see com.innotas.aws.sqs.DataEvent#getType()
	 */
	@Override
	public DataEventType getType() {
		return type;
	}

	void setType(DataEventType type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see com.innotas.aws.sqs.DataEvent#getEntityId()
	 */
	@Override
	public long getEntityId() {
		return entityId;
	}

	void setEntityId(long entityId) {
		this.entityId = entityId;
	}

	/* (non-Javadoc)
	 * @see com.innotas.aws.sqs.DataEvent#getEntityTypeId()
	 */
	@Override
	public long getEntityTypeId() {
		return entityTypeId;
	}

	void setEntityTypeId(long entityTypeId) {
		this.entityTypeId = entityTypeId;
	}
	
	public Map<String, Object> getProperties() {
		return properties;
	}

	void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	
}
