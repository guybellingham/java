package com.gus.enumeration;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import com.gus.annotation.ColumnMapping;
import com.gus.annotation.TableMapping;


/**
 * Enumeration of internal permission types.
 * <li><code>id</code> is the unique id numnber (integer) for each enum.
 * <li><code>title</code> describes the type of permission.  
 * 
 * @author Guy
 */
@TableMapping(name="T_INTERNAL_PERMISSION_TYPE")
public enum InternalPermissionType implements ImmutableGlobalData<InternalPermissionType> {
	VIEW_TAB(1,"View Tab"),
	VIEW(2,"View"),
	CREATE(3,"Create"),
	EDIT(4,"Edit"),
	DELETE(5,"Delete"),
	PROPOSE(6,"Propose"),
	PROCESS_REQUESTS(7,"Process Requests"),
	STAFF_DIRECTLY(8,"Staff Directly"), 
	DELETE_9(9,"Delete"),
	DETAILS_20(20,"Details"),
	ATTACHMENTS_46(46,"Attachments")
	;
	@ColumnMapping(name="PERMISSION_TYPE_ID")
	private int id;
	
	@ColumnMapping(name="TITLE")
	private String title;
	
	InternalPermissionType(int id, String title) {
		setId(id);
		setTitle(title);
	}
	
	@Override
	public InternalPermissionType forId(int id) {
		for (InternalPermissionType item : values()) {
			if(id == item.getId()) {
				return item;
			}
		} 
		return null;
	}

	@Override
	public Set<InternalPermissionType> forTitle(String title) {
		Set<InternalPermissionType> items = new HashSet<>();
		for (InternalPermissionType item : values()) {
			if(title.equals(item.getTitle())) {
				items.add(item);
			}
		} 
		return items;
	}
	@Override
	public EnumSet<InternalPermissionType> getEnumSet() { 
		return EnumSet.allOf(InternalPermissionType.class);
	}
	
	public int getId() {
		return id;
	}

	void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	
	void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("id:").append(getId());
		sb.append(",title:").append(getTitle());
		sb.append("}");
		return sb.toString();
	}

}
