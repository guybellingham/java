package com.gus.annotation;

public enum ColumnType {
	String(java.lang.String.class),
	Long(java.lang.Long.class),
	Boolean(java.lang.Boolean.class),
	Integer(java.lang.Integer.class);

	Class clazz; 
	
	ColumnType(Class clazz){
		setType(clazz);
	}

	public Class getType() {
		return clazz;
	}

	void setType(Class clazz) {
		this.clazz = clazz;
	}

	public static ColumnType getColumnTypeForPrimitive(Class prim){
		switch(prim.toString()){
			case "Integer":
			case "int":
				return ColumnType.Integer;
			case "String": return ColumnType.String;
			case "long": return ColumnType.Long;
			case "boolean": return ColumnType.Boolean;
			default: return ColumnType.String;
		}
	}
}
