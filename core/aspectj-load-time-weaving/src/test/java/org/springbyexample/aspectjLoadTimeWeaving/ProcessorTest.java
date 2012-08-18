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

package org.springbyexample.aspectjLoadTimeWeaving;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>Tests processor for advice.</p>
 * 
 * <p>Not the best test since it can only be seen in logging output.</p>
 * 
 * <p><strong>Note</strong>: Must specify the javaagent when running 
 * this class (ex: 
 * -javaagent:~/.m2/repository/org/springframework/org.springframework.instrument/3.0.0.RELEASE/org.springframework.instrument-3.0.0.RELEASE.jar).</p>
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/application-context.xml"})
public class ProcessorTest { 

    final Logger logger = LoggerFactory.getLogger(ProcessorTest.class);

    @Autowired
    private Processor processor = null;
    
    /**
     * Tests processor from the Spring context.
     */
    @Test
    public void testProcessorFromContext() {
        assertNotNull("Processor is null.", processor);
        
        logger.debug("Running processor from Spring context.");
        
        processor.process();
    }

    /**
     * Tests processor created outside the Spring context.
     */
    @Test
    public void testProcessor() {
        Processor processor = new Processor();
                
        logger.debug("Running processor from outside the Spring context.");
        
        processor.process();
    }

}
