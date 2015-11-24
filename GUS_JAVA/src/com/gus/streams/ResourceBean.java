package com.gus.streams;

import java.io.Serializable;
/**
 * Simple bean for defining a 'resource' that plays a role on a project.
 * @author Guy
 *
 */
public class ResourceBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * A unique id for this type of person.
	 */
	private long resourceId;
	/**
	 * A person <i>always</i> has a firstName and lastName.
	 */
	private String firstName, lastName;
	/**
	 * Whether this person is available to work on projects, terminated ..etc.
	 */
	private boolean available, terminated;
	
	public ResourceBean(long id, String firstName, String lastName, boolean available, boolean terminated) {
		setResourceId(id);
		setFirstName(firstName);
		setLastName(lastName);
		setAvailable(available);
		setTerminated(terminated);
	}

	public String toString() {
		return "{ \"resourceId\":"+resourceId+
				", \"firstName\":"+firstName+
				", \"lastName\":"+lastName+
				", \"available\":"+available+
				", \"terminated\":"+terminated+
				" }";
	}
	
	public int hashCode() {
		return (int)resourceId;
	}
	public boolean equals(Object other) {
		boolean rc = false; 
		if(other instanceof ResourceBean) {
			ResourceBean otherAllocation = (ResourceBean)other;
			if(resourceId == otherAllocation.getResourceId()) {
				rc = true;
			}
		}
		return rc;
	}

	public long getResourceId() {
		return resourceId;
	}

	public void setResourceId(long roleId) {
		this.resourceId = roleId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean isTerminated() {
		return terminated;
	}

	public void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}
}
