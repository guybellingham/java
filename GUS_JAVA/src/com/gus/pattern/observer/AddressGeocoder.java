package com.gus.pattern.observer;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gus.exception.GeoCodingException;
import com.gus.pattern.serviceprovider.AddressGeocoding;
import com.gus.pattern.serviceprovider.AddressGeocodingService;
/**
 * An 'Observer' of {@link Address} that sends it to a geocoder on update.
 * @author Guy
 */
public class AddressGeocoder implements Observer {

	private static final Logger logger = Logger.getLogger("com.gus.pattern");
	private static final AddressGeocoding geocoderService = AddressGeocodingService.getInstance();
	
	public AddressGeocoder() {
	}

	@Override
	public void update(Observable observable, Object arg) {
		if(observable instanceof Address) {
			Address address = (Address)observable;
			if(address.isNeedsGeocoding() || address.getLatitude()==null) {
				
				logger.log(Level.FINEST, "sending address="+address+" for geocoding!");
				
				try {
					geocoderService.geocode(address);
				} catch (GeoCodingException e) {
					logger.log(Level.SEVERE, "FAILED to geocode address="+address+"!", e);
				}
			}
		}

	}

}
