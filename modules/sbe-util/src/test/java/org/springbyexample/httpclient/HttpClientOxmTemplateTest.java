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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.schema.beans.Person;
import org.springbyexample.schema.beans.Persons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests <code>HttpClientOxmTemplate</code>.
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class HttpClientOxmTemplateTest {

    final Logger logger = LoggerFactory.getLogger(HttpClientOxmTemplateTest.class);
 
    protected final static String HOST = "http://localhost";
    protected final static int PORT = 8093;
    protected final static String SERVLET_MAPPING = "/test";
    
    protected final static Integer ID = new Integer(1);
    protected final static String FIRST_NAME = "Joe";
    protected final static String LAST_NAME = "Smith";

    protected final static Integer ID_RESULT = new Integer(3);
    protected final static String FIRST_NAME_RESULT = "John";
    protected final static String LAST_NAME_RESULT = "Jackson";

    protected final static String POST_DATA_RESULT = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ns2:Persons xmlns:ns2=\"http://www.springbyexample.org/schema/beans\"><person><id>3</id><first-name>John</first-name><last-name>Jackson</last-name></person></ns2:Persons>";
    
    protected static Server server = null;
    
    @Autowired
    protected HttpClientOxmTemplate<Persons> template = null;

    /**
     * Initialize class before any tests run.
     */
    @BeforeClass
    public static void init() throws Exception {
        server = new Server();

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(PORT);
        server.setConnectors(new Connector[]{connector});

        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(new ServletHolder(new ProcessOxmServlet()), SERVLET_MAPPING);
        server.setHandler(servletHandler);

        server.start();
    }

    /**
     * Initialize class before any tests run.
     */
    @AfterClass
    public static void destroy() throws Exception {
        server.stop();
    }

    /**
     * Tests template's post method sending <code>String</code> data and 
     * ignoring the response.
     */ 
    @Test
    public void testStringDataPostMethodIgnoreResponse() {
        assertNotNull("HttpClientOxmTemplate is null.", template);

        Persons persons = new Persons();
        
        Person person = new Person();
        person.setId(ID);
        person.setFirstName(FIRST_NAME);
        person.setLastName(LAST_NAME);
        
        persons.getPerson().add(person);
        
        template.executePostMethod(persons);
    }

    /**
     * Tests template's post method sending <code>String</code> data and 
     * with a <code>String</code> response.
     */ 
    @Test
    public void testStringDataPostMethodWithStringResponse() {
        assertNotNull("HttpClientOxmTemplate is null.", template);

        Persons persons = new Persons();
        
        Person person = new Person();
        person.setId(ID);
        person.setFirstName(FIRST_NAME);
        person.setLastName(LAST_NAME);
        
        persons.getPerson().add(person);
        
        template.executePostMethod(persons, 
            new ResponseCallback<Persons>() {
                public void doWithResponse(Persons persons) throws IOException {
                    assertNotNull("Persons result is null.", persons);
                    assertNotNull("Person list is null.", persons.getPerson());
    
                    int expectedCount = 1;
                    
                    assertEquals("Person list size should be '" + expectedCount + "'.", expectedCount, persons.getPerson().size());
                    
                    Person result = persons.getPerson().get(0);
                    
                    assertEquals("Person id should be '" + ID_RESULT + "'.", ID_RESULT, new Integer(result.getId()));
                    assertEquals("Person first name should be '" + FIRST_NAME_RESULT + "'.", FIRST_NAME_RESULT, result.getFirstName());
                    assertEquals("Person last name should be '" + LAST_NAME_RESULT + "'.", LAST_NAME_RESULT, result.getLastName());
    
                    logger.debug("id={}  firstName={}  lastName={}", 
                                 new Object[] { result.getId(), 
                                                result.getFirstName(), 
                                                result.getLastName()});
                }
        });
    }
    
}
