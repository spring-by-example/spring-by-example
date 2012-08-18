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
 * Tests <code>AnnotationLoggerBeanPostProcessorTest</code>.
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AnnotationLoggerBeanPostProcessorComponentTest {

    final Logger logger = LoggerFactory.getLogger(AnnotationLoggerBeanPostProcessorComponentTest.class);

    @Autowired
    protected AnnotationLoggerComponentBean annotationLoggerBean = null;

    /**
     * Tests field <code>AutowiredLogger</code>.
     */
    @Test
    public void testFieldAnnotationLogger() {
        assertNotNull("Annotation logger bean is null.", annotationLoggerBean);
        
        Logger beanLogger = annotationLoggerBean.getLogger();
        
        assertNotNull("Annotation bean's field logger is null.", beanLogger);
        
        beanLogger.debug("Logging message from annotation bean's field logger that was injected by AnnotationLoggerAwareBeanPostProcessorTest.");
    }

    /**
     * Tests method <code>AutowiredLogger</code>.
     */
    @Test
    public void testMethodAnnotationLogger() {
        assertNotNull("Annotation logger bean is null.", annotationLoggerBean);
        
        Logger beanLogger = annotationLoggerBean.getMethodLogger();
        
        assertNotNull("Annotation bean's method logger is null.", beanLogger);
        
        beanLogger.debug("Logging message from annotation bean's method logger that was injected by AnnotationLoggerAwareBeanPostProcessorTest.");
    }
    
}
