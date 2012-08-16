/*
 * Copyright 2002-2008 the original author or authors.
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

package org.springbyexample.web.gwt.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.orm.hibernate3.annotation.dao.PersonDao;
import org.springbyexample.web.gwt.client.Service;
import org.springbyexample.web.gwt.client.bean.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


/**
 * Service controller implementation
 * 
 * @author David Winterfeldt
 */
@Controller
public class ServiceController extends GwtController implements Service {

    final Logger logger = LoggerFactory.getLogger(ServiceController.class);

    private static final long serialVersionUID = -2103209407529882816L;

    @Autowired
    private PersonDao personDao = null;
    
    /**
     * Finds person within a range.
     */
    public Person[] findPersons(int startIndex, int maxResults) {
        Person[] results = null;

        List<Person> lResults = new ArrayList<Person>();

        Collection<org.springbyexample.orm.hibernate3.annotation.bean.Person> lPersons = personDao.findPersons(startIndex, maxResults);
        
        for (org.springbyexample.orm.hibernate3.annotation.bean.Person person : lPersons) {
            Person result = new Person();
            result.setId(person.getId());
            result.setFirstName(person.getFirstName());
            result.setLastName(person.getLastName());
            
            lResults.add(result);
        }

        return lResults.toArray(new Person[]{});
    }

}
