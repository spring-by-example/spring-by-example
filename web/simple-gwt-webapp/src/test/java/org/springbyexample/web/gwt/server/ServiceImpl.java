/*
 * Copyright 2002-2006 the original author or authors.
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
import java.util.List;

import org.springbyexample.web.gwt.client.Service;
import org.springbyexample.web.gwt.client.bean.Person;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Service implemetation used for testing with GWT Eclispe IDE.
 * 
 * @author Davivd Winterfeldt
 */
public class ServiceImpl extends RemoteServiceServlet implements Service {
    
    final List<Person> lPersons = new ArrayList<Person>();

    /**
     * Constructor
     */
    public ServiceImpl() {
        for (int i = 1; i < 100; i++) {
            Person person = new Person();
            person.setId(i);
            person.setFirstName("Joe");
            person.setLastName("Smith the " + i);
            
            lPersons.add(person);
        }
    }

    /**
     * Finds person within a range.
     */
    public Person[] findPersons(int startIndex, int maxResults) {
        Person[] results = null;
        int endIndex = (startIndex + maxResults);

        if (startIndex == 0 && endIndex > lPersons.size()) {
            results = lPersons.toArray(new Person[]{});
        } else if (startIndex >= 0 && endIndex < lPersons.size()) {
            results = lPersons.subList(startIndex, endIndex).toArray(new Person[]{});
        } else if (startIndex >= 0 && endIndex > lPersons.size()) {
            results = lPersons.subList(startIndex, lPersons.size()).toArray(new Person[]{});
        } else {
            results = new Person[] {};
        }
        
        return results;
    }
    
}
