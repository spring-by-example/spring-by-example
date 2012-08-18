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

package org.springbyexample.di.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>Tests setter messsage injecting a reference.</p>
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ReferenceSetterMessageTest {

    final Logger logger = LoggerFactory.getLogger(ReferenceSetterMessageTest.class);

    @Autowired
    private SetterMessage message = null;

    /**
     * Tests message.
     */
    @Test
    public void testMessage() {   
        assertNotNull("Setter message instance is null.", message);
        
        String msg = message.getMessage();
        
        assertNotNull("Message is null.", msg);
        
        String expectedMessage = "Spring is fun.";
        
        assertEquals("Message should be '" + expectedMessage + "'.", expectedMessage, msg);

        logger.info("message='{}'", msg);
    }
    
}
