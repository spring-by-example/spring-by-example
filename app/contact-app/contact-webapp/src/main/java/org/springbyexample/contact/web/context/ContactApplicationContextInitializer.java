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
package org.springbyexample.contact.web.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;


/**
 * Custom <code>ApplicationContextInitializer</code> used to 
 * configure Spring profiles.
 * 
 * @author David Winterfeldt
 */
public class ContactApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final String SPRING_PROFILES_ACTIVE_PROPERTY = "spring.profiles.active";
    
    private static final String PROFILE_HSQL = "hsql";
    
    private final static String [] DEFAULT_ACTIVE_PROFILES = { PROFILE_HSQL };
    
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        String springProfilesActive = System.getProperty(SPRING_PROFILES_ACTIVE_PROPERTY);
        
        if (StringUtils.hasText(springProfilesActive)) {
            logger.info("Using set spring profiles.  profiles='{}'", springProfilesActive);
        } else {
            applicationContext.getEnvironment().setActiveProfiles(DEFAULT_ACTIVE_PROFILES);
            
            logger.info("Setting default spring profiles.  profiles='{}'", DEFAULT_ACTIVE_PROFILES);
        }        
    }

}
