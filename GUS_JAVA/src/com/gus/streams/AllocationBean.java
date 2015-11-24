package com.gus.streams;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Simple bean for filtering
 * @author Guy
 *
 */
public class AllocationBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat MMDDYYYY = new SimpleDateFormat("MM/dd/yyyy");
	
	private long allocationId; 
	
	/**
	 * An allocation <i>always</i> involves a role.
	 */
	private RoleBean role; 
	/**
	 * An allocation <i>always</i> involves a project.
	 */
	private ProjectBean project;
	/**
	 * Sometimes there is a resource assigned.
	 */
	private ResourceBean resource;
	/**
	 * An allocation always has a start and end date.
	 */
	private Date startDate, endDate;
	
	public AllocationBean(long id, ProjectBean project, RoleBean role, ResourceBean resource, Date startDate, Date endDate){
		setAllocationId(id);
		setProject(project);
		setRole(role);
		setResource(resource);
		setStartDate(startDate);
		setEndDate(endDate);
	}

	public String toString() {
		return "{ \"allocationId\":"+allocationId+
				", \"role\":"+role+
				", \"project\":"+project+
				", \"resource\":"+resource+
				", \"startDate\":"+MMDDYYYY.format(startDate)+
				", \"endDate\":"+MMDDYYYY.format(endDate)+
				" }";
	}
	
	public int hashCode() {
		return (int)(role.getRoleId() * 7) + startDate.hashCode() + endDate.hashCode() + 37;
	}
	public boolean equals(Object other) {
		boolean rc = false; 
		if(other instanceof AllocationBean) {
			AllocationBean otherAllocation = (AllocationBean)other;
			if(project.equals(otherAllocation.getProject()) && 
			   role.equals(otherAllocation.getRole()) &&  
			   resource.equals(otherAllocation.getResource()) &&
			   startDate.equals(otherAllocation.getStartDate()) &&
			   endDate.equals(otherAllocation.getEndDate()) ) {
				rc = true;
			}
		}
		return rc;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public RoleBean getRole() {
		return role;
	}

	public void setRole(RoleBean role) {
		this.role = role;
	}

	public ProjectBean getProject() {
		return project;
	}

	public void setProject(ProjectBean project) {
		this.project = project;
	}

	public ResourceBean getResource() {
		return resource;
	}

	public void setResource(ResourceBean resource) {
		this.resource = resource;
	}

	public long getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(long allocationId) {
		this.allocationId = allocationId;
	}
}
