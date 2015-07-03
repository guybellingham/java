package com.gus.pattern.serviceprovider;

import com.gus.exception.GeoCodingException;
import com.gus.pattern.observer.Address;
import com.gus.pattern.serviceprovider.spi.GoogleAddressGeocoder;
/**
 * Threadsafe service that can geocode Address objects. 
 * @author Guy
 *
 */
public final class AddressGeocodingService implements AddressGeocoding {

	/**
	 * Call {@link #getInstance()} method!
	 */
	private AddressGeocodingService() {
	}
	
	/**
	 * The singleton instance.
	 */
	private static AddressGeocodingService service; 
	
	public static synchronized AddressGeocodingService getInstance() {
		if(null==service){
			service = new AddressGeocodingService();
		}
		return service;
	}
	
	@Override
	public void geocode(Address address) throws GeoCodingException {

		AddressGeocoding geocodingService = new GoogleAddressGeocoder();
		geocodingService.geocode(address);
		
	}	

}