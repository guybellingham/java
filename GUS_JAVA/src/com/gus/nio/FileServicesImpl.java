package com.gus.nio;

import static java.nio.file.StandardOpenOption.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FilePermission;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public final class FileServicesImpl {

	private static final Logger logger = LogManager.getLogger(FileServicesImpl.class);
	private int bufferSize = 4096;   //4K
	
	public byte[] readSmallBinaryFile(String filename) throws IOException {
	    Path path = Paths.get(filename);
	    return Files.readAllBytes(path);
	}
	
	public List<String> readSmallTextFile(String filename) throws IOException {
	    Path path = Paths.get(filename);
	    return Files.readAllLines(path);
	}
	/**
	 * Overwrites the given file with the given data.  
	 * @param filename
	 * @param data
	 * @throws IOException
	 */
	public Path writeSmallBinaryFile(String filename, byte[] data) throws IOException {
	    Path path = Paths.get(filename);
	    return writeSmallBinaryFile(path,data);
	}   
	public Path writeSmallBinaryFile(Path path, byte[] data) throws IOException {
	    //StandardOpenOption.CREATE TRUNCATE_EXISTING are the defaults here!
	    return Files.write(path, data); //creates, overwrites
	}
	
	public int appendToFile(String filename, String text) throws IOException {
		Path path = Paths.get(filename);
		return appendToFile(path,text.getBytes());   //default Charset!
	}
	public int appendToFile(String filename, byte[] data) throws IOException {
		Path path = Paths.get(filename);
		return appendToFile(path,data);
	}
	public int appendToFile(Path path, byte[] data) throws IOException {
		ByteBuffer byteBuffer = ByteBuffer.wrap(data); 
		Set<OpenOption> options = new HashSet<OpenOption>();
	    options.add(APPEND);
	    options.add(CREATE);  //create if it doesn't exist
	    
	    try (SeekableByteChannel sbc = Files.newByteChannel(path, options)) {
	    	return sbc.write(byteBuffer);
	    } catch (IOException e) {
	    	logger.error("FAILED  to appendToFile("+path.toAbsolutePath()+")",e);
			throw e;
	    }
	}
	
	public List<String>  readAsciiTextFile(String filename) throws IOException {
		Charset charset = Charset.forName("US-ASCII");
		return readTextFile(filename, charset);
	}
	public List<String>  readUtf8TextFile(String filename) throws IOException {
		Charset charset = Charset.forName("UTF-8");
		return readTextFile(filename, charset);
	}
	public List<String>  readTextFile(String filename, Charset charset) throws IOException {
		Path path = Paths.get(filename);
		return readTextFile(path, charset);
	}
	public List<String>  readTextFile(Path path, Charset charset) throws IOException {
		List<String> output = new ArrayList<String>(); 
		try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		    	output.add(line);
		    }
		} catch (IOException e) {
			logger.error("FAILED  to readAsciiTextFile "+path.toAbsolutePath(),e);
			throw e;
		} 
		return output;
	}
	
	public byte[] readLargeBinaryFile(String filename) throws IOException {
		Path path = Paths.get(filename);
		return readLargeBinaryFile(path);
	}
	public byte[] readLargeBinaryFile(Path path) throws IOException {
		ByteBuffer byteBuffer = null;
		ByteArrayOutputStream bytesOut = null;
		
		//StandardOpenOption.READ is the default here
		try(SeekableByteChannel fileChannel = Files.newByteChannel(path)) {
			
			int fileSize = (int)fileChannel.size();
			// Allocate a "direct" ByteBuffer
	        byteBuffer = ByteBuffer.allocateDirect(getBufferSize());
	        
	        bytesOut = new ByteArrayOutputStream(fileSize);
	        long startTime = 0L, elapsedTime = 0L;
	        
	        if(logger.isDebugEnabled()) {
	        	startTime = System.nanoTime();
				logger.debug("readLargeBinaryFile("+path.getFileName()+") about to map "+fileChannel.size()+" bytes into byteBuffer!");
			}
	        
	        int bytesCount = 0;
	        while ((bytesCount = fileChannel.read(byteBuffer)) > 0) { // Read data from file into ByteBuffer
	        	// flip the buffer which set the limit to current position, and position to 0.
	        	byteBuffer.flip();
	        	//remaining is the number of bytes between position --> limit
	        	byte[] bytes = new byte[byteBuffer.remaining()];
	        	byteBuffer.get(bytes);
	            
	        	bytesOut.write(bytes);
	            // Write data from ByteBuffer to file
	        	byteBuffer.clear();     // For the next read
	        }
	        if(logger.isDebugEnabled()) {
	        	elapsedTime = System.nanoTime() - startTime;
	        	logger.debug("readLargeBinaryFile: elapsed=" + (elapsedTime / 1000000.0) + " msec");
	        }
		} catch (FileNotFoundException e) {
			logger.error("FAILED  to find filename "+path.toAbsolutePath(),e);
			throw e;
		} finally {
			if(null!=byteBuffer) {
				byteBuffer.clear();
			}
		}
		return bytesOut.toByteArray();
	}
	

	/**
	 * <p>A region of a file may be mapped into memory in one of three modes (read, read/write and private),
	 *  this method uses (MapMode.READ_ONLY): 
	 *  Any attempt to modify the resulting buffer will cause a ReadOnlyBufferException to be thrown.
	 * <p>For most operating systems, mapping a file into memory is more expensive than reading or writing 
	 * a few tens of kilobytes of data via the usual read and write methods. From the standpoint of performance
	 *  it is generally only worth mapping relatively LARGE files >50K into memory. 
	 * @param filename
	 * @return String containing the contents of the text file or an empty String if an error occurred
	 */
	public String readLargeTextFile(String filename) throws IOException { 
		RandomAccessFile bigFile = null;
		MappedByteBuffer byteBuffer = null;
		FileChannel fileChannel = null;
		StringBuilder sb = new StringBuilder();
		String encoding = System.getProperty("file.encoding");   //uses default char encoding for this OS
		try {
			bigFile = new RandomAccessFile(filename, "r");
			fileChannel = bigFile.getChannel();
			if(logger.isDebugEnabled()) {
				logger.debug("readLargeTextFile("+filename+") about to map "+fileChannel.size()+" bytes into byteBuffer!");
			}
			byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
			byteBuffer.load(); 
			CharBuffer charBuffer = Charset.forName(encoding).decode(byteBuffer);
			charBuffer.rewind();
			sb.append(charBuffer);
		} catch (FileNotFoundException e) {
			logger.error("FAILED  to find filename "+filename,e);
			throw e;
		} catch (IOException e) {
			logger.error("FAILED  to read filename "+filename,e);
			throw e;
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
			if(null!=bigFile) {
				try {
					bigFile.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
        return sb.toString();
	}

	public Path createFile(String filename) throws IOException {
		Path path = Paths.get(filename);
		try {
		    // Create the empty file with default permissions, etc.
		    Files.createFile(path);
		} catch (FileAlreadyExistsException e) {
		    logger.error("FAILED  to create filename "+filename,e);
			throw e;
		} catch (IOException e) {
		    // Some other sort of failure, such as permissions.
			logger.error("FAILED  to create filename "+filename,e);
			throw e;
		}
		return path;
	}
	
	public Path createTempFile(String filename) throws IOException {
		return createTempFile(filename, ".temp");
	}
	/**
	 * Create a temporary file in the default 'temp' directory for this OS.
	 * @param filename
	 * @param extension - ".txt", ".log" ..etc
	 * @throws IOException
	 * @return Path to the new temp file
	 */
	public Path createTempFile(String filename, String extension) throws IOException {
		//UNIX stuff
//		Set<PosixFilePermission> perms =
//			      PosixFilePermissions.fromString("rw-r-----");
//		FileAttribute<Set<PosixFilePermission>> attrs =
//			      PosixFilePermissions.asFileAttribute(perms);
		Path pathToFile = null;
		try {
		    // Create the empty file with default permissions, etc.
			pathToFile = Files.createTempFile(filename, extension);
		} catch (FileAlreadyExistsException e) {
		    logger.error("FAILED  to create filename "+filename,e);
			throw e;
		} catch (IOException e) {
		    // Some other sort of failure, such as permissions.
			logger.error("FAILED  to create filename "+filename,e);
			throw e;
		}
		return pathToFile;
	}
	
	public boolean deleteFile(String filename) throws IOException {
		Path path = Paths.get(filename);
		return deleteFile(path);
	}
	public boolean deleteFile(Path path) throws IOException {
		boolean rc = false;
		try {
		    rc = Files.deleteIfExists(path);
		} catch (IOException e) {
		    // Some other sort of failure, such as permissions.
			logger.error("FAILED  to delete filename "+path.toAbsolutePath()+"?",e);
			throw e;
		}
		return rc;
	}
	
	public boolean fileExists(String filename) throws IOException {
		Path path = Paths.get(filename);
		boolean rc = Files.exists(path, LinkOption.NOFOLLOW_LINKS);
		return rc;
	}
	
	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}
}
