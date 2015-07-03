package com.gus.pattern.serviceprovider.spi;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.code.geocoder.AdvancedGeoCoder;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderAddressComponent;
import com.google.code.geocoder.model.GeocoderGeometry;
import com.google.code.geocoder.model.GeocoderLocationType;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.GeocoderStatus;
import com.google.code.geocoder.model.LatLng;
import com.gus.exception.GeoCodingException;
import com.gus.pattern.observer.Address;
import com.gus.pattern.serviceprovider.AddressGeocoding;

public class GoogleAddressGeocoder implements AddressGeocoding {

	private static final Logger logger = LogManager.getLogger(GoogleAddressGeocoder.class);
	
	public GoogleAddressGeocoder() {		
	}
	
	/* (non-Javadoc)
	 * @see com.gus.pattern.serviceprovider.spi.AddressGeocodingService#geocode(java.lang.String)
	 */
	@Override
	public void geocode(Address address) throws GeoCodingException {
//		final Geocoder geocoder = new Geocoder("clientId","clientKey");  'Premier' API
//		final Geocoder geocoder = new Geocoder();						'No Timeout' 
		
		HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		httpClient.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 30 * 1000); //30s
		Geocoder geocoder = new AdvancedGeoCoder(httpClient);
		if(logger.isDebugEnabled()) {
			logger.debug("geocode("+address+") starting ...");
		}
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(address.toString()).setLanguage("en").getGeocoderRequest();
		try {
			GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
			GeocoderStatus status = geocoderResponse.getStatus();
			if(status == GeocoderStatus.OK) {
				if(logger.isDebugEnabled()) {
					logger.debug("geocode("+address+") status is OK!");
				}
				List<GeocoderResult> results = geocoderResponse.getResults();
				for (Iterator<GeocoderResult> iterator = results.iterator(); iterator.hasNext();) {
					GeocoderResult geocoderResult = (GeocoderResult) iterator.next();
					boolean partialMatch = geocoderResult.isPartialMatch();
					address.setPartialGeocoderMatch(partialMatch);
					GeocoderGeometry geometry = geocoderResult.getGeometry();
					GeocoderLocationType type = geometry.getLocationType();
					if(logger.isDebugEnabled()) {
						logger.debug("geocode("+address+") result partialMatch="+partialMatch+" geometry="+geometry+ " type="+type);
					}
					LatLng latLng = geometry.getLocation();
					BigDecimal latitude = latLng.getLat();
					BigDecimal longitude = latLng.getLng();
					address.setLatitude(latitude);
					address.setLongitude(longitude);
					if(logger.isDebugEnabled()) {
						logger.debug("geocode("+address+") setting latitude="+latitude.toPlainString()+" longitude="+longitude.toPlainString());
					}
					if(!partialMatch) {
						List<GeocoderAddressComponent> components = geocoderResult.getAddressComponents();
						for (Iterator<GeocoderAddressComponent> iterator2 = components.iterator(); iterator2.hasNext();) {
							GeocoderAddressComponent geocoderAddressComponent = (GeocoderAddressComponent) iterator2.next();
							String shortName = geocoderAddressComponent.getShortName();
							String longName = geocoderAddressComponent.getLongName();
							List<String> types = geocoderAddressComponent.getTypes();
						}
					}
				}
			} else {
				throw new GeoCodingException("FAILED to geocode address="+address+" status="+status);
			}
		} catch (IOException e) {
			logger.error("FAILED to geocode("+address+")!",e);
			throw new GeoCodingException("FAILED to connect to google "+Geocoder.getGeocoderHost()+" geocoding service!",e);
		}
		
	}
}
