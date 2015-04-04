package com.gus.pattern.serviceprovider;

import com.gus.exception.GeoCodingException;
import com.gus.pattern.observer.Address;

public interface AddressGeocoding {

	public abstract void geocode(Address address) throws GeoCodingException;

}