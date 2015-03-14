package com.gus.http.get.google.timezone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;

public class ERoadLatLongTimezoner {

	
	public static final String END_POINT = "https://maps.googleapis.com/maps/api/timezone/json?location={0}&timestamp={1}";
	public static final String USER_AGENT = "Mozilla/5.0";
	
	public static final Gson GSON = new Gson();
	
	public static void main(String[] args) {
		ERoadLatLongTimezoner app = new ERoadLatLongTimezoner();
		
		if(null==args || args.length < 1) {
			System.out.println("Usage: java com.gus.eroad.ERoadLatLongTimezoner inputFile.csv [outputFile.csv]");
			System.out.println("     : inputFile.csv is the full path to the input CSV file and is required.");
			System.out.println("     : outputFile.csv is the full path to the output CSV file and is optional (defaults to output.csv in the working directory).");
			return;
		}
		
		String inputFileName = null, outputFilename = null;
		if(args.length == 2) {
			inputFileName = args[0]; outputFilename = args[1];
		} else {
			inputFileName = args[0]; outputFilename = "output.csv";			
		}
		File inputFile = new File(inputFileName);
		File outputFile = new File(outputFilename);
		System.out.println("ERoadLatLongTimezoner: starting with input="+inputFile.getName()+" output="+outputFile.getName());
		BufferedReader br = null;
		BufferedWriter bw = null;
	    
        try
        {
            br = new BufferedReader(new FileReader(inputFile));
            bw = new BufferedWriter(new FileWriter(outputFile));
            String inputLine = null;
            while ((inputLine = br.readLine()) != null)
            {
                String outputLine = app.parseAndConvert(inputLine);
                //write the output line!
                if(null!=outputLine && outputLine.length() > 0) {
                	bw.write(outputLine);
                	bw.write("\n");
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
        	if (br != null) {
	            try  { 
	                br.close();
	            } catch (IOException ex) {
	                //ignore
	            }
        	}
        	if (bw != null) {
	            try  { 
	                bw.close();
	            } catch (IOException ex) {
	                //ignore
	            }
        	}
        }
		
	}

	private String parseAndConvert(String inputLine) {
		StringBuilder sb = new StringBuilder(); 
		//Do CSV tokenizing the old way 
		StringTokenizer tokenizer = new StringTokenizer(inputLine, ",");
		String dateString = tokenizer.nextToken().trim();
		Date date = parseDateString(dateString);
		String latitude = tokenizer.nextToken().trim();
		String longitude = tokenizer.nextToken().trim();
		String tuple = latitude+","+longitude;
		if(null!=date && null!=tuple && tuple.length() > 0) {
			String json = callGoogleTimezoneAPI(tuple, date);
			if(null!=json && json.length()>0) {
				GoogleTimezoneResponse response = GSON.fromJson(json, GoogleTimezoneResponse.class);
				if(response.getStatus().equalsIgnoreCase("OK")) {
					//build the local time 
					long rawOffset = response.getRawOffset();
					long dstOffset = response.getDstOffset();
					long localTime = date.getTime() + (rawOffset*1000) + (dstOffset*1000); 
					Date localDateTime = new Date(localTime);
					sb.append(dateString);
					sb.append(",");
					sb.append(tuple);
					sb.append(",");
					sb.append(response.getTimeZoneId());
					sb.append(",");
					sb.append(formatDate(localDateTime));
				}
			}
		}
		
		return sb.toString();
	}
	
	private Date parseDateString(String dateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date theDate = null; 
		try {
			theDate = dateFormat.parse(dateString);
		} catch (ParseException e) {
			System.out.println("FAILED to parse date="+dateString);
			e.printStackTrace();
		}
	
		return theDate;
	}
	private String formatDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = null; 
		try {
			dateString = dateFormat.format(date);
		} catch (Exception e) {
			System.out.println("FAILED to format date="+date);
			e.printStackTrace();
		}
		return dateString;
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
	
	private String callGoogleTimezoneAPI(String tuple, Date dateTime) {
		URL url = null;
		HttpsURLConnection con = null;
		Long timeInMillis = dateTime.getTime();  //autobox is awesome
		Long timeInSeconds = timeInMillis/1000;
		String urlString = MessageFormat.format(END_POINT, tuple, timeInSeconds.toString());
		
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			System.out.println("FAILED  to create URL "+END_POINT);
			e.printStackTrace();
		} 
		
		try {
			con = (HttpsURLConnection) url.openConnection();
		} catch (IOException e) {
			System.out.println("FAILED to open connection to URL="+url);
			return null; 
		}
		
		//add request headers
		try {
			con.setRequestMethod("GET");
		} catch (ProtocolException e) {
			System.out.println("FAILED to set 'GET' method on HttpUrlConnection="+con);
			return null; 
		}
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("charset", "utf-8");
		con.setRequestProperty("Accept", "application/json");
 
		// Send GET request 
		con.setDoInput(true);
		con.setDoOutput(false);
		
		int responseCode;
		try {
			responseCode = con.getResponseCode();
		} catch (IOException e) {
			System.out.println("FAILED to get response code from HttpUrlConnection="+con);
			return null;
		}
		
		// Read response text 
		//TODO break this out to another method
		BufferedReader in;
		try {
			in = new BufferedReader( new InputStreamReader(con.getInputStream()) );
		} catch (IOException e) {
			System.out.println("FAILED to get InputStream from HttpUrlConnection="+con);
			return null;
		}
		
		String inputLine, response = "";
		StringBuilder sb = new StringBuilder();
 
		try {
			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
			}
			response = sb.toString();
		} catch (IOException e) {
			System.out.println("FAILED to read response line from BufferedReader");
			return null;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				//ignore
			}
		}
 
		System.out.println("\nGot response = " + response);
		return response;
	}
	
	byte[] readSmallBinaryFile(String aFileName) throws IOException {
	    Path path = Paths.get(aFileName);
	    return Files.readAllBytes(path);
	}
	  
	void writeSmallBinaryFile(byte[] aBytes, String aFileName) throws IOException {
	    Path path = Paths.get(aFileName);
	    Files.write(path, aBytes); //creates, overwrites
	}
	
	public String readLargeTextFile(String filename) { 
		RandomAccessFile csvFile = null;
		MappedByteBuffer byteBuffer = null;
		FileChannel fileChannel = null;
		StringBuilder sb = new StringBuilder();
		try {
			csvFile = new RandomAccessFile(filename, "r");
			fileChannel = csvFile.getChannel();
			
			byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
			byteBuffer.load(); 
	        for (int i = 0; i < byteBuffer.limit(); i++)
	        {
	            sb.append((char) byteBuffer.get());
	        }
		} catch (FileNotFoundException e) {
			System.out.println("FAILED  to find filename "+filename);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("FAILED  to read filename "+filename);
			e.printStackTrace();
		} finally {
			if(null!=byteBuffer) {
				byteBuffer.clear();
			}
			if(null!=fileChannel) {
				try {
					fileChannel.close();
				} catch (IOException e) {
					//ignore
				}
			}
			if(null!=csvFile) {
				try {
					csvFile.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
        
        return sb.toString();
        
	}

}
