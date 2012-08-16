/*
 * Copyright 2007-2009 the original author or authors.
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

package org.springbyexample.web.service;

import org.springbyexample.web.jpa.bean.Person;
import org.springbyexample.web.jpa.dao.PersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;


/**
 * Person service.
 * 
 * @author David Winterfeldt
 */
@Service
@RemotingDestination
public class PersonService {

    private final PersonDao personDao;

    /**
     * Constructor
     */
    @Autowired
    public PersonService(PersonDao personDao) {
        this.personDao = personDao;
    }

    /**
     * <p>Deletes person.</p>
     * 
     * <p><strong>Note</strong>: Passing back the person 
     * from the Flex client causes a problem with Hibernate.</p>
     */
    public void remove(int id) {
        Person person = personDao.findPersonById(id);
        
        personDao.delete(person);
    }
    
}
