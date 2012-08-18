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

package org.springbyexample.web.context;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ServletContextAware;

/**
 * <code>ServletContext</code> scope implementation.
 * Can be used to share a bean from one web application to another 
 * if the web container has cross context support.
 * 
 * @author David Winterfeldt
 */
public class ServletContextScope implements Scope, ServletContextAware {

    final Logger logger = LoggerFactory.getLogger(ServletContextScope.class);
    
    protected ServletContext servletContext = null;
    protected String context = null;
    
    /**
     * Implementation of <code>ServletContextAware</code>.
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Gets context to use when getting and setting 
     * a <code>ServletContext</code> attribute (ex: '/simple-form').
     * If no context is specified, current one will be used.
     */
    public String getContext() {
        return context;
    }

    /**
     * Sets context to use when getting and setting 
     * a <code>ServletContext</code> attribute (ex: '/simple-form').
     * If no context is specified, current one will be used.
     */
    public void setContext(String context) {
        this.context = context;
    }

    /**
     * Gets bean from scope.
     */
    public Object get(String name, ObjectFactory factory) {
        Object result = null;
       
        ServletContext sc = getServletContext();
        
        result = sc.getAttribute(name);
        
        if (result == null) {
            result = factory.getObject();
        
            sc.setAttribute(name, result);
        }
        
        return result;
    }

    /**
     * Removes bean from scope.
     */
    public Object remove(String name) {
        Object result = null;
        
        ServletContext sc = getServletContext();
        
        result = sc.getAttribute(name);
        
        sc.removeAttribute(name);

        return result;
    }

    /**
     * <p>Registers destruction callback.</p>
     * 
     * <p><strong>Note</strong>: Currently not implemented.</p>
     */
    public void registerDestructionCallback(String name, Runnable callback) {
        logger.warn("ServletContextScope does not support descruction callbacks");
    }

    /**
     * Resolve the contextual object for the given key, if any.
     * Which in this case will always be <code>null</code>.
     */
    public Object resolveContextualObject(String key) {
        return null;
    }

    /**
     * <p>Gets conversation id.</p>
     * 
     * <p><strong>Note</strong>: Currently not implemented.</p>
     */
    public String getConversationId() {
        return null;
    }

    /**
     * Gets current or cross context <code>ServletContext</code>.
     */
    protected ServletContext getServletContext() {
        ServletContext sc = null;
        
        if (StringUtils.hasText(context)) { 
            sc = servletContext.getContext(context);
            
            if (sc == null) {
                throw new UnsupportedOperationException("Unable to get context for '" + context + "'.  " +
                                                        "Server may not have cross context support or may be configured incorrectly.");
            }
        } else {
            sc = servletContext;
        }
        
        return sc;
    }
    
}