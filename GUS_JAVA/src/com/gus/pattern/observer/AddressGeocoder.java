package com.gus.pattern.observer;

import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.gus.exception.GeoCodingException;
import com.gus.pattern.serviceprovider.AddressGeocoding;
import com.gus.pattern.serviceprovider.AddressGeocodingService;
/**
 * An 'Observer' of {@link Address} that sends it to a geocoder on update.
 * @author Guy
 */
public class AddressGeocoder implements Observer {

	private static final Logger logger = LogManager.getLogger(AddressGeocoder.class);
	private static final AddressGeocoding geocoderService = AddressGeocodingService.getInstance();
	
	public AddressGeocoder() {
	}

	@Override
	public void update(Observable observable, Object arg) {
		if(observable instanceof Address) {
			Address address = (Address)observable;
			if(address.isNeedsGeocoding() || address.getLatitude()==null) {
				if (logger.isDebugEnabled()) {
					logger.debug("sending address="+address+" for geocoding!");
				}
				try {
					geocoderService.geocode(address);
				} catch (GeoCodingException e) {
					logger.error("FAILED to geocode address="+address+"!", e);
				}
			}
		}

	}

}
