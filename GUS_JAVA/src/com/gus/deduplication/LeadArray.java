package com.gus.deduplication;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

public class LeadArray implements Serializable, Iterable<Lead> {
	
	private static final long serialVersionUID = 1L;
	private Lead[] leads;
	/**
	 * No args constructor.
	 */
	public LeadArray() {
		
	}
	/**
	 * Full constructor.
	 */
	public LeadArray(Lead[] leadArray) {
		setLeads(leadArray);
	}
	public Lead getLead(int index){
		//IndexOutOfBounds is a programming error!
		return getLeads()[index];
	}
	public Lead[] getLeads() {
		return leads;
	}
	public void setLead(Lead theLead,int index) {
		getLeads()[index]=theLead;
	}
	public void setLeads(Lead[] leads) {
		this.leads = leads;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("{\"leads\":[").append("\n");
		for (int i = 0; i < leads.length; i++) {
			if(i == 0){
				sb.append(leads[i].toString());
			} else {
				sb.append(",\n").append(leads[i].toString());
			}
		}
		sb.append("\n").append("] }");
		return sb.toString();
	}
	
	public int getLength() {
		if(null!=getLeads()){
			return getLeads().length;
		}
		return 0;
	}
	@Override
	public Iterator<Lead> iterator() {
		return Arrays.asList(leads).iterator();
	}
}
