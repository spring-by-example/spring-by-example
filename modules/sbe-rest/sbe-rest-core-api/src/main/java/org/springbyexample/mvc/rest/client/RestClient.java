/*
 * Copyright 2007-2013 the original author or authors.
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

import javax.annotation.PostConstruct;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


/**
 * REST client.
 *
 * @author David Winterfeldt
 */
@Component
public class RestClient {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final RestTemplate template;
    private final RestClientProperties clientProperties;
    private final DefaultHttpClient httpClient;

    @Autowired
    public RestClient(RestTemplate template, RestClientProperties clientProperties,
                      DefaultHttpClient httpClient) {
        this.template = template;
        this.clientProperties = clientProperties;
        this.httpClient = httpClient;
    }

    @PostConstruct
    public void init() {
        setCredentials(clientProperties.getUsername(), clientProperties.getPassword());
    }

    /**
     * Gets rest template.
     */
    public RestTemplate getRestTemplate() {
        return template;
    }

    /**
     * Creates URL based on the URI passed in.
     */
    public String createUrl(String uri) {
        StringBuilder sb = new StringBuilder();

        sb.append(clientProperties.getUrl());
        sb.append(clientProperties.getApiPath());
        sb.append(uri);

        logger.debug("URL is '{}'.", sb.toString());

        return sb.toString();
    }

    /**
     * Set default credentials on HTTP client.
     */
    public void setCredentials(String userName, String password) {
        UsernamePasswordCredentials creds =
                new UsernamePasswordCredentials(clientProperties.getUsername(), clientProperties.getPassword());
        AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);

        httpClient.getCredentialsProvider().setCredentials(authScope, creds);
    }

}
