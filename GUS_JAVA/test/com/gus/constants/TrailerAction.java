package com.gus.constants;

public enum TrailerAction {
	TCON("Trailer Close"),
	DISP("Dispatch"),
	ARIV("Arrive"),
	UNLD("Unload");
	
	private String description; 
	
	TrailerAction(String description){
		setDescription(description);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
