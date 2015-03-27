package com.gus.deduplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;

import com.google.gson.Gson;

public class LeadsDeDup {
	
	static final String DEFAULT_FILE_PATH = "C:\\data\\leads.json";
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		
		String filePath = DEFAULT_FILE_PATH;
		if(args.length>0){
			filePath = args[0].trim();
		}
		File inputFile = new File(filePath);
		FileReader fileReader = null; 
		FileWriter fileWriter = null; 
		Gson gson = new Gson();
		
		try {
			fileReader = new FileReader(inputFile);
			LeadArray leadArray = gson.fromJson(fileReader, LeadArray.class);
			assert(null!=leadArray);
			//TODO Or just handle an empty file and return a System exit>0
			assert(0<leadArray.getLength());
			
			LinkedHashMap<String, Lead> idMap = new LinkedHashMap<String, Lead>(leadArray.getLength()); 
			for (Lead lead : leadArray) {
				Lead duplicateLead = idMap.put(lead.get_id(), lead);
				if(null!=duplicateLead){
					//compare their entry dates!
					int rc = lead.compareTo(duplicateLead);
					if(rc==-1) {
						System.out.println("Found Leads with duplicate id="+lead.get_id()+"!");
						System.out.println("Replaced Lead with entryDate="+lead.getEntryDate());
						System.out.println(lead);
						idMap.put(duplicateLead.get_id(), duplicateLead);
					} else {
						//This 'lead' is the newest one (entryDate is greater than) or the entry dates are equal we're good
						System.out.println("\nFound Leads with duplicate id="+duplicateLead.get_id()+"!");
						System.out.println("\nReplaced DuplicateLead with entryDate="+duplicateLead.getEntryDate());
						System.out.println(duplicateLead);
					}
				}
				
			}
			Collection<Lead> outputLeads = idMap.values();
			LinkedHashMap<String, Lead> emailMap = new LinkedHashMap<String, Lead>(leadArray.getLength()); 
			//...repeat using email address as the hash key 
			for (Lead lead : outputLeads) {
				Lead duplicateLead = emailMap.put(lead.getEmail(), lead);
				if(null!=duplicateLead){
					//compare their entry dates!
					int rc = lead.compareTo(duplicateLead);
					if(rc==-1) {
						System.out.println("Found Leads with duplicate email="+lead.getEmail()+"!");
						System.out.println("Replaced Lead with entryDate="+lead.getEntryDate());
						System.out.println(lead);
						emailMap.put(duplicateLead.getEmail(), duplicateLead);
					} else {
						//This 'lead' is the newest one (entryDate is greater than) or the entry dates are equal we're good
						System.out.println("\nFound Leads with duplicate email="+duplicateLead.getEmail()+"!");
						System.out.println("\nReplaced DuplicateLead with entryDate="+duplicateLead.getEntryDate());
						System.out.println(duplicateLead);
					}
				}
				
			}
			
			LeadArray outputLeadArray = new LeadArray(outputLeads.toArray(new Lead[outputLeads.size()])); 
			fileWriter = new FileWriter("C:\\data\\leads_dedup.json");
			fileWriter.write(outputLeadArray.toString());    //no need to use Gson here
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			// cleanup
			if(null!=fileReader) { 
				try {
					fileReader.close();
				} catch (IOException e) {
					//ignore
				} 
			}
			if(null!=fileWriter) { 
				try {
					fileWriter.close();
				} catch (IOException e) {
					//ignore
				}
			}
		}
	}

	
}
