package com.gus.java2d;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageResizer {

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
	
	public static BufferedImage getCroppedImage(BufferedImage image, int width, int height, CropMethod method) throws IOException {
		assert(image != null);
		int imageWidth  = image.getWidth();
	    int imageHeight = image.getHeight();
	    if(width >= imageWidth) {
			throw new IOException("Cropped width "+width+" must be less than image width "+imageWidth+"!");
		}
	    if(height >= imageHeight) {
			throw new IOException("Cropped height "+height+" must be less than image height "+imageHeight+"!");
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
	
	/*
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
	*/
	
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
	
	public static boolean writeBufferedImage(BufferedImage buffer, File outputFile) {
		try {
			return ImageIO.write(buffer, "jpg", outputFile);
		} catch (IOException e) {
			System.err.println("FAILED to write buffer to output file '"+outputFile+"'!");
			return false;
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