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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>Runs processor for advice.</p>
 * 
 * <p><strong>Note</strong>: Must specify the javaagent when running 
 * this class (ex: 
 * -javaagent:~/.m2/repository/org/springframework/org.springframework.instrument/3.0.0.RELEASE/org.springframework.instrument-3.0.0.RELEASE.jar).</p>
 * 
 * @author David Winterfeldt
 */
public class ProcessorRunner { 

    final Logger logger = LoggerFactory.getLogger(ProcessorRunner.class);

    /**
     * Runs processor after initializing Spring so AspectJ LTW is initialized.
     */
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/application-context.xml");
        
        Processor processor = new Processor();
        
        processor.process();
    }

}
