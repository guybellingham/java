package com.gus.pattern.serviceprovider;

import com.gus.exception.LatLongTimezoneException;
import com.gus.pattern.observer.Address;
import com.gus.pattern.serviceprovider.spi.GoogleLatLongTimezoner;

public final class LatLongTimezoneService implements LatLongTimezone {

	/**
	 * Call {@link #getInstance()} method!
	 */
	private LatLongTimezoneService() {
		
	}
	private static LatLongTimezoneService service; 
	
	public static synchronized LatLongTimezoneService getInstance() {
		if(null==service){
			service = new LatLongTimezoneService();
		}
		return service;
	}

	@Override
	public void getTimezone(Address address) throws LatLongTimezoneException {
		LatLongTimezone timeZoner = new GoogleLatLongTimezoner();
		timeZoner.getTimezone(address);
	}
	
	

}