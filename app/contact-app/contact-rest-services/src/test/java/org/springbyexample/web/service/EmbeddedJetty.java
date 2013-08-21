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
package org.springbyexample.web.service;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.apache.commons.lang.ArrayUtils;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.FilterHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * Spring bean that creates an embedded jetty server.
 * 
 * @author David Winterfeldt
 */
public class EmbeddedJetty {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final static String[] DEFAULT_ACTIVE_PROFILES = { "hsql" };
    private final static String[] DEFAULT_CONFIG_LOCATIONS = new String[] { "/embedded-jetty-context.xml" };
    private final static String SECURITY_FILTER_NAME = "springSecurityFilterChain";

    private final String[] activeProfiles;
    private final String[] configLocations;

    @Value("#{ restProperties['ws.port'] }")
    private int port;
    
    @Value("#{ restProperties['ws.context.path'] }")
    private String contextPath;

    private ApplicationContext ctx;

    /**
     * Creates an instance with default active profiles and Spring contexts.
     */
    public EmbeddedJetty() {
        this.activeProfiles = DEFAULT_ACTIVE_PROFILES;
        this.configLocations = DEFAULT_CONFIG_LOCATIONS;
    }

    /**
     * Creates an instance with the specified Spring contexts.
     */
    public EmbeddedJetty(String... configLocations) {
        this.activeProfiles = DEFAULT_ACTIVE_PROFILES;
        this.configLocations = configLocations;
    }

    /**
     * Creates an instance with the specified active profiles and Spring contexts.
     */
    public EmbeddedJetty(String[] activeProfiles, String[] configLocations) {
        this.activeProfiles = activeProfiles;
        this.configLocations = configLocations;
    }

    /**
     * Gets application context.
     */
    public ApplicationContext getApplicationContext() {
        return ctx;
    }

    @PostConstruct
    public void init() throws Exception {
        ctx = new ClassPathXmlApplicationContext();
        ((AbstractApplicationContext)ctx).getEnvironment().setActiveProfiles(activeProfiles);
        ((AbstractXmlApplicationContext)ctx).setConfigLocations(configLocations);
        
        if (logger.isInfoEnabled()) {
            logger.info("Creating embedded jetty context.  activeProfiles='{}'  configLocations='{}'",
                        new Object[] { ArrayUtils.toString(activeProfiles), ArrayUtils.toString(configLocations) });
        }
        
        ((AbstractXmlApplicationContext)ctx).refresh();

        ((AbstractApplicationContext)ctx).registerShutdownHook();
        
        Server server = (Server) ctx.getBean("jettyServer");

        if (port > 0) {
            Connector connector = server.getConnectors()[0];
            connector.setPort(port);
        }

        ServletContext servletContext = null;
        
        for (Handler handler : server.getHandlers()) {
            if (handler instanceof Context) {
                Context context = (Context) handler;

                if (StringUtils.hasText(contextPath)) {
                    context.setContextPath("/" + contextPath);
                }

                servletContext = context.getServletContext();
                
                // setup Spring Security Filter
                FilterHolder filterHolder = new FilterHolder();
                filterHolder.setName(SECURITY_FILTER_NAME);
                filterHolder.setClassName(DelegatingFilterProxy.class.getName());

                context.getServletHandler().addFilterWithMapping(filterHolder, "/*", 0);
                
                break;                
            }
        }

        XmlWebApplicationContext wctx = new XmlWebApplicationContext();
        wctx.setParent(ctx);
        wctx.setConfigLocation("");
        wctx.setServletContext(servletContext);
        wctx.refresh();
        
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, wctx);

        server.start();
        
        logger.info("Server started.");
    }
    
}
