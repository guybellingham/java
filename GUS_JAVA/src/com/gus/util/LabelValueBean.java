package com.gus.util;

import java.io.Serializable;

public class LabelValueBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String label;
	private String value;
	
	public LabelValueBean(){
		
	}
	public LabelValueBean(String label,String value){
		
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
