package com.gus.java2d;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;

import com.gus.java2d.CropMethod;

public class ImageResizer {

	/**
	 * Method uses Java2D Bilinear sampling to scale a (jpeg) image to the given 
	 * <code>width</code> and <code>height</code>. Does not necessarily maintain 
	 * the image aspect ratio, that is up to the caller whose responsibility it is 
	 * to pick the 'correct' width and height or to crop the image first. 
	 * @param image - input image buffer
	 * @param width
	 * @param height
	 * @return output (scaled) image buffer
	 * @throws IOException
	 */
	public static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {
	    int imageWidth  = image.getWidth();
	    int imageHeight = image.getHeight();

	    double scaleX = (double)width/imageWidth;
	    double scaleY = (double)height/imageHeight;
	    AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
	    AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
	    
	    return bilinearScaleOp.filter(
	        image,
	        new BufferedImage(width, height, image.getType()));
	}
	/**
	 * Crops a given image to the given <code>width</code> and <code>height</code>. 
	 * Note: The (new) width and height must be less than the existing images width and height! 
	 * @param image
	 * @param width
	 * @param height
	 * @param method - {@link CropMethod} controls how the cropping square or rectangle is positioned (offset). 
	 * @return the cropped image buffer
	 * @throws IOException
	 */
	public static BufferedImage getCroppedImage(BufferedImage image, int width, int height, CropMethod method) throws IOException {
		assert(image != null);
		int imageWidth  = image.getWidth();
	    int imageHeight = image.getHeight();
	    if(width > imageWidth) {
			throw new IOException("Cropped width "+width+" cannot be greater than image width "+imageWidth+"!");
		}
	    if(height > imageHeight) {
			throw new IOException("Cropped height "+height+" cannot be greater than image height "+imageHeight+"!");
		}
	    int x = 0 , y = 0;   //coords to crop from

	    switch (method) {
			case TOP_CENTER:
				x = (imageWidth - width)/2;
				break;
			case TOP_RIGHT:
				x = (imageWidth - width); 
				break;
			case CENTER_CENTER:
				x = (imageWidth - width)/2;
				y = (imageHeight - height)/2;
				break;
			default:
				break;
		}
		BufferedImage croppedImage = image.getSubimage(x, y, width, height);
		return croppedImage;
		
	}
	
	public static BufferedImage getScaledImageHighQuality(BufferedImage image, int width, int height) throws IOException {
		int imageWidth  = image.getWidth();
	    int imageHeight = image.getHeight();

	    double scaleX = (double)width/imageWidth;
	    double scaleY = (double)height/imageHeight;
	    
		BufferedImage outputImage = new BufferedImage(width,height,image.getType());
		
		Graphics2D graphics2D = outputImage.createGraphics();
		
		AffineTransform savedAT = graphics2D.getTransform();
		AffineTransform scaleAT = AffineTransform.getScaleInstance(scaleX,scaleY);
//		graphics2D.scale(percent,percent); 

		// Enable smooth, high-quality resampling
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
//		Rectangle2D rect = new Rectangle2D.Float();
//		rect.setRect(0, 0, width, height);
//		graphics2D.clip(rect);
        
		ThumbnailReadyObserver observer = new ThumbnailReadyObserver(Thread.currentThread());
		
		boolean scalingComplete = graphics2D.drawImage(image, scaleAT, observer);
		
		if (!scalingComplete && observer != null) {
			System.out.println("ResizeImage: Scaling is not yet complete!");
			
			while(!observer.ready)
			{
				System.err.println("Waiting .1 sec...");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}
			
		}
		graphics2D.setTransform(savedAT);
		graphics2D.dispose();
		return outputImage;
	}
	

	/** 
	 * https://community.oracle.com/docs/DOC-983611 
	 * Convenience method that returns a scaled instance of the 
	 * provided {@code BufferedImage}. 
	 * @param img the original image to be scaled 
	 * @param targetWidth the desired width of the scaled instance, * in pixels * 
	 * @param targetHeight the desired height of the scaled instance, * in pixels * 
	 * @param hint one of the rendering hints that corresponds to 
	 *  {@code RenderingHints.KEY_INTERPOLATION} (e.g. 
	 *    {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR}, 
	 *    {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR}, 
	 *    {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC}
	 *  ) 
	 * @param higherQuality if true, this method will use a multi-step  
	 * scaling technique that provides higher quality than the usual 
	 * one-step technique (only useful in downscaling cases, where 
	 * {@code targetWidth} or {@code targetHeight} is 
	 * smaller than the original dimensions, and generally only when 
	 * the {@code BILINEAR} hint is specified) 
	 * @return a scaled version of the original {@code BufferedImage} 
	 */ 
	public static BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint, boolean higherQuality) { 
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB; 
		BufferedImage ret = (BufferedImage)img; 
		int w, h; 
		if (higherQuality) { 
			// Use multi-step technique: start with original size, then 
			// scale down in multiple passes with drawImage() 
			// until the target size is reached 
			w = img.getWidth(); h = img.getHeight(); 
		} else { 
			// Use one-step technique: scale directly from original size 
			// to target size with a single drawImage() call 
			w = targetWidth; h = targetHeight; 
		} 
		do { 
			if (higherQuality && w > targetWidth) { 
				w /= 2; 
				if (w < targetWidth) { 
					w = targetWidth; 
				} 
			} 
			if (higherQuality && h > targetHeight) { 
				h /= 2; 
				if (h < targetHeight) {
					h = targetHeight; 
					} 
				} 
			BufferedImage tmp = new BufferedImage(w, h, type); 
			Graphics2D g2 = tmp.createGraphics(); 
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint); 
			// use the scaling variant of Graphics.drawImage() - no AffineTransform needed!
			g2.drawImage(ret, 0, 0, w, h, null); 
			
			g2.dispose(); 
			ret = tmp; 
		} while (w != targetWidth || h != targetHeight); 
		return ret; 
	}
	
	public static class ThumbnailReadyObserver implements ImageObserver {

		private Thread toNotify;
		
		public volatile boolean ready = false;
		
		public ThumbnailReadyObserver(Thread toNotify)
		{
			this.toNotify = toNotify;
			ready = false;
		}
		public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
			
			if ((infoflags & ImageObserver.ALLBITS) > 0)
			{
				ready = true;
				System.out.println("ThumbnailReadyObserver image is ready!");
				toNotify.notify();
				return true;
			}
			return false; 
		}
	}
	
	public static BufferedImage getBufferedImage(File imageFile) throws IOException {
		BufferedImage buffer = ImageIO.read(imageFile);
		return buffer;
	}
	public static BufferedImage getBufferedImage(InputStream imageBytes) throws IOException {
		BufferedImage buffer = ImageIO.read(imageBytes);
		return buffer;
	}
	public static BufferedImage getImage(InputStream imageBytes, String mimeType) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(imageBytes);
		BufferedImage image = null;
		Iterator<ImageReader> it = ImageIO.getImageReadersByMIMEType(mimeType);
		ImageReader reader = it.next();
		ImageInputStream iis = ImageIO.createImageInputStream(bis);
		reader.setInput(iis, false, false);
		image = reader.read(0);
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		System.out.println("getImage type="+mimeType+" got buffered image from stream width="+width+" height="+height);
		IIOMetadata imageMetadata = reader.getImageMetadata(0);
		return image;
	}
	
	public static boolean writeBufferedImage(BufferedImage buffer, String format, File outputFile) {
		try {
			return ImageIO.write(buffer, format, outputFile);
		} catch (IOException e) {
			System.err.println("FAILED to write buffer to output file '"+outputFile+"'!");
			return false;
		}
	}
	
	/**
	 * Write a JPEG file setting the compression quality.
	 * 
	 * @param image -  a BufferedImage to be saved
	 * @param outputFile - destination .JPG file 
	 * @param quality -  a decimal between 0.0 and 1.0, where 1.0 means uncompressed.
	 * @throws IOException  in case of problems writing the file
	 */
	public static void writeJpeg(BufferedImage image, File outputFile, float quality) 
	throws IOException {
	    ImageWriter writer = null;
	    FileImageOutputStream output = null;
	    try {
	        writer = ImageIO.getImageWritersByFormatName("jpeg").next();
	        ImageWriteParam param = writer.getDefaultWriteParam();
	        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	        param.setCompressionQuality(quality);
	        output = new FileImageOutputStream(outputFile);
	        writer.setOutput(output);
	        IIOImage iioImage = new IIOImage(image, null, null);
	        writer.write(null, iioImage, param);
	    } finally {
	        if (writer != null) writer.dispose();
	        if (output != null) output.close();
	    }
	}
    /**
     * Get a List of accepted File Types.
     * Normally, these are: bmp, jpg, wbmp, jpeg, png, gif
     * The exact list may depend on the Java installation.
     * 
     * @return MIME-Types
     */
	public static String[] getAcceptedMIMETypes()
	{
		return ImageIO.getReaderMIMETypes();
	}
	
}