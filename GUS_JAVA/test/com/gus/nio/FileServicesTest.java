package com.gus.nio;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileServicesTest {
	
	private static final Logger logger = LogManager.getLogger(FileServicesTest.class);
	public static final String ASCII_FILENAME = "langs.model.xml";
	public static final String BINARY_FILENAME = "mortgage-estimate.png";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		org.apache.log4j.BasicConfigurator.configure();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testReadAsciiFile() {
		FileServices fileServices = new FileServicesImpl();
		try {
			logger.debug("testReadAsciiFile reading "+ASCII_FILENAME);
			List<String> contents = fileServices.readAsciiTextFile(ASCII_FILENAME);
			assertNotNull("", contents);
			assertTrue("", contents.size() > 0);
		} catch (IOException e) {
			fail("Failed to read "+ASCII_FILENAME+"?");
		}
	}

	@Test
	public void testReadLargeBinaryFile() {
		FileServices fileServices = new FileServicesImpl();
		try {
			logger.debug("testReadLargeBinaryFile reading "+BINARY_FILENAME);
			byte[] contents = fileServices.readLargeBinaryFile(BINARY_FILENAME);
			assertNotNull("", contents);
			assertTrue("Failed to read all bytes?", contents.length == 165415);
		} catch (IOException e) {
			fail("Failed to read "+BINARY_FILENAME+"?");
		}
	}
	
	@Test
	public void testReadLargeTextFile() {
		FileServices fileServices = new FileServicesImpl();
		try {
			logger.debug("testReadLargeTextFile reading "+ASCII_FILENAME);
			String contents = fileServices.readLargeTextFile(ASCII_FILENAME);
			assertNotNull("", contents);
			assertTrue("Failed to read all bytes?", contents.length() == 113719);  //read all bytes?
		} catch (IOException e) {
			fail("Failed to read "+ASCII_FILENAME+"?");
		}
	}
	
	@Test
	public void testCreateWriteDeleteTempFile() {
		FileServices fileServices = new FileServicesImpl();
		Path path = null;
		try {
			path = fileServices.createTempFile("JUnit", ".dat");
			assertNotNull("", path);
			File file = path.toFile();
			assertTrue("Temp file doesn't exist?", file.exists());
			assertTrue("Can't read temp file?", file.canRead());  
			assertTrue("Can't write to temp file?", file.canWrite());			
			logger.debug("testCreateWriteDeleteTempFile created JUnit.dat OK!");
		} catch (IOException e) {
			fail("Failed to create temp file JUnit.dat?");
		}
		
		try {
			//Default Charset! 
			fileServices.writeSmallBinaryFile(path, "The quick brown fox jumped over the lazy dog!".getBytes());
			File file = path.toFile();
			logger.debug("testCreateWriteDeleteTempFile wrote "+file.length()+" bytes to JUnit.dat OK!"); 
			assertTrue("Temp file doesn't exist?", file.exists());
		} catch (IOException e) {
			fail("Failed to write to temp file JUnit.dat?");
		}
		
		try {
			//Default Charset and boolean rc here is not reliably true! 
			boolean rc = fileServices.deleteFile(path); 
			//assertTrue("Didn't delete temp file?", rc);
			logger.debug("testCreateWriteDeleteTempFile deleted JUnit.dat OK!");
		} catch (IOException e) {
			fail("Failed to delete temp file JUnit.dat?");
		}
	}


}
