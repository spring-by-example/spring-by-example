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

package org.springbyexample.util.log;

import static org.junit.Assert.assertNotNull;

import org.apache.commons.logging.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests <code>LoggerAwareBeanPostProcessorTest</code>.
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class LoggerAwareBeanPostProcessorTest {

    final Logger logger = LoggerFactory.getLogger(LoggerAwareBeanPostProcessorTest.class);

    @Autowired
    protected Slf4JLoggerBean slf4jLoggerBean = null;

    @Autowired
    protected CommonsLoggerBean commonsLoggerBean = null;

    @Autowired
    protected Log4JLoggerBean log4jLoggerBean = null;

    @Autowired
    protected JdkLoggerBean jdkLoggerBean = null;

    /**
     * Tests <code>Slf4JLoggerAware</code>.
     */
    @Test
    public void testSlf4JAwareLogger() {
        assertNotNull("SLF4J logger bean is null.", slf4jLoggerBean);
        
        Logger beanLogger = slf4jLoggerBean.getLogger();
        
        assertNotNull("SLF4J bean's logger is null.", beanLogger);
        
        beanLogger.debug("Logging message from SLF4J bean's logger that was injected by LoggerAwareBeanPostProcessorTest.");
    }

    /**
     * Tests <code>CommonsLoggerAware</code>.
     */
    @Test
    public void testCommonsAwareLogger() {
        assertNotNull("Commons logger bean is null.", commonsLoggerBean);
        
        Log beanLogger = commonsLoggerBean.getLogger();
        
        assertNotNull("Commons bean's logger is null.", commonsLoggerBean);
        
        beanLogger.debug("Logging message from Commons bean's logger that was injected by LoggerAwareBeanPostProcessorTest.");
    }

    /**
     * Tests <code>Log4JLoggerAware</code>.
     */
    @Test
    public void testLog4JAwareLogger() {
        assertNotNull("Log4J logger bean is null.", log4jLoggerBean);
        
        org.apache.log4j.Logger beanLogger = log4jLoggerBean.getLogger();
        
        assertNotNull("Log4J bean's logger is null.", log4jLoggerBean);
        
        beanLogger.debug("Logging message from Log4J bean's logger that was injected by LoggerAwareBeanPostProcessorTest.");
    }

    /**
     * Tests <code>JdkJLoggerAware</code>.
     */
    @Test
    public void testJdkAwareLogger() {
        assertNotNull("JDK 1.4 logger bean is null.", jdkLoggerBean);
        
        java.util.logging.Logger beanLogger = jdkLoggerBean.getLogger();
        
        assertNotNull("JDK 1.4 bean's logger is null.", jdkLoggerBean);

        beanLogger.info("Logging message from JDK 1.4 bean's logger that was injected by LoggerAwareBeanPostProcessorTest.");
    }

}
