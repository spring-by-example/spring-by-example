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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests custom <code>ServletContext</code> scope.
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ServletContextScopeTest extends AbstractJUnit4SpringContextTests {

    final Logger logger = LoggerFactory.getLogger(ServletContextScopeTest.class);

    private static final String SIMPLE_FORM_CONTEXT_PATH = "/simple-form";
    private static final String ADVANCED_FORM_CONTEXT_PATH = "/advanced-form";
    
    private static final String CONTEXT_KEY = "custom";
    private static final String SIMPLE_FORM_CONTEXT_VALUE = "ServletContext scope";
    private static final String ADVANCED_FORM_CONTEXT_VALUE = "Cross Context scope";
    
    @Autowired
    private String testString = null;
    
    /**
     * Tests scope retrieval from Spring.
     */
    @Test
    public void testScopeRetrievalFromSpring() {
        assertNotNull("Test string is null.", testString);
        
        String expected = "spring";
        
        assertEquals("Test string should be '" + expected + "'.", expected, testString);
    }

    /**
     * Tests cross context scope retrieval.
     */
    @Test
    public void testCrossContextScopeRetrieval() {
        ServletContextScope scs = getServletContextScope(true);
        
        String value = (String)scs.get(CONTEXT_KEY, null);
        
        assertNotNull("Simple form context value is null.", value);
        
        String expected = SIMPLE_FORM_CONTEXT_VALUE;
        
        assertEquals("Simple form context value string should be '" + expected + "'.", expected, value);

        // switch context for ServletContextScope to be '/advanced-form'
        scs.setContext(ADVANCED_FORM_CONTEXT_PATH);
        
        value = (String)scs.get(CONTEXT_KEY, null);
        
        assertNotNull("Advanced form context value is null.", value);
        
        expected = ADVANCED_FORM_CONTEXT_VALUE;
        
        assertEquals("Advanced form context value string should be '" + expected + "'.", expected, value);
    }

    /**
     * Tests cross context scope get with creation.
     */
    @Test
    public void testCrossContextScopeGet() {
        ServletContextScope scs = getServletContextScope(false);
        
        String value = (String)scs.get(CONTEXT_KEY, new ObjectFactory() {
            public Object getObject() throws BeansException {
                return SIMPLE_FORM_CONTEXT_VALUE;
            }
        });
        
        String expected = SIMPLE_FORM_CONTEXT_VALUE;

        assertNotNull("Simple form context value is null.", value);
        assertEquals("Simple form context value string should be '" + expected + "'.", expected, value);

        // try again, but fail if the factory is called again.
        value = (String)scs.get(CONTEXT_KEY, new ObjectFactory() {
            public Object getObject() throws BeansException {
                fail("Bean should have been retrieved from ServletContext and not created again from the ObjectFactory.");
                
                return SIMPLE_FORM_CONTEXT_VALUE;
            }
        });

        assertNotNull("Simple form context value is null.", value);
        assertEquals("Simple form context value string should be '" + expected + "'.", expected, value);
    }

    /**
     * Gets <code>ServletContextScope</code> with two contexts configured.
     */
    protected ServletContextScope getServletContextScope(boolean configureAttributes) {
        // setup mock ServletContext
        MockServletContext simpleFormServletContext = new MockServletContext();
        simpleFormServletContext.setContextPath(SIMPLE_FORM_CONTEXT_PATH);
        
        MockServletContext advancedFormServletContext = new MockServletContext();
        advancedFormServletContext.setContextPath(ADVANCED_FORM_CONTEXT_PATH);
        
        if (configureAttributes) {
            simpleFormServletContext.setAttribute(CONTEXT_KEY, SIMPLE_FORM_CONTEXT_VALUE);
            advancedFormServletContext.setAttribute(CONTEXT_KEY, ADVANCED_FORM_CONTEXT_VALUE);
        }
        
        simpleFormServletContext.registerContext(ADVANCED_FORM_CONTEXT_PATH, advancedFormServletContext);
        
        ServletContextScope scs = new ServletContextScope();
        scs.setServletContext(simpleFormServletContext);

        return scs;
    }

}
