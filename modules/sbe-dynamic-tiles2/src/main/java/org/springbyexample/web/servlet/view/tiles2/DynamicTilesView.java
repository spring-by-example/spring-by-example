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

package org.springbyexample.web.servlet.view.tiles2;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

/**
 * <p>View implementation that first checks if the request is 
 * a Tiles definition, then if it isn't tries to retrieve a default Tiles 
 * template and uses the url to dynamically set the body.</p>
 * 
 * <p>The main template name defaults to 'mainTemplate', the 
 * body attribute defaults to to 'content', and the delimiter is '.' 
 * which is used for replacing a '/' in the url.</p>
 * 
 * <p>If a request is made for '/info/index.html', Spring will pass 'info/index' into the view. 
 * The first thing will be to look for 'info/index' as a Tiles definition. 
 * Then a template definition of '.info.mainTemplate', which if found will dynamically have a 
 * body set on this definition. If the previous aren't found, it is assumed a root definition 
 * exists. This would be '.mainTemplate'. If none of these exist, a TilesException will be thrown.</p>
 * 
 * <ol>
 *    <li>Check if a Tiles definition based on the URL matches.</li>
 *    <li>Checks if a definition derived from the URL and default template name exists and then dynamically insert the body based on the URL.</li>
 *    <li>Check if there is a root template definition and then dynamically insert the body based on the URL.</li>
 *    <li>If no match is found from any process above a TilesException is thrown.</li> 
 * </ol>
 * 
 * @author David Winterfeldt
 */
public class DynamicTilesView extends AbstractUrlBasedView {

	final DynamicTilesViewProcessor dynamicTilesViewProcessor = new DynamicTilesViewProcessor();
	
	/**
	 * Main template name.  The default is 'mainTemplate'.
	 * 
	 * @param 	tilesDefinitionName		Main template name used to lookup definitions.
	 */
	public void setTilesDefinitionName(String tilesDefinitionName) {
	    dynamicTilesViewProcessor.setTilesDefinitionName(tilesDefinitionName);
	}

	/**
	 * Tiles body attribute name.  The default is 'body'.
	 * 
	 * @param 	tilesBodyAttributeName		Tiles body attribute name.
	 */
	public void setTilesBodyAttributeName(String tilesBodyAttributeName) {
	    dynamicTilesViewProcessor.setTilesBodyAttributeName(tilesBodyAttributeName);
	}

	/**
	 * Sets Tiles definition delimiter.  For example, instead of using 
	 * the request 'info/about' to lookup the template definition 
	 * 'info/mainTemplate', the default delimiter of '.' 
	 * would look for '.info.mainTemplate' 
	 * 
	 * @param 	tilesDefinitionDelimiter	Optional delimiter to replace '/' in a url.
	 */
	public void setTilesDefinitionDelimiter(String tilesDefinitionDelimiter) {
	    dynamicTilesViewProcessor.setTilesDefinitionDelimiter(tilesDefinitionDelimiter);
	}

	/**
	 * Renders output using Tiles.
	 */
	protected void renderMergedOutputModel(Map model,
	                                       HttpServletRequest request, HttpServletResponse response)
	       throws Exception {

        ServletContext servletContext = getServletContext();
        TilesContainer container = TilesAccess.getContainer(servletContext);
        if (container == null) {
            throw new ServletException("Tiles container is not initialized. " + 
                                       "Have you added a TilesConfigurer to your web application context?");
        }

        exposeModelAsRequestAttributes(model, request);
        
        dynamicTilesViewProcessor.renderMergedOutputModel(getBeanName(), getUrl(), 
                servletContext, request, response, container);
    }

}
