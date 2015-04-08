package com.gus.pattern.serviceprovider;

import static org.junit.Assert.*; 
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gus.pattern.observer.Address;

public class LatLongTimezoneTest {
	
	private static final Logger logger = LogManager.getLogger(LatLongTimezoneTest.class);
	
	public static final Address MY_ADDRESS = new Address("2615 NE 42nd Ave",null,"Portland","OR","USA","97216");

	private static final BigDecimal MY_LATITIUDE = new BigDecimal(45.541551);
	private static final BigDecimal MY_LONGITIUDE = new BigDecimal(-122.620234);
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		org.apache.log4j.BasicConfigurator.configure();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testAddressTimezoner() {		
		LatLongTimezone addressTimezoner = LatLongTimezoneService.getInstance();
		
		try {
			MY_ADDRESS.setLatitude(MY_LATITIUDE);
			MY_ADDRESS.setLongitude(MY_LONGITIUDE);
			
			addressTimezoner.getTimezone(MY_ADDRESS);
			assertNotNull("No TimeZone?",MY_ADDRESS.getTimeZone());
			logger.debug("Timezoner OK Address="+MY_ADDRESS+" TimeZone="+MY_ADDRESS.getTimeZone().getDisplayName()); 
			
		} catch (Exception e) {
			logger.error(e);
			fail("FAILED to TimeZone Address="+MY_ADDRESS+" tuple="+MY_ADDRESS.getLatLongTuple());
		}
		
	}

}
