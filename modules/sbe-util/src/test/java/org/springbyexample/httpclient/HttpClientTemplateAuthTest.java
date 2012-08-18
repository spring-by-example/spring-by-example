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

package org.springbyexample.httpclient;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.jetty.security.ConstraintMapping;
import org.mortbay.jetty.security.HashUserRealm;
import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests <code>HttpClientTemplate</code> with 
 * authorization credentials.
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class HttpClientTemplateAuthTest extends HttpClientTemplateTest{

    final Logger logger = LoggerFactory.getLogger(HttpClientTemplateAuthTest.class);

    protected final static String AUTH_SERVLET_MAPPING = "/admin/test";
    
    /**
     * Initialize class before any tests run.
     */
    @BeforeClass
    public static void init() throws Exception {
        server = new Server();

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(PORT);
        server.setConnectors(new Connector[]{connector});

        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setRoles(new String[]{ "user", "admin" });
        constraint.setAuthenticate(true);

        ConstraintMapping cm = new ConstraintMapping();
        cm.setConstraint(constraint);
        cm.setPathSpec("/admin/*");

        HashUserRealm hashRealm = new HashUserRealm();
        hashRealm.put("jsmith", "password");
        hashRealm.addUserToRole("jsmith", "admin");

        SecurityHandler sh = new SecurityHandler();
        sh.setUserRealm(hashRealm);
        sh.setConstraintMappings(new ConstraintMapping[]{cm});
        
        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(
                new ServletHolder(new ProcessServlet()), AUTH_SERVLET_MAPPING);

        server.setHandlers(new Handler[]{ sh, servletHandler });
        //server.setHandler(servletHandler);

        server.start();
    }
    
}
