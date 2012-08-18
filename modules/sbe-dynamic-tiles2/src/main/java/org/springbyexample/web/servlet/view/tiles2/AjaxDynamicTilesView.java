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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.Definition;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.impl.BasicTilesContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.js.ajax.AjaxHandler;
import org.springframework.js.ajax.SpringJavascriptAjaxHandler;
import org.springframework.js.ajax.tiles2.AjaxTilesView;
import org.springframework.web.servlet.support.JstlUtils;
import org.springframework.web.servlet.support.RequestContext;

/**
 * <p>If the request isn't an AJAX request, <code>DynamicTilesView</code> 
 * will handle the request.  Otherwise it is expected that an AJAX 
 * view render will be next in the chain and can handle the request.</p>
 *
 * <p><strong>Note</strong>: Most code is copied from <code>AjaxTilesView</code> (giving author credit to original author).  
 * If the method <code>flattenedAttributeMap</code> also processed temporary 
 * Tiles context attributes, no changes to this class only <code>renderMergedOutputModel</code> 
 * would need to be overridden.</p>
 *
 * @author Jeremy Grelle
 * @author David Winterfeldt
 */
public class AjaxDynamicTilesView extends AjaxTilesView {

    final Logger logger = LoggerFactory.getLogger(AjaxDynamicTilesView.class);
        
    final DynamicTilesViewProcessor dynamicTilesViewProcessor = new DynamicTilesViewProcessor();
	final AjaxHandler ajaxHandler = new SpringJavascriptAjaxHandler();

	/**
	 * Renders output using Tiles.
	 */
    protected void renderMergedOutputModel(Map model,
	                                       HttpServletRequest request, HttpServletResponse response)
	       throws Exception {
	    String beanName = getBeanName();
	    String url = getUrl();

        ServletContext servletContext = getServletContext();
        TilesContainer container = TilesAccess.getContainer(servletContext);
        if (container == null) {
            throw new ServletException("Tiles container is not initialized. " + 
                                       "Have you added a TilesConfigurer to your web application context?");
        }

	    if (ajaxHandler.isAjaxRequest(request, response)) {
	        // change URL used to lookup tiles template to correct definition
	        String definitionName = dynamicTilesViewProcessor.startDynamicDefinition(beanName, url, request, response, container);
	        
	        super.renderMergedOutputModel(model, request, response);
	        
	        dynamicTilesViewProcessor.endDynamicDefinition(definitionName, beanName, request, response, container);
        } else {
            exposeModelAsRequestAttributes(model, request);

            dynamicTilesViewProcessor.renderMergedOutputModel(beanName, url, 
                                                              servletContext, request, response, container);
        }
    }
	
    /**
     * Check whether the underlying resource that the configured URL points to
     * actually exists.
     * @param locale the desired Locale that we're looking for
     * @return <code>true</code> if the resource exists (or is assumed to exist);
     * <code>false</code> if we know that it does not exist
     * @throws Exception if the resource exists but is invalid (e.g. could not be parsed)
     */
	@Override
    public boolean checkResource(Locale locale) throws Exception {
	    // possibly could check if the Tiles template exists or 
	    // the file that will be the body of the dynamic context exists
	    
        return true;
    }
    
}
