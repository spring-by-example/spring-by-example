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
package org.springbyexample.web.client.person;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.schema.beans.person.PersonResponse;
import org.springbyexample.web.client.RestClient;
import org.springbyexample.web.service.person.PersonMarshallingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Person client.
 * 
 * @author David Winterfeldt
 */
@Component
public class PersonClient implements PersonMarshallingService {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final RestClient client;

    @Autowired
    public PersonClient(RestClient client) {
        this.client = client;
    }

    @Override
    public PersonResponse findById(long id) {
        PersonResponse response = null;
        
        String url = client.getUrl(FIND_BY_ID_REQUEST);
        
        logger.debug("REST client findById.  id={}  url='{}'", id, url);
        
        Map<String, Long> vars = Collections.singletonMap(ID_VAR, id);
        
        response = client.getRestTemplate().getForObject(url, PersonResponse.class, vars);
        
        return response;
    }

    
}
