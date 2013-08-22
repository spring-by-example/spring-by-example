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
package org.springbyexample.contact.web.service.person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.FIRST_NAME;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.LAST_NAME;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.NEW_LAST_NAME;

import java.util.List;

import org.junit.Test;
import org.springbyexample.mvc.rest.service.PersistenceMarshallingService;
import org.springbyexample.schema.beans.person.Person;
import org.springbyexample.schema.beans.person.PersonFindResponse;
import org.springbyexample.schema.beans.person.PersonResponse;
import org.springbyexample.web.service.AbstractPersistenceControllerTest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests contact marshalling client through the embedded REST service 
 * with the main Spring context as the parent context.
 * 
 * @author David Winterfeldt
 */
public class ContactMarshallingServiceTest extends AbstractPersistenceControllerTest<PersonResponse, PersonFindResponse, Person> {

    @Autowired
    private ContactMarshallingService client = null;
    
    public ContactMarshallingServiceTest() {
        super(1, 3);
    }

    @Override
    protected PersistenceMarshallingService<PersonResponse, PersonFindResponse, Person> getClient() {
        return client;
    }

    @Override
    protected Person generateCreateRequest() {
        return new Person().withFirstName(FIRST_NAME).withLastName(LAST_NAME);
    }

    @Override
    protected Person generateUpdateRequest(Person request) {
        return request.withLastName(NEW_LAST_NAME);
    }

    @Override
    protected Person generateDeleteRequest() {
        return new Person().withId(id);
    }

    @Override
    protected void verifyRecord(Person record, boolean save, boolean update) {
        verifyPartialRecord(record, save, update);
        
        verifyAuditInfo(record.getLastUpdated(), record.getLastName(), record.getCreated(), record.getCreateUser());

        logger.debug("id=" + record.getId() + 
                     "  firstName=" + record.getFirstName() + 
                     "  lastName=" + record.getLastName() +
                     "  lastUpdated=" + record.getLastUpdated() +
                     "  lastUpdateUser=" + record.getLastUpdateUser() +
                     "  created=" + record.getCreated() +
                     "  createUser=" + record.getCreateUser());
    }

    protected void verifyPartialRecord(Person record, boolean save, boolean update) {
        assertNotNull("Result is null.", record);
        
        verifyPrimaryKey(record.getId(), save);
        
        String lastName = (update ? NEW_LAST_NAME : LAST_NAME);
        
        assertEquals("'firstName'", FIRST_NAME, record.getFirstName());
        assertEquals("'lastName'", lastName, record.getLastName());
    }
    
    protected void verifySmallRecord(Person record, boolean save) {
        verifyPartialRecord(record, save, false);
        
        assertNull("'lastUpdated' isn't null", record.getLastUpdated());
        assertNull("'lastUpdateUser' isn't null", record.getLastUpdateUser());
        assertNull("'created' isn't null", record.getCreated());
        assertNull("'createUser' isn't null", record.getCreateUser());
    }

    @Test
    public void testFindByLastName() {
        PersonFindResponse response = client.findByLastName(LAST_NAME);
        
        assertNotNull("Response is null.", response);
        
        List<Person> results = response.getResults();
        
        int expectedCount = 1;

        assertNotNull("Person list is null.", results);
        assertEquals("Number of persons should be " + expectedCount + ".", expectedCount, results.size());
        
        verifyRecord(results.get(0));
    }

    @Test
    public void testSmallFindByLastName() {
        PersonFindResponse response = client.smallFindByLastName(LAST_NAME);
        
        assertNotNull("Response is null.", response);
        
        List<Person> results = response.getResults();
        
        int expectedCount = 1;
        
        assertNotNull("Person list is null.", results);
        assertEquals("Number of persons should be " + expectedCount + ".", expectedCount, results.size());
        
        verifySmallRecord(results.get(0), false);
    }

    @Test
    public void testSmallFindById() {
        PersonResponse response = client.smallFindById(id);
        
        assertNotNull("Response is null.", response);

        verifySmallRecord(response.getResults(), false);
    }

    @Test
    public void testSmallPaginatedFind() {
        int page = 0;
        int pageSize = 2;
        
        PersonFindResponse response = client.smallFind(page, pageSize);
        assertNotNull("Response is null.", response);
        
        assertEquals("count", expectedCount, response.getCount());
        
        assertNotNull("Response results is null.", response.getResults());
        verifySmallRecord(response.getResults().get(0), false);
    }

//    @Override
//    public void testDeletePk() {}
    
}
