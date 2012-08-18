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

import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Consumes messages from a JMS queue.
 * 
 * @author David Winterfeldt
 */
@Component
public class JmsMessageListener implements MessageListener { 

    private static final Logger logger = LoggerFactory.getLogger(JmsMessageListener.class);

    @Autowired
    private AtomicInteger counter = null;

    /**
     * Implementation of <code>MessageListener</code>.
     */
    public void onMessage(Message message) {
        try {   
            int messageCount = message.getIntProperty(JmsMessageProducer.MESSAGE_COUNT);
            
            if (message instanceof TextMessage) {
                TextMessage tm = (TextMessage)message;
                String msg = tm.getText();
                
                
                logger.info("Processed message '{}'.  value={}", msg, messageCount);
                
                counter.incrementAndGet();
            }
        } catch (JMSException e) {
            logger.error(e.getMessage(), e);
        }
    }
    
}
