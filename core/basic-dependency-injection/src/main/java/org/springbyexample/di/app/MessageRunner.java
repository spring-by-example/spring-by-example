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

package org.springbyexample.di.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Message application.
 * 
 * @author David Winterfeldt
 */
public class MessageRunner {

    final static Logger logger = LoggerFactory.getLogger(MessageRunner.class);
    
    /**
     * Main method.
     */
    public static void main(String[] args) {
        logger.info("Initializing Spring context.");
        
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/application-context.xml");
        
        logger.info("Spring context initialized.");

        Message message = (Message) applicationContext.getBean("message");

        logger.debug("message='" + message.getMessage() + "'");
    }

}
