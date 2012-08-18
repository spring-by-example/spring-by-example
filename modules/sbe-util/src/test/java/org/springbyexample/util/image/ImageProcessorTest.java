/*
 * Copyright 2007-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springbyexample.util.image;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests image processor.
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ImageProcessorTest {

    final Logger logger = LoggerFactory.getLogger(ImageProcessorTest.class);

    @Autowired
    private ImageProcessor imageProcessor = null;
    
    /**
     * Tests scaling an image.
     */
    @Test
    public void testScaleImage() throws IOException {
        assertNotNull("ImageProcessor is null.", imageProcessor);
        
        InputStream in = null;
        
        try {
            in = this.getClass().getResourceAsStream("central_park_west.jpg");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            
            imageProcessor.scaleImage(in, out, "jpg");
            
            assertNotNull("Scaled image is null.", out);
            
            BufferedImage scaledImage = ImageIO.read(new ByteArrayInputStream(out.toByteArray()));
            
            int expectedWidth = imageProcessor.getImageScaleWidth();
            int imageWidth = scaledImage.getWidth();
            
            assertTrue("Scaled image width should be less than or equal to '" + expectedWidth + "'.", (imageWidth <= expectedWidth));
        } finally {
            try { in.close(); } catch(Exception e) {}
        }
    }
    
}
