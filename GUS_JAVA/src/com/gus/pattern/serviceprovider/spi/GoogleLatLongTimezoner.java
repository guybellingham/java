package com.gus.pattern.serviceprovider.spi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.gus.exception.LatLongTimezoneException;
import com.gus.pattern.observer.Address;
import com.gus.pattern.serviceprovider.LatLongTimezone;

public class GoogleLatLongTimezoner implements LatLongTimezone {

	private static final Logger logger = LogManager.getLogger(GoogleLatLongTimezoner.class);
	
	public static final String END_POINT = "https://maps.googleapis.com/maps/api/timezone/json?location={0}&timestamp={1}";
	public static final String USER_AGENT = "Mozilla/5.0";
	
	public static final Gson GSON = new Gson();
	
	public GoogleLatLongTimezoner() {		
	}
	
	/* (non-Javadoc)
	 * @see com.gus.pattern.serviceprovider.spi.AddressGeocodingService#geocode(java.lang.String)
	 */
	@Override
	public void getTimezone(Address address) throws LatLongTimezoneException {
		URL url = null;
		HttpsURLConnection con = null;
		Long timeInMillis = System.currentTimeMillis();
		Long timeInSeconds = timeInMillis/1000;
		String tuple = address.getLatLongTuple();
		String urlString = MessageFormat.format(END_POINT, tuple, timeInSeconds.toString());
		logger.debug("getTimezone() for Address=" + address + " tuple=" + tuple);
		
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			logger.error("FAILED  to create URL "+urlString,e);
			throw new LatLongTimezoneException("FAILED to create URL");
		} 
		
		try {
			con = (HttpsURLConnection) url.openConnection();
		} catch (IOException e) {
			logger.error("FAILED to open connection to URL="+url,e);
			throw new LatLongTimezoneException("FAILED to open connection");
		}
		
		//add request headers
		try {
			con.setRequestMethod("GET");
		} catch (ProtocolException e) {
			logger.error("FAILED to set 'GET' method on HttpUrlConnection="+con,e);
			throw new LatLongTimezoneException("FAILED to set GET method");
		}
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("charset", "utf-8");
		con.setRequestProperty("Accept", "application/json");
 
		// Send GET request 
		con.setDoInput(true);
		con.setDoOutput(false);
		
		
		String jsonResponse = null; 
		try {
			int responseCode = con.getResponseCode();
			//TODO Check for responseCode==200 ?
			jsonResponse = readResponse(con);
			
		} catch (IOException e) {
			logger.error("FAILED to get response from HttpUrlConnection="+con,e);
			throw new LatLongTimezoneException("FAILED to get a response");
		}

		GoogleTimezoneResponse response = GSON.fromJson(jsonResponse, GoogleTimezoneResponse.class);
		if(response.getStatus().equalsIgnoreCase("OK")) {
			String timezoneId = response.getTimeZoneId();
			TimeZone timeZone = TimeZone.getTimeZone(timezoneId);
			address.setTimeZone(timeZone);
			logger.debug("\nGot TimeZone = " + timezoneId);
		} else {
			logger.error("FAILED to get OK response status="+response.getStatus()+" error="+response.getError_message());
			throw new LatLongTimezoneException("FAILED to get \"OK\" response");
		}
	}
	
	public String readResponse(HttpsURLConnection con) throws LatLongTimezoneException { 
		// Read response text 
		BufferedReader in;
		try {
			in = new BufferedReader( new InputStreamReader(con.getInputStream()) );
		} catch (IOException e) {
			logger.error("FAILED to get InputStream from HttpUrlConnection="+con,e);
			throw new LatLongTimezoneException("FAILED to get InputStream from response");
		}
		
		String inputLine, response = "";
		StringBuilder sb = new StringBuilder();
 
		try {
			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
			}
			response = sb.toString();
		} catch (IOException e) {
			logger.error("FAILED to read response line from BufferedReader",e);
			throw new LatLongTimezoneException("FAILED to read response");
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				//ignore
			}
		}
 
		return response;
	}
	
	public class GoogleTimezoneResponse {
		private int dstOffset, rawOffset;
		private String timeZoneId, status, error_message;
		public int getDstOffset() {
			return dstOffset;
		}
		public void setDstOffset(int dstOffset) {
			this.dstOffset = dstOffset;
		}
		public int getRawOffset() {
			return rawOffset;
		}
		public void setRawOffset(int rawOffset) {
			this.rawOffset = rawOffset;
		}
		public String getTimeZoneId() {
			return timeZoneId;
		}
		public void setTimeZoneId(String timeZoneId) {
			this.timeZoneId = timeZoneId;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getError_message() {
			return error_message;
		}
		public void setError_message(String error_message) {
			this.error_message = error_message;
		}
	}
}
