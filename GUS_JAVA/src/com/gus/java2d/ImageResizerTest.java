package com.gus.java2d;

import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.gus.java2d.CropMethod;

public class ImageResizerTest {

	protected InputStream getInputImageStream() {
		InputStream clouds = this.getClass().getClassLoader().getResourceAsStream("com/gus/java2d/clouds.jpg");
		return clouds;
	}
	
	@Test
	public void testScaleImage() {
		
		try {
			File outputFile = new File("C:\\Temp\\clouds_scaled.jpg");
			if(outputFile.exists()) {
				outputFile.delete();
			}
			InputStream clouds = getInputImageStream();
			if(clouds != null) {
				BufferedImage buffer =  ImageResizer.getBufferedImage(clouds); 
				BufferedImage scaledBuffer = ImageResizer.getScaledImage(buffer, 100, 100);
				ImageResizer.writeBufferedImage(scaledBuffer, outputFile);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}
		
	}

	@Test
	public void testCropImageTopCenter() {
		try {
			File outputFile = new File("C:\\Temp\\clouds_crop_topcenter.jpg");
			if(outputFile.exists()) {
				outputFile.delete();
			}
			InputStream clouds = getInputImageStream();
			if(clouds != null) {
				BufferedImage buffer =  ImageResizer.getBufferedImage(clouds); 
				BufferedImage scaledBuffer = ImageResizer.getCroppedImage(buffer, 100, 100, CropMethod.TOP_CENTER);
				ImageResizer.writeBufferedImage(scaledBuffer, outputFile);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}
		
	}
	@Test
	public void testCropImageCenterCenter() {
		try {
			File outputFile = new File("C:\\Temp\\clouds_crop_centercenter.jpg");
			if(outputFile.exists()) {
				outputFile.delete();
			}
			InputStream clouds = getInputImageStream();
			if(clouds != null) {
				BufferedImage buffer =  ImageResizer.getBufferedImage(clouds); 
				BufferedImage scaledBuffer = ImageResizer.getCroppedImage(buffer, 100, 100, CropMethod.CENTER_CENTER);
				ImageResizer.writeBufferedImage(scaledBuffer, outputFile);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}
		
	}
}
