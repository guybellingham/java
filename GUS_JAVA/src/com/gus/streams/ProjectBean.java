package com.gus.streams;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
/**
 * Simple bean for defining a 'project' that someone can play a role on.
 * @author Guy
 *
 */
public class ProjectBean implements Serializable, Comparable<ProjectBean> {

	private static final long serialVersionUID = 1L;

	/**
	 * A unique id for this type of Role.
	 */
	private long projectId;
	
	private double budget;
	
	/**
	 * A project <i>always</i> has a name and description.
	 */
	private String name, description;
	
	/**
	 * Whether this project is in planning mode, in-progress, ended ..etc.
	 */
	private ProjectStatus status;
	/**
	 * An allocation has a start and projected end date.
	 */
	private Date startDate, endDate;
	
	private DateFormat sdf = DateFormat.getDateInstance(DateFormat.SHORT);
	private NumberFormat nmf = NumberFormat.getCurrencyInstance();
	
	public ProjectBean(long id, double budget, String name, String desc, ProjectStatus status, Date startDate, Date endDate) {
		setProjectId(id);
		setBudget(budget);
		setName(name);
		setDescription(desc);
		setStatus(status);
		setStartDate(startDate);
		setEndDate(endDate);
	}

	public String toString() {
		return "{ \"projectId\":"+projectId+
				", \"name\":"+name+
				", \"budget\":"+nmf.format(budget)+	
				", \"status\":"+status+
				", \"startDate\":"+sdf.format(startDate)+
				" }";
	}
	
	public int hashCode() {
		return (int)projectId;
	}
	public boolean equals(Object other) {
		boolean rc = false; 
		if(other instanceof ProjectBean) {
			ProjectBean otherAllocation = (ProjectBean)other;
			if(projectId == otherAllocation.getProjectId()) {
				rc = true;
			}
		}
		return rc;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long roleId) {
		this.projectId = roleId;
	}
	public double getBudget() {
		return budget;
	}

	public void setBudget(double budget) {
		this.budget = budget;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isInplanning() {
		return status.isInPlanning();
	}
	public boolean isClosed() {
		return status.isClosed();
	}
	
	public ProjectStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectStatus status) {
		this.status = status;
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
	/**
	 * Natural ordering is by projectId.
	 */
	@Override
	public int compareTo(ProjectBean other) {		
		return (int) (getProjectId() - other.getProjectId());
	}

}
