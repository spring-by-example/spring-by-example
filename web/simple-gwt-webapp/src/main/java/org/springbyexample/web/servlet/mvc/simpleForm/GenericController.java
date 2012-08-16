/*
 * Copyright 2002-2006 the original author or authors.
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

package org.springbyexample.web.servlet.mvc.simpleForm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Generic controller.
 * 
 * @author David Winterfeldt
 */
@Controller
public class GenericController { // implements ResourceLoaderAware {

//    private ResourceLoader resourceLoader = null;
//    
//    /**
//     * Implementation of <code>ResourceLoaderAware</code>.
//     */
//    public void setResourceLoader(ResourceLoader resourceLoader) {
//        this.resourceLoader = resourceLoader;
//    }

    /**
     * Necessary for processing of static pages or 
     * ones not requiring initialization.
     */
    @RequestMapping(value="/**/*.htm")
    public void genericPage() {}

//	public String genericPage(HttpServletRequest request) {
//	    String result = null;
//	    
//	    String requestUri = request.getRequestURI();
//	    requestUri = requestUri.substring(request.getContextPath().length());
//	    
//	    if (!resourceLoader.getResource(requestUri).exists()) {
//	        result = requestUri.substring(0, requestUri.lastIndexOf(".html"));
//	    } else {
//	        result = "forward:" + requestUri;
//	    }
//	    
//	    return result;
//	}

}
