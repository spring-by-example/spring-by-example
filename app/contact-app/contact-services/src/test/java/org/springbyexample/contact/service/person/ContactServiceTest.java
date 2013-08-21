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
package org.springbyexample.contact.service.person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.*;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.FIRST_ID;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.FIRST_NAME;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.LAST_NAME;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.SECOND_ID;

import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.contact.service.AbstractServiceTest;
import org.springbyexample.schema.beans.entity.PkEntityBase;
import org.springbyexample.schema.beans.person.Person;
import org.springbyexample.schema.beans.person.PersonFindResponse;
import org.springbyexample.schema.beans.person.PersonResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests contact service.
 * 
 * @author David Winterfeldt
 */
public class ContactServiceTest extends AbstractServiceTest {

    final Logger logger = LoggerFactory.getLogger(ContactServiceTest.class);

    @Autowired
    private ContactService service;
        
    @Test
    public void testFindById() {
        PersonResponse response = service.findById(FIRST_ID);
        Person person = response.getResults();

        testPersonOne(person);
    }

    @Test
    public void testFindByLastName() {
        PersonFindResponse response = service.findByLastName(LAST_NAME);
        List<Person> results = response.getResults();
        
        int expectedCount = 1;

        assertNotNull("Person list is null.", results);
        assertEquals("Number of persons should be " + expectedCount + ".", expectedCount, results.size());
        
        Person person = response.getResults().get(0);
        
        testPersonOne(person);
    }
    
    @Test
    public void testFind() {
        PersonFindResponse response = service.find();
        assertNotNull("Person response is null.", response);
        
        Collection<Person> persons = response.getResults();

        assertNotNull("Person list is null.", persons);
        assertEquals("Number of persons should be " + EXPECTED_COUNT + ".", EXPECTED_COUNT, persons.size());
        
        for (Person person : persons) {
            logger.debug(person.toString());
            
            if (FIRST_ID.equals(person.getId())) {                
                testPersonOne(person);
            } else if (SECOND_ID.equals(person.getId())) {
                testPersonTwo(person);
            }
        }
    }

    @Test
    public void testCreate() {
        String firstName = "Jack";
        String lastName = "Johnson";
        
        PersonResponse response = createPerson(firstName, lastName);
        assertNotNull("Person response is null.", response);
        
        Person person = response.getResults();
        
        // test saved person
        testPerson(person, 
                   firstName, lastName);

        PersonFindResponse findResponse = service.find();
        assertNotNull("Person response is null.", findResponse);
        
        Collection<Person> persons = findResponse.getResults();

        int expectedCount = EXPECTED_COUNT + 1;
        
        assertNotNull("Person list is null.", persons);
        assertEquals("Number of persons should be " + expectedCount + ".", expectedCount, persons.size());
    }

    @Test
    public void testUpdate() {
        PersonResponse response = service.findById(FIRST_ID);
        assertNotNull("Person response is null.", response);
        
        Person person = response.getResults();
        
        testPersonOne(person);
        
        String lastName = "Jones"; 
        person.setLastName(lastName);

        service.update(person);

        response = service.findById(FIRST_ID);
        assertNotNull("Person response is null.", response);
        
        person = response.getResults();
        
        testPersonOne(person, lastName);
    }

    @Test
    public void testDelete() {
        service.delete(new Person().withId(FIRST_ID));

        // person should be null after delete
        PersonResponse response = service.findById(FIRST_ID);
        assertNotNull("Person response is null.", response);
        
        Person person = response.getResults();

        assertNull("Person is not null.", person);
    }

    /**
     * Create person.
     */
    private PersonResponse createPerson(String firstName, String lastName) {
        Person person = new Person();

        person.setFirstName(firstName);
        person.setLastName(lastName);
        
        PersonResponse response = service.create(person);
        
        return response;
    }
    
    /**
     * Tests person with a PK of one.
     */
    private void testPersonOne(Person person) {
        testPersonOne(person, LAST_NAME);
    }
    
    /**
     * Tests person with a PK of one.
     */
    private void testPersonOne(Person person, String lastName) {
        testPerson(person, 
                   FIRST_NAME, lastName);
    }

    /**
     * Tests person with a PK of two.
     */
    private void testPersonTwo(Person person) {
        String firstName = "John";
        String lastName = "Wilson";

        testPerson(person, 
                   firstName, lastName);
    }

    /**
     * Tests person.
     */
    private void testPerson(Person person, 
                            String firstName, String lastName) {
        assertNotNull("Person is null.", person);
        
        assertEquals("firstName", firstName, person.getFirstName());
        assertEquals("lastName", lastName, person.getLastName());
                
        testAuditable(person);
    }

    /**
     * Tests auditable entity.
     */
    private void testAuditable(PkEntityBase auditRecord) {
        assertNotNull("lastUpdated", auditRecord.getLastUpdated());
        assertNotNull("lastUpdatedBy", auditRecord.getLastUpdateUser());
        assertNotNull("created", auditRecord.getCreated());
        assertNotNull("createdBy", auditRecord.getCreateUser());
    }
 
}
