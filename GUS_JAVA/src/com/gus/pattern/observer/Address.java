package com.gus.pattern.observer;

import java.math.BigDecimal;
import java.util.Observable;
import java.util.Observer;
import java.util.TimeZone;

import com.gus.pattern.serviceprovider.AddressGeocodingService;
/**
 * An <b>immutable</b> postal Address object with (optional) geocoding and timezone 
 * information provided by {@link AddressGeocodingService} and  
 * 
 * <p>An observable object can have one or more observers. 
 * When an observable object is newly created, its set of observers is
 * empty. 
 * <p>An observer may be any object that implements interface {@link Observer}. 
 * <p>After an observable instance changes, an application calling the
 * <code>Observable</code>'s <code>notifyObservers()</code> method 
 * causes all of its observers to be notified of the change by a call
 * to their <code>update</code> method. 
 * <ul>
 * <li> <code>addObserver(Observer o)</code> :add Observer to the list of observers for this subject.
 * <li> <code>deleteObserver(Observer o)</code> :delete Observers from the list of observers .
 * <li> <code>notifyObservers()</code> : notify all the observers if object has changed.
 * <li> <code>hasChanged()</code> :return true if object has changed.
 * <li> <code>setChanged()</code> :This method marks object has changed
 * <li> <code>clearChanged()</code> :this method will indicate that subject has no changes or all the observers has been notified.
 * </ul> 
 * <p>PROs:
 * <li> Loose coupling between Subject(Observable) and Observers: 
 * The only thing the subject knows about its observers is that they implement the Observer interface. 
 * You can add or delete any observer without effecting subject.
 * <li> Support for broadcast communication: 
 * Notification about subject state change does not need to specify its receiver. 
 * This notification is broadcast to all interested objects that subscribed to it.
 * <p>CONs:
 * <li> Difficult to debug especially if the Observers fork Threads to do their work. 
 * If multiple Threads can update the subject then you have to think about locking/deadlocking...etc
 * @author Guy
 *
 */
public class Address extends Observable {

	private String address1, address2, townOrCity, stateOrRegion, countryName, postalOrZipCode;
	private BigDecimal latitude, longitude;
	private boolean needsGeocoding, partialGeocoderMatch; 
	private TimeZone timeZone;
	
	/**
	 * DO NOT USE. JavaBeans constructor for TEST.
	 */
	public Address() {
	}
	
	/**
	 * Construct a new (immutable) Address without a Lat/Long or TimeZone. 
	 * @param address1 - REQUIRED 
	 * @param address2 - OPTIONAL
	 * @param townOrCity - REQUIRED
	 * @param stateOrRegion - REQUIRED
	 * @param countryName - REQUIRED
	 * @param postalCode - OPTIONAL
	 */
	public Address(String address1,String address2,String townOrCity,String stateOrRegion,String countryName,String postalCode) {
		this(address1,address2,townOrCity,stateOrRegion,countryName,postalCode,null,null,null);
	}
	/**
	 * Construct a new (immutable) Address
	 * @param address1 - REQUIRED
	 * @param address2 - OPTIONAL
	 * @param townOrCity - REQUIRED
	 * @param stateOrRegion - REQUIRED
	 * @param countryName - REQUIRED
	 * @param postalCode - OPTIONAL
	 * @param lat - OPTIONAL
	 * @param lng - OPTIONAL
	 * @param timezone - OPTIONAL
	 */
	public Address(String address1,String address2,String townOrCity,
			String stateOrRegion,String countryName,String postalCode,
			BigDecimal lat,BigDecimal lng,TimeZone timezone) {
		assert(null!=address1 && !address1.isEmpty());
		assert(null!=townOrCity && !townOrCity.isEmpty());
		assert(null!=stateOrRegion && !stateOrRegion.isEmpty());
		assert(null!=countryName && !countryName.isEmpty());
		setAddress1(address1);
		setAddress2(address2);
		setTownOrCity(townOrCity);
		setStateOrRegion(stateOrRegion);
		setCountryName(countryName);
		setPostalOrZipCode(postalCode);
		setLatitude(lat);
		setLongitude(lng);
		setTimeZone(timezone);
	}

	public String getAddress1() {
		return address1;
	}
	protected void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	protected void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getTownOrCity() {
		return townOrCity;
	}
	protected void setTownOrCity(String townOrCity) {
		this.townOrCity = townOrCity;
	}
	public String getStateOrRegion() {
		return stateOrRegion;
	}
	protected void setStateOrRegion(String stateOrRegion) {
		this.stateOrRegion = stateOrRegion;
	}
	public String getCountryName() {
		return countryName;
	}
	protected void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getPostalOrZipCode() {
		return postalOrZipCode;
	}
	protected void setPostalOrZipCode(String postalCode) {
		this.postalOrZipCode = postalCode;
	}
	
	public TimeZone getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	public BigDecimal getLatitude() {
		return latitude;
	}
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	public BigDecimal getLongitude() {
		return longitude;
	}
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}
	public boolean isPartialGeocoderMatch() {
		return partialGeocoderMatch;
	}
	public void setPartialGeocoderMatch(boolean partialGeocoderMatch) {
		this.partialGeocoderMatch = partialGeocoderMatch;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		sb.append(address1);
		sb.append(",");
		if(null!=address2 && !address2.isEmpty()) {
			sb.append(address2);
			sb.append(",");
		}
		sb.append(townOrCity);
		sb.append(",");
		sb.append(stateOrRegion);
		if(null!=postalOrZipCode && !postalOrZipCode.isEmpty()) {
			sb.append(",");
			sb.append(postalOrZipCode);
		}
		sb.append(",");
		sb.append(countryName);
		sb.append(".");
		return sb.toString();
	}
	public String getLatLongTuple() {
		String lat = latitude!=null?latitude.toPlainString():"";
		String lng = longitude!=null?longitude.toPlainString():"";
		return lat+','+lng;
	}
	public boolean isNeedsGeocoding() {
		return needsGeocoding;
	}
	public void setNeedsGeocoding(boolean needsGeocoding) {
		this.needsGeocoding = needsGeocoding;
	}
}
