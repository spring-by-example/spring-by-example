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
package org.springbyexample.mvc.rest.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * REST client properties.
 * 
 * @author David Winterfeldt
 */
@Component
public class RestClientProperties {

    @Value("#{ restProperties['ws.api'] }") 
    private String apiPath;

    @Value("#{ systemProperties['ws.url'] != null ? systemProperties['ws.url'] : restProperties['ws.url'] }") 
    private String url;

    @Value("#{ systemProperties['ws.username'] != null ? systemProperties['ws.username'] : restProperties['ws.username'] }")
    private String username;
    
    @Value("#{ systemProperties['ws.password'] != null ? systemProperties['ws.password'] : restProperties['ws.password'] }")
    private String password;   

    /**
     * Gets base URI for the REST APIs.
     */
    public String getApiPath() {
        return apiPath;
    }

    /**
     * Gets URL.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets password.
     */
    public String getPassword() {
        return password;
    }
    
}
