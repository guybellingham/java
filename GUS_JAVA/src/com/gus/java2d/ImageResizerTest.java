package com.gus.java2d;

import static org.junit.Assert.fail;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.gus.java2d.CropMethod;

public class ImageResizerTest {

	public static final String FORMAT_JPEG = "jpg";
	public static final String MIME_TYPE_JPEG = "image/jpeg";
	public static final String FORMAT_GIF = "gif";
	public static final String MIME_TYPE_GIF = "image/gif";
	public static final String FORMAT_PNG = "png";
	public static final String MIME_TYPE_PNG = "image/png";
	
	protected InputStream getInputImageStream(String filepath) {
		InputStream clouds = this.getClass().getClassLoader().getResourceAsStream(filepath);
		return clouds;
	}
	
	@Test
	public void testScaleJPEGImage() {
		try {
			File outputFile = new File("C:\\Temp\\joker_scaled_60.jpg");
			if(outputFile.exists()) {
				outputFile.delete();
			}
			InputStream joker = getInputImageStream("com/gus/java2d/joker-2048.jpg");
			if(joker != null) {
				BufferedImage buffer =  ImageResizer.getBufferedImage(joker); 
				BufferedImage scaledBuffer = ImageResizer.getScaledImage(buffer, 60, 60);
				ImageResizer.writeBufferedImage(scaledBuffer, FORMAT_JPEG, outputFile);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}
	}
	
	@Test
	public void testScaleJPEGImageBilinearHighQuality() {
		try {
			File outputFile = new File("C:\\Temp\\joker_scaled_bilinear_60_hq.jpg");
			if(outputFile.exists()) {
				outputFile.delete();
			}
			InputStream joker = getInputImageStream("com/gus/java2d/joker-2048.jpg");
			if(joker != null) {
				BufferedImage buffer =  ImageResizer.getBufferedImage(joker); 
				BufferedImage scaledBuffer = ImageResizer.getScaledInstance(buffer, 60, 60, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
				ImageResizer.writeJpeg(scaledBuffer, outputFile, 0.95F);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}
	}
	
	@Test
	public void testCropAndScaleJPEGImageThumbnail() {
		try {
			File outputFile = new File("C:\\Temp\\joker_crop_scaled_60.jpg");
			if(outputFile.exists()) {
				outputFile.delete();
			}
			InputStream joker = getInputImageStream("com/gus/java2d/joker-2048.jpg");
			if(joker != null) {
				BufferedImage buffer =  ImageResizer.getBufferedImage(joker); 
				BufferedImage croppedBuffer = ImageResizer.getCroppedImage(buffer, 1366, 1366, CropMethod.TOP_CENTER);
				BufferedImage scaledBuffer = ImageResizer.getScaledImage(croppedBuffer, 60, 60);
				ImageResizer.writeBufferedImage(scaledBuffer, FORMAT_JPEG, outputFile);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}
	}
	
	@Test
	public void testCropAndScaleJPEGHighQuality() {
		try {
			File outputFile = new File("C:\\Temp\\joker_crop_scaled_bicubic_60_hq.jpg");
			if(outputFile.exists()) {
				outputFile.delete();
			}
			InputStream joker = getInputImageStream("com/gus/java2d/joker-2048.jpg");
			if(joker != null) {
				BufferedImage buffer =  ImageResizer.getBufferedImage(joker); 
				BufferedImage croppedBuffer = ImageResizer.getCroppedImage(buffer, 1366, 1366, CropMethod.TOP_CENTER);
				BufferedImage scaledBuffer = ImageResizer.getScaledInstance(croppedBuffer, 60, 60, RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);
				ImageResizer.writeJpeg(scaledBuffer, outputFile, 1.0F);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}
	}
	
	@Test
	public void testCropAndScaleLargeGreyJPEGImage() {
		try {
			File outputFile = new File("C:\\Temp\\headshot_crop_scaled_bicubic_60_hq.jpg");
			if(outputFile.exists()) {
				outputFile.delete();
			}
			System.out.println("Reading greyscale.jpg");
			long start = System.currentTimeMillis(), stop = 0L; 
			InputStream greyscaled = getInputImageStream("com/gus/java2d/headshot.jpg");
			if(greyscaled != null) {
				BufferedImage buffer =  ImageResizer.getBufferedImage(greyscaled); 
				BufferedImage croppedBuffer = ImageResizer.getCroppedImage(buffer, 2187, 2187, CropMethod.TOP_CENTER);
				BufferedImage scaledBuffer = ImageResizer.getScaledInstance(croppedBuffer, 60, 60, RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);
				ImageResizer.writeJpeg(scaledBuffer, outputFile, 0.9F);
				stop = System.currentTimeMillis();
				System.out.println("Wrote grey_scaled.jpg in "+(stop - start)+"ms");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}
	}
	
	@Test
	public void testScaleGIFImage() {
		try {
			File outputFile = new File("C:\\Temp\\homer_scaled_60_hq.gif");
			if(outputFile.exists()) {
				outputFile.delete();
			}
			InputStream clouds = getInputImageStream("com/gus/java2d/homer.gif");
			if(clouds != null) {
				BufferedImage buffer =  ImageResizer.getBufferedImage(clouds); 
				BufferedImage scaledBuffer = ImageResizer.getScaledInstance(buffer, 60, 60, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
				ImageResizer.writeBufferedImage(scaledBuffer, FORMAT_GIF, outputFile);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}
	}
	
	@Test
	public void testScalePNGImage() {
		try {
			File outputFile = new File("C:\\Temp\\lisa_scaled_60_hq.png");
			if(outputFile.exists()) {
				outputFile.delete();
			}
			InputStream clouds = getInputImageStream("com/gus/java2d/Lisa.png");
			if(clouds != null) {
				BufferedImage buffer =  ImageResizer.getBufferedImage(clouds); 
				BufferedImage scaledBuffer = ImageResizer.getScaledInstance(buffer, 60, 60, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
				ImageResizer.writeBufferedImage(scaledBuffer, FORMAT_PNG, outputFile);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}
	}
	
	@Test
	public void testCropJPEGImageTopCenter() {
		try {
			File outputFile = new File("C:\\Temp\\bart_crop_topcenter.jpg");
			if(outputFile.exists()) {
				outputFile.delete();
			}
			InputStream bart = getInputImageStream("com/gus/java2d/bart.jpg");
			if(bart != null) {
				BufferedImage buffer =  ImageResizer.getBufferedImage(bart); 
				BufferedImage croppedBuffer = ImageResizer.getCroppedImage(buffer, 300, 300, CropMethod.TOP_CENTER);
				ImageResizer.writeBufferedImage(croppedBuffer, FORMAT_JPEG, outputFile);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}
		
	}
	@Test
	public void testCropGIFImageCenterCenter() {
		try {
			File outputFile = new File("C:\\Temp\\mrburns_crop_centercenter.gif");
			if(outputFile.exists()) {
				outputFile.delete();
			}
			InputStream mrburns = getInputImageStream("com/gus/java2d/mrburns.gif");
			if(mrburns != null) {
				BufferedImage buffer =  ImageResizer.getBufferedImage(mrburns); 
				BufferedImage croppedBuffer = ImageResizer.getCroppedImage(buffer, 200, 200, CropMethod.CENTER_CENTER);
				ImageResizer.writeBufferedImage(croppedBuffer,  FORMAT_GIF, outputFile);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}
		
	}
	@Test
	public void testCropPNGImageTopCenter() {
		try {
			File outputFile = new File("C:\\Temp\\lisa_crop_topcenter.png");
			if(outputFile.exists()) {
				outputFile.delete();
			}
			InputStream clouds = getInputImageStream("com/gus/java2d/Lisa.png");
			if(clouds != null) {
				BufferedImage buffer =  ImageResizer.getBufferedImage(clouds); 
				BufferedImage croppedBuffer = ImageResizer.getCroppedImage(buffer, 400, 400, CropMethod.TOP_CENTER);
				ImageResizer.writeBufferedImage(croppedBuffer,  FORMAT_PNG, outputFile);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}
		
	}
}
