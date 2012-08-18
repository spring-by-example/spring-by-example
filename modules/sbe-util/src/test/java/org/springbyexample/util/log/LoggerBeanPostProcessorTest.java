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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests <code>LoggerBeanPostProcessor</code>.
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class LoggerBeanPostProcessorTest {

    final Logger logger = LoggerFactory.getLogger(LoggerBeanPostProcessorTest.class);

    @Autowired
    protected LoggerBean loggerBean = null;

    /**
     * Tests <code>Slf4JLoggerAware</code>.
     */
    @Test
    public void testLogger() {
        assertNotNull("SLF4J logger bean is null.", loggerBean);
        
        Logger beanLogger = loggerBean.getCustomLogger();
        
        assertNotNull("SLF4J bean's logger is null.", beanLogger);
        
        beanLogger.debug("Logging message from SLF4J bean's logger that was injected by LoggerBeanPostProcessor.");
    }

}
