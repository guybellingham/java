package com.gus.enumeration;

import java.util.EnumSet;
import java.util.Iterator;

/**
 * Enumeration of acceptable Image formats for photos and logos ..etc 
 * along with their standard mime/types.
 * @author Guy
 *
 */
public enum ImageFormat {
	jpg("image/jpeg"),
	jpeg("image/jpeg"),
	gif("image/gif"),
	png("image/png"),
	bmp("image/bmp");
	
	private String mimeType;
	
	ImageFormat(String mimeType) {
		setMimeType(mimeType);
	}
	
	public static String getSupportedFormats() {
		StringBuilder sb = new StringBuilder();
		EnumSet<ImageFormat> set = EnumSet.allOf(ImageFormat.class);
		for (Iterator<ImageFormat> iterator = set.iterator(); iterator.hasNext();) {
			ImageFormat format = iterator.next();
			sb.append(format.name());
			if(iterator.hasNext()) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
	public String getMimeType() {
		return mimeType;
	}
	protected void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
}
