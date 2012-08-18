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

package org.springbyexample.web.servlet.mvc.gwt;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Spring Controller that just called GWT's <code>RemoteServiceServlet.doPost(request, response)</code>.
 * 
 * @author David Winterfeldt
 */
public abstract class GwtController extends RemoteServiceServlet implements Controller, ServletContextAware {

    private static final long serialVersionUID = -8355008871464981285L;

    final Logger logger = LoggerFactory.getLogger(GwtController.class);

    private ServletContext servletContext;

    /**
     * Gets <code>ServletContext</code>.
     */
    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Implementation of <code>ServletContextAware</code>.
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Handles request and delegates to GWT's <code>RemoteServiceServlet.doPost(request, response)</code>.
     */
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        try {
            logger.debug("Processing handleRequest before.");
            
            doPost(request, response);
            
            logger.debug("Processing handleRequest after.");
            
            return null;
        } catch (Exception e) {
            logger.error("Handle request error '" + e.getMessage() + "'", e);

            return null;
        }
    }

}
