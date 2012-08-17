package org.springbyexample.util.image;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Image processor.
 * 
 * @author David Winterfeldt
 */
public class ImageProcessorImpl implements ImageProcessor{

	final Logger logger = LoggerFactory.getLogger(ImageProcessorImpl.class);
	
	protected int imageScaleWidth = 200;

	/**
     * Gets image width for scaling.
     */
    public int getImageScaleWidth() {
        return imageScaleWidth;
    }
    
	/**
     * Sets image width for scaling.
     */
    public void setImageScaleWidth(int imageScaleWidth) {
        this.imageScaleWidth = imageScaleWidth;
    }

    /**
     * Creates a scaled new file.
     */
	public void scaleImage(File imageFile, File newImageFile) 
			throws IOException {
        Assert.notNull(imageFile, "Image file can not be null.");
        Assert.notNull(newImageFile, "New image file can not be null.");

	    String imageFileName = imageFile.getName();
        String formatName = imageFileName.substring(imageFileName.lastIndexOf("."));

	    BufferedImage image = ImageIO.read(imageFile);
	    
	    BufferedImage destImage = processImageScaling(image);

	    ImageIO.write(destImage, formatName, newImageFile);

        logger.debug("Created '{}' from orginal file '{}'.", newImageFile.getAbsolutePath(), imageFile.getAbsolutePath());
	}

    /**
     * Creates a scaled new file.
     */
    public void scaleImage(InputStream in, OutputStream out, String formatName) 
            throws IOException {
        Assert.notNull(in, "InputStream can not be null.");
        Assert.notNull(out, "OutStream can not be null.");
        Assert.hasText(formatName, "Format name can not be blank.");
        
        BufferedImage image = ImageIO.read(in);
        
        BufferedImage destImage = processImageScaling(image);

        ImageIO.write(destImage, formatName, out);
        
        logger.debug("Created scaled from orginal file.");
    }
    
    /**
     * Creates a scaled new file.
     */
    protected BufferedImage processImageScaling(BufferedImage image) 
            throws IOException {
		double d = (double) imageScaleWidth / (double) image.getHeight(null);
		
		if (image.getWidth(null) > image.getHeight(null)) {
			d = (double) imageScaleWidth / (double) image.getWidth(null);
		}
		
		int j = (int) (d * (double) image.getWidth(null));
		int k = (int) (d * (double) image.getHeight(null));
		
		BufferedImage destImage = new BufferedImage(j, k, 1);

		AffineTransform at = new AffineTransform();
		
		if (d < 1.0D) {
			at.scale(d, d);
		}
			
		Graphics2D graphics2d = destImage.createGraphics();
		graphics2d.drawImage(image, at, null);
		graphics2d.dispose();
		
		return destImage;
	}
	   
}
