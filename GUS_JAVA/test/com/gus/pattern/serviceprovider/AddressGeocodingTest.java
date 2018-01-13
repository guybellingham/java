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

public class AddressGeocodingTest {
	
	private static final Logger logger = LogManager.getLogger(AddressGeocodingTest.class);
	
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
	public void testGeocodeAddress() {		
		AddressGeocoding addressGeocoding = AddressGeocodingService.getInstance();
		
		try {
			addressGeocoding.geocode(MY_ADDRESS);
			logger.debug("Geocoding OK Address="+MY_ADDRESS); 
			assertNotNull("No Latitude?",MY_ADDRESS.getLatitude());
			assertNotNull("No Longitude?",MY_ADDRESS.getLongitude());
			assertEquals(MY_LATITIUDE, MY_ADDRESS.getLatitude());
			assertEquals(MY_LONGITIUDE, MY_ADDRESS.getLongitude());
			assertTrue(MY_ADDRESS.isPartialGeocoderMatch()); 
		} catch (Exception e) {
			logger.error(e);
			fail("FAILED Geocoding of Address="+MY_ADDRESS);
		}
		
	}

}
