package com.gus.nio;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

public interface FileServices {

	public abstract byte[] readSmallBinaryFile(String filename)
			throws IOException;

	public abstract List<String> readSmallAsciiTextFile(String filename)
			throws IOException;

	public abstract List<String> readSmallUtf8TextFile(String filename)
			throws IOException;

	public abstract List<String> readSmallTextFile(String filename,
			Charset charset) throws IOException;

	/**
	 * Overwrites the given file with the given data.  
	 * @param filename
	 * @param data
	 * @throws IOException
	 */
	public abstract Path writeSmallBinaryFile(String filename, byte[] data)
			throws IOException;

	public abstract Path writeSmallBinaryFile(Path path, byte[] data)
			throws IOException;

	public abstract int appendToFile(String filename, String text)
			throws IOException;

	public abstract int appendToFile(String filename, byte[] data)
			throws IOException;

	public abstract int appendToFile(Path path, byte[] data) throws IOException;

	public abstract List<String> readAsciiTextFile(String filename)
			throws IOException;

	public abstract List<String> readUtf8TextFile(String filename)
			throws IOException;

	public abstract List<String> readTextFile(String filename, Charset charset)
			throws IOException;

	public abstract List<String> readTextFile(Path path, Charset charset)
			throws IOException;

	public abstract byte[] readLargeBinaryFile(String filename)
			throws IOException;

	public abstract byte[] readLargeBinaryFile(Path path) throws IOException;

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
	public abstract String readLargeTextFile(String filename)
			throws IOException;

	public abstract Path createFile(String filename) throws IOException;

	public abstract Path createTempFile(String filename) throws IOException;

	/**
	 * Create a temporary file in the default 'temp' directory for this OS.
	 * @param filename
	 * @param extension - ".txt", ".log" ..etc
	 * @throws IOException
	 * @return Path to the new temp file
	 */
	public abstract Path createTempFile(String filename, String extension)
			throws IOException;

	public abstract boolean deleteFile(String filename) throws IOException;

	public abstract boolean deleteFile(Path path) throws IOException;

	public abstract boolean fileExists(String filename) throws IOException;

	public abstract int getBufferSize();

	public abstract void setBufferSize(int bufferSize);

}