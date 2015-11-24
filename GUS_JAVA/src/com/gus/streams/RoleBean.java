package com.gus.streams;

import java.io.Serializable;
/**
 * Simple bean for defining a 'role' that someone can play on a project.
 * @author Guy
 *
 */
public class RoleBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * A unique id for this type of Role.
	 */
	private long roleId;
	/**
	 * A role <i>always</i> has a title and description.
	 */
	private String title, description;
	/**
	 * Whether this role is an engineering role, project role ..etc.
	 */
	private boolean engineering, project;
	
	public RoleBean(long id, String title, String desc, boolean isProjectRole, boolean isEngineeringRole) {
		setRoleId(id);
		setTitle(title);
		setDescription(desc);
		setProject(isProjectRole);
		setEngineering(isEngineeringRole);
	}

	public String toString() {
		return "{ \"roleId\":"+roleId+
				", \"title\":"+title+
				", \"description\":"+description+
				", \"engineering\":"+engineering+
				", \"project\":"+project+
				" }";
	}
	
	public int hashCode() {
		return (int)roleId;
	}
	public boolean equals(Object other) {
		boolean rc = false; 
		if(other instanceof RoleBean) {
			RoleBean otherAllocation = (RoleBean)other;
			if(roleId == otherAllocation.getRoleId()) {
				rc = true;
			}
		}
		return rc;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEngineering() {
		return engineering;
	}

	public void setEngineering(boolean engineering) {
		this.engineering = engineering;
	}

	public boolean isProject() {
		return project;
	}

	public void setProject(boolean project) {
		this.project = project;
	}

}
