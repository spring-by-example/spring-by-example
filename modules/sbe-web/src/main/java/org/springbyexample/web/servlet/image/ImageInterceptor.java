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

package org.springbyexample.web.servlet.image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.util.image.ImageProcessor;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Intercepts a request and if the path to the view matches 
 * a directory in the image directory, a list of images will be 
 * created and thumbnails can optionally be generated.
 * 
 * @author David Winterfeldt
 */
public class ImageInterceptor extends HandlerInterceptorAdapter {

	final Logger logger = LoggerFactory.getLogger(ImageInterceptor.class);
	
	protected ImageProcessor imageProcessor = null;
	protected String rootImagePath = null;
	protected String imagesPath = "/images";
	protected String thumbnailSuffix = "-thumbnail";
	protected String fileSuffix = ".jpg";
	protected String imageViewName = null;	
	protected int rowWidth = 5;
	
	/**
     * Sets image processor.
     */
    public void setImageProcessor(ImageProcessor imageProcessor) {
        this.imageProcessor = imageProcessor;
    }

	/**
	 * Sets root image path.
	 */
	public void setRootImagePath(String rootImagePath) {
		this.rootImagePath = rootImagePath;
	}

	/**
	 * Sets relative image path under root.
	 */
	public void setImagesPath(String imagesPath) {
		this.imagesPath = imagesPath;
	}

	/**
	 * Sets thumbnail suffix.
	 */
	public void setThumbnailSuffix(String thumbnailSuffix) {
		this.thumbnailSuffix = thumbnailSuffix;
	}

	/**
	 * A default image display view to have the interceptor 
	 * redirect to if an images directory is found 
	 * that matches the request.
	 */
    public void setImageViewName(String imageViewName) {
        this.imageViewName = imageViewName;
    }

	/**
	 * Sets file suffice.
	 */
	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	/**
	 * Row width to expose to view for displaying image list.
	 */
	public void setRowWidth(int rowWidth) {
        this.rowWidth = rowWidth;
    }

	/**
	 * Intercepts before request.
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, 
						   Object handler, ModelAndView modelAndView)
			throws Exception {
		List<ThumbnailBean> lResults = new ArrayList<ThumbnailBean>();

		if (!StringUtils.hasText(rootImagePath)) {
			rootImagePath = request.getSession().getServletContext().getRealPath("/");
		}
    	
    	String path = request.getServletPath();
    	path = StringUtils.stripFilenameExtension(path);
		
    	String imagePath = imagesPath + path;
    	String absoluteImagePath = StringUtils.cleanPath(rootImagePath + "/" + imagePath);
    	
    	//logger.debug("imagePath={}", absoluteImagePath);
    	
    	File dir = new File(absoluteImagePath);
    	
		if (dir.isDirectory()) {
			File fileList[] = dir.listFiles();
			
			for (int i = 0; i < fileList.length; i++) {
				File file = fileList[i];
				
				if (file.exists() && !file.isDirectory() && 
			        !file.getName().endsWith(thumbnailSuffix + fileSuffix)) {
					String name = StringUtils.stripFilenameExtension(file.getName());
					
					String thumbnailName = name + thumbnailSuffix + fileSuffix;
					
					String mainImagePath = imagePath + "/" + name + fileSuffix;
					String thumbnailPath = imagePath + "/" + thumbnailName;
					
					File newFile = new File(dir, thumbnailName);
					
					// if thumbnail file doesn't exist, create
					if  (!newFile.exists()) {
						imageProcessor.scaleImage(file, newFile);
					}
					
					lResults.add(new ThumbnailBean(mainImagePath, thumbnailPath));
				}
			}

			modelAndView.setViewName(imageViewName);
		}
		
		modelAndView.addObject("rowWidth", rowWidth);
    	modelAndView.addObject("imageList", lResults);
    }
    	   
}
