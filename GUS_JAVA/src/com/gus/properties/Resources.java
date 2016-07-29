package com.gus.properties;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.gus.nio.FileServices;
import com.gus.nio.FileServicesImpl;

public final class Resources {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Resources app = new Resources();
		ResourceBundle resources = ResourceBundle.getBundle ("ApplicationResources");
		ResourceBundle frenchResources = ResourceBundle.getBundle ("ApplicationResources",Locale.FRANCE);
		ResourceBundle germanResources = ResourceBundle.getBundle ("ApplicationResources",Locale.GERMAN);
		Locale russian = new Locale("ru","RU");
		ResourceBundle russianResources = ResourceBundle.getBundle ("ApplicationResources",russian);
		
		Enumeration<String> keys = resources.getKeys();
		while(keys.hasMoreElements()) {
			String key = keys.nextElement();
			printValue(key,frenchResources);
			printValue(key,germanResources);
			//printValue(key,russianResources);  System.out doesn't display \\uXXXX unicode by default
		}
		
		PrintStream printStream = null;
		keys = resources.getKeys();
		//OutputStreamWriter writer = new OutputStreamWriter(System.out,Charset.forName("UTF-8"));
		//"UTF-16BE" 
		try {
			printStream = new PrintStream(System.out, true, "UTF-8");
			while(keys.hasMoreElements()) {
				String key = keys.nextElement();
				printStream.println(russianResources.getString(key));
			}
			Charset.forName("UTF-8");
			//Greek Omega code point 03A9 or decimal 937 - makes you save this .java file in UTF-8 see the Properties of this file!
			char c1 = 'Ω';
			printStream.println(c1);
			char c2 = '\u043E';
			printStream.println(c2);
			char copyright = '\u00A9';
			printStream.println(copyright);
			printStream.flush();		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(printStream != null) {
				printStream.close();
			} 
		}
		
	}

	public static void printValue(String key,ResourceBundle bundle) {
		String language = bundle.getLocale().getLanguage();
		System.out.println("{key:\""+key+"\", value:\""+bundle.getString(key)+"\", language:\""+language+"\"}");
	}
	
	public static List<String>  readUTF8Text(String filename) {
		FileServices fileServices = new FileServicesImpl();
		List<String> lines = null;
		try {
			lines = fileServices.readSmallUtf8TextFile(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}
}
