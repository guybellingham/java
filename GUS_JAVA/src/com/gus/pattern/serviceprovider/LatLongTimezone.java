package com.gus.pattern.serviceprovider;

import com.gus.exception.LatLongTimezoneException;
import com.gus.pattern.observer.Address;

public interface LatLongTimezone {

	public void getTimezone(Address address) throws LatLongTimezoneException;
	
}
