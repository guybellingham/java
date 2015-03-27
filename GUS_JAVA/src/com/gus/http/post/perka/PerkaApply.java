package com.gus.http.post.perka;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.apache.commons.codec.binary.Base64;

public class PerkaApply {

	public static final String FILE_NAME = "Guy Bellingham Resume.pdf";
	public static final String END_POINT = "https://getperka.com/api/2/apply";
	public static final String USER_AGENT = "Mozilla/5.0";
	
	public static void main(String[] args) {
		PerkaApply app = new PerkaApply();
		byte[] resume = null;
		
		try {
			resume = app.readSmallBinaryFile(FILE_NAME);
			System.out.println("PerkaApply got file "+FILE_NAME);
		} catch (IOException e) {
			System.out.println("PerkaApply FAILED  to get file "+FILE_NAME+" from classpath!");
			e.printStackTrace();
			return;
		}
		Base64 encoder = new Base64();
		
		String encodedResume = encoder.encodeToString(resume);
		System.out.println("PerkaApply Base64 encoded file: "+encodedResume);
		URL url = null;
		HttpsURLConnection con = null;
		
		try {
			url = new URL(END_POINT);
			
			con = (HttpsURLConnection)url.openConnection();
			
			app.sendPost(con,encodedResume);
			
			//app.print_https_cert(con);
			
		} catch (MalformedURLException e) {
			System.out.println("PerkaApply FAILED  to create URL "+END_POINT);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("PerkaApply FAILED  to connect to URL "+END_POINT);
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("PerkaApply FAILED  to POST to URL "+END_POINT);
			e.printStackTrace();
		} finally {
			if(null != con) {
				con.disconnect();
			}
		}
		
	}

	byte[] readSmallBinaryFile(String aFileName) throws IOException {
	    Path path = Paths.get(aFileName);
	    return Files.readAllBytes(path);
	}
	  
	void writeSmallBinaryFile(byte[] aBytes, String aFileName) throws IOException {
	    Path path = Paths.get(aFileName);
	    Files.write(path, aBytes); //creates, overwrites
	}
	
	private void print_https_cert(HttpsURLConnection con){
		 
	    if(con!=null){
	    	try {
				System.out.println("Response Code : " + con.getResponseCode());
				System.out.println("Cipher Suite : " + con.getCipherSuite());
				System.out.println("\n");
			 
				Certificate[] certs = con.getServerCertificates();
				for(Certificate cert : certs){
				   System.out.println("Cert Type : " + cert.getType());
				   System.out.println("Cert Hash Code : " + cert.hashCode());
				   System.out.println("Cert Public Key Algorithm : " 
			                                    + cert.getPublicKey().getAlgorithm());
				   System.out.println("Cert Public Key Format : " 
			                                    + cert.getPublicKey().getFormat());
				   System.out.println("\n");
				}
		 
			} catch (SSLPeerUnverifiedException e) {
				e.printStackTrace();
			} catch (IOException e){
				e.printStackTrace();
			}
	 
	     }
	 
	}
	
	// HTTP POST request
	private void sendPost(HttpsURLConnection con,String encodedResume) throws Exception {			
	 
			
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			sb.append("\"first_name\":\"Guy\",");
			sb.append("\"last_name\":\"Bellingham\",");
			sb.append("\"email\":\"guybellingham@gmail.com\",");
			sb.append("\"position_id\":\"Java_API\",");
			sb.append("\"explanation\":\"First I tried using an HTML form and jQuery ajax to post the data as JSON, but I ran into 'cross domain' issues. Then I tried using Java net HttpsURLConnection to send a json string.\",");
			sb.append("\"projects\":[\"https://github.com/guybellingham/bettermenu\",\"https://github.com/guybellingham/java\"],");
			sb.append("\"source\":\"Elise Hermann,Edgelink\",");
			sb.append("\"resume\":\""+encodedResume+"\"");
			sb.append("}");
			String data = sb.toString();
			byte[] postData       = data.getBytes( Charset.forName( "UTF-8" ));
			int    postDataLength = postData.length;
			
			//add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Content-Length", String.valueOf(postDataLength));
			con.setRequestProperty("charset", "utf-8");
			con.setRequestProperty("Accept", "application/json");
	 
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(postData);
			wr.flush();
			wr.close();
	 
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + con.getURL());
			System.out.println("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	 
			//print result
			System.out.println("Response : " + response.toString());
	 
		}
	 
		
	private void print_content(HttpsURLConnection con){
		if(con!=null){
 
			try {
		 
			   System.out.println("****** Content of the URL ********");			
			   BufferedReader br = 
				new BufferedReader(
					new InputStreamReader(con.getInputStream()));
		 
			   String input;
		 
			   while ((input = br.readLine()) != null){
			      System.out.println(input);
			   }
			   br.close();
		 
			} catch (IOException e) {
			   e.printStackTrace();
			}
		 
		}
		 
	 }
}
