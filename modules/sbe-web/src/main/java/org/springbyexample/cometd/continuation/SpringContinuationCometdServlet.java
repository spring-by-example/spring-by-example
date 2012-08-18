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

package org.springbyexample.cometd.continuation;

import java.util.Map;

import javax.servlet.ServletException;

import org.mortbay.cometd.AbstractBayeux;
import org.mortbay.cometd.continuation.ContinuationCometdServlet;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * A continuation comet servlet meant to be used along with 
 * {@link SpringContinuationBayeux}.  This servlet overrides the 
 * default intialization of the bayeux instance because the 
 * {@link SpringContinuationBayeux} fully initializes itself using 
 * a Spring configuration. 
 * 
 * @author David Winterfeldt
 */
public class SpringContinuationCometdServlet extends ContinuationCometdServlet {

    private static final long serialVersionUID = 6186673799799707142L;

    /**
     * <p>Gets bayeux instance configured by Spring
     * and sets <code>asyncDeliver</code> from the servlet init parameters.</p>
     * 
     * <p>Throws a <code>ServletException</code> if it can't find 
     * a unique Spring bean of the type <code>AbstractBayeux</code>.</p>
     * 
     * <p>Overrides <code>AbstractCometdServlet.init()</code> because 
     * {@link SpringContinuationBayeux} handles it's own initialization.</p>
     */
    @Override
    @SuppressWarnings("unchecked")
    public void init() throws ServletException {
        synchronized (SpringContinuationCometdServlet.class) {
            Map<String, AbstractBayeux> hBayeuxBeans = (Map<String, AbstractBayeux>)WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBeansOfType(AbstractBayeux.class);
            
            if (hBayeuxBeans == null || hBayeuxBeans.size() != 1) {
                throw new ServletException("Unable to find a unique Spring bean of the type AbstractBayeux.");
            }
            
            for (AbstractBayeux bayeux : hBayeuxBeans.values()) {
                _bayeux = bayeux;
                break;
            }            
        }
        
        String async = getInitParameter("asyncDeliver");

        if (async!=null) {
            _asyncDeliver = Boolean.parseBoolean(async);
        }
    }
    
}
