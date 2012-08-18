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
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

/**
 * Tests <code>HttpClientTemplate</code>.
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class HttpClientTemplateTest {

    final Logger logger = LoggerFactory.getLogger(HttpClientTemplateTest.class);

    protected final static String HOST = "http://localhost";
    protected final static int PORT = 8093;
    protected final static String SERVLET_MAPPING = "/test";
    
    protected final static String GET_RESULT = "<html><head><title>Get Test</title></head><body>Get Test</body></html>";
    protected final static String GET_PARAM_INPUT_KEY = "message";
    protected final static String GET_PARAM_INPUT_VALUE = "Greetings from get method.";
    protected final static String GET_PARAM_RESULT = "<message>Greetings from get method.</message>";
    
    protected final static String LOWER_PARAM = "lower";
    protected final static String POST_NAME = "Joe Smith";
    protected final static String POST_LOWER_RESULT = "<result>joe smith</result>";
    protected final static String POST_DATA_INPUT = "<message>Greetings</message>";
    protected final static String POST_DATA_RESULT = "<success/>";

    protected static Server server = null;
    
    @Autowired
    protected HttpClientTemplate template = null;

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
        servletHandler.addServletWithMapping(new ServletHolder(new ProcessServlet()), SERVLET_MAPPING);
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
     * Tests template's get method with an <code>byte[]</code> response.
     */
    @Test
    public void testGetMethodWithByteResponse() {
        assertNotNull("HttpClientTemplate is null.", template);

        template.executeGetMethod(new ResponseByteCallback() {
            public void doWithResponse(byte[] byteResponse) throws IOException {
                String response = new String(byteResponse);
                
                assertNotNull("Response is null.", response);
                assertTrue("Response has body.", StringUtils.hasText(response));

                assertEquals("Response does not match expected result.", GET_RESULT, response);
                
                logger.debug("HTTP Get byte response. '{}'", response);
            }
        });
    }
    
    /**
     * Tests template's get method with an <code>InputStream</code> response.
     */
    @Test
    public void testGetMethodWithStreamResponse() {
        assertNotNull("HttpClientTemplate is null.", template);

        template.executeGetMethod(new ResponseStreamCallback() {
            public void doWithResponse(InputStream in) throws IOException {
                String response = IOUtils.toString(in);
                
                assertNotNull("Response is null.", response);
                assertTrue("Response has body.", StringUtils.hasText(response));

                assertEquals("Response does not match expected result.", GET_RESULT, response);
                
                logger.debug("HTTP Get stream response. '{}'", response);
            }
        });
    }
    
    /**
     * Tests template's get method with a <code>String</code> response.
     */
    @Test
    public void testGetMethodWithStringResponse() {
        assertNotNull("HttpClientTemplate is null.", template);

        template.executeGetMethod(new ResponseStringCallback() {
            public void doWithResponse(String response) throws IOException {
                assertNotNull("Response is null.", response);                
                assertTrue("Response has body.", StringUtils.hasText(response));
                
                assertEquals("Response does not match expected result.", GET_RESULT, response);
                
                logger.debug("HTTP Get string response. '{}'", response);
            }
        });
    }

    /**
     * Tests template's get method with parameters and 
     * with a <code>String</code> response.
     */
    @Test
    public void testGetMethodWithParamsWithStringResponse() {
        assertNotNull("HttpClientTemplate is null.", template);

        Map<String, String> hParams = new HashMap<String, String>();
        hParams.put(GET_PARAM_INPUT_KEY, GET_PARAM_INPUT_VALUE);
        
        template.executeGetMethod(hParams, 
            new ResponseStringCallback() {
                public void doWithResponse(String response) throws IOException {
                    assertNotNull("Response is null.", response);                
                    assertTrue("Response has body.", StringUtils.hasText(response));
                    
                    assertEquals("Response does not match expected result.", 
                                 GET_PARAM_RESULT, response);
                    
                    logger.debug("HTTP Get with params string response. '{}'", response);
                }
        });
    }
    
    /**
     * Tests template's post method with a <code>byte[]</code> response.
     */ 
    @Test
    public void testPostMethodWithByteResponse() {
        assertNotNull("HttpClientTemplate is null.", template);

        Map<String, String> hParams = new HashMap<String, String>();
        hParams.put(LOWER_PARAM, POST_NAME);

        template.executePostMethod(hParams,
            new ResponseByteCallback() {
                public void doWithResponse(byte[] byteResponse) throws IOException {
                    String response = new String(byteResponse);
                    
                    assertNotNull("Response is null.", response);
                    assertTrue("Response has body.", StringUtils.hasText(response));
    
                    assertEquals("Response does not match expected result.", POST_LOWER_RESULT, response);
    
                    logger.debug("HTTP Post byte response. '{}'", response);
                }
        });
    }

    /**
     * Tests template's post method with an <code>InputStream</code> response.
     */ 
    @Test
    public void testPostMethodWithStreamResponse() {
        assertNotNull("HttpClientTemplate is null.", template);

        Map<String, String> hParams = new HashMap<String, String>();
        hParams.put(LOWER_PARAM, POST_NAME);

        template.executePostMethod(hParams,
            new ResponseStreamCallback() {
                public void doWithResponse(InputStream in) throws IOException {
                    String response = IOUtils.toString(in);
                    
                    assertNotNull("Response is null.", response);
                    assertTrue("Response has body.", StringUtils.hasText(response));
    
                    assertEquals("Response does not match expected result.", POST_LOWER_RESULT, response);
    
                    logger.debug("HTTP Post stream response. '{}'", response);
                }
        });
    }
    
    /**
     * Tests template's post method with a <code>String</code> response.
     */ 
    @Test
    public void testPostMethodWithStringResponse() {
        assertNotNull("HttpClientTemplate is null.", template);

        Map<String, String> hParams = new HashMap<String, String>();
        hParams.put(LOWER_PARAM, POST_NAME);

        template.executePostMethod(hParams, 
            new ResponseStringCallback() {
                public void doWithResponse(String response) throws IOException {
                    assertNotNull("Response is null.", response);
                    assertTrue("Response has body.", StringUtils.hasText(response));
    
                    assertEquals("Response does not match expected result.", POST_LOWER_RESULT, response);
    
                    logger.debug("HTTP Post string response. '{}'", response);
                }
        });
    }

    /**
     * Tests template's post method sending <code>String</code> data and 
     * with a <code>String</code> response.
     */ 
    @Test
    public void testStringDataPostMethodWithStringResponse() {
        assertNotNull("HttpClientTemplate is null.", template);

        Map<String, String> hParams = new HashMap<String, String>();
        hParams.put(LOWER_PARAM, POST_NAME);
        
        template.executePostMethod(POST_DATA_INPUT,
            new ResponseStringCallback() {
                public void doWithResponse(String response) throws IOException {
                    assertNotNull("Response is null.", response);
                    assertTrue("Response has body.", StringUtils.hasText(response));
    
                    assertEquals("Response does not match expected result.", POST_DATA_RESULT, response);
    
                    logger.debug("HTTP string data post string response. '{}'", response);
                }
        });
    }

    /**
     * Tests template's post method sending <code>String</code> data and 
     * with a <code>String</code> response.
     */ 
    @Test
    public void testStreamDataPostMethodWithStringResponse() {
        assertNotNull("HttpClientTemplate is null.", template);

        Map<String, String> hParams = new HashMap<String, String>();
        hParams.put(LOWER_PARAM, POST_NAME);
        
        template.executePostMethod(new ByteArrayInputStream(POST_DATA_INPUT.getBytes()),
            new ResponseStringCallback() {
                public void doWithResponse(String response) throws IOException {
                    assertNotNull("Response is null.", response);
                    assertTrue("Response has body.", StringUtils.hasText(response));
    
                    assertEquals("Response does not match expected result.", POST_DATA_RESULT, response);
    
                    logger.debug("HTTP stream data post string response. '{}'", response);
                }
        });
    }
    
}
