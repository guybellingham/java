package com.gus.streams;

public enum ProjectStatus {
	INVESTIGATION,
	FEASIBILITY,
	APPROVAL,
	INPROGRESS,
	ONHOLD,
	TERMINATED; 
	
	public boolean isInPlanning() {
		if(this==ProjectStatus.INVESTIGATION || this==ProjectStatus.FEASIBILITY || this==ProjectStatus.APPROVAL) {
			return true;
		}
		return false;
	}
	public boolean isInProgress() {
		if(this==ProjectStatus.INPROGRESS) {
			return true;
		}
		return false;
	}
	public boolean isClosed() {
		if(this==ProjectStatus.ONHOLD || this==ProjectStatus.TERMINATED) {
			return true;
		}
		return false;
	}
}
