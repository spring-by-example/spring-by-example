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

package org.springbyexample.jms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests that the generated messages were all processed by the JMS listener.
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JmsMessageListenerTest {

    final Logger logger = LoggerFactory.getLogger(JmsMessageListenerTest.class);

    @Autowired
    private AtomicInteger counter = null;
    
    @Test
    public void testMessage() throws Exception {
        assertNotNull("Counter is null.", counter);

        int expectedCount = 100;
        
        logger.info("Testing...");
        
        // give listener a chance to process messages
        Thread.sleep(2 * 1000);
        
        assertEquals("Message is not '" + expectedCount + "'.", expectedCount, counter.get());
    }
    
}
