package org.springbyexample.util.image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Image processor.
 * 
 * @author David Winterfeldt
 */
public interface ImageProcessor {

    /**
     * Gets image width for scaling.
     */
    public int getImageScaleWidth();
    
    /**
     * Sets image width for scaling.
     */
    public void setImageScaleWidth(int imageScaleWidth);
        
    /**
     * Creates a scaled new file.
     */
	public void scaleImage(File imageFile, File newFile) 
			throws IOException;

    /**
     * Creates a scaled new file.
     */
    public void scaleImage(InputStream in, OutputStream out, String formatName) 
            throws IOException;

}
