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
package org.springbyexample.web.service.person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.schema.beans.person.Person;
import org.springbyexample.schema.beans.person.PersonResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Person form controller.
 * 
 * @author David Winterfeldt
 */
@Controller
public class PersonController implements PersonMarshallingService {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * Find by primary key.
     */
    @Override
    @RequestMapping(value = FIND_BY_ID_REQUEST, method = RequestMethod.GET)
    public PersonResponse findById(@PathVariable(ID_VAR) long id) {
        logger.info("Find person.  id={}", id);

        return  new PersonResponse().withResult(new Person().withId(1).withFirstName("Joe").withLastName("Smith"));
    }
    
}
