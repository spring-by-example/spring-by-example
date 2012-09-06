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
package org.springbyexample.person.web.service.person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.springbyexample.person.web.client.person.PersonClient;
import org.springbyexample.person.web.service.AbstractPersistenceControllerTest;
import org.springbyexample.person.web.service.PersistenceMarshallingService;
import org.springbyexample.schema.beans.person.Person;
import org.springbyexample.schema.beans.person.PersonFindResponse;
import org.springbyexample.schema.beans.person.PersonResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests person client against an embedded REST service 
 * with the main Spring context as the parent context.
 * 
 * @author David Winterfeldt
 */
public class PersonControllerTest extends AbstractPersistenceControllerTest<PersonResponse, PersonFindResponse, Person> {

    final static Integer ID = 1;
    final static String FIRST_NAME = "Joe";
    final static String LAST_NAME = "Smith";

    @Autowired
    private PersonClient client = null;
    
    public PersonControllerTest() {
        super(1, 3);
    }

    @Override
    protected PersistenceMarshallingService<PersonResponse, PersonFindResponse, Person> getClient() {
        return client;
    }

    @Override
    protected Person createSaveRequest() {
        return new Person().withFirstName(FIRST_NAME).withLastName(LAST_NAME);
    }

    @Override
    protected void verifyRecord(Person record, boolean save) {
        assertNotNull("Result is null.", record);
        
        verifyPrimaryKey(record.getId(), save);
        
        assertEquals("'firstName'", FIRST_NAME, record.getFirstName());
        assertEquals("'lastName'", LAST_NAME, record.getLastName());
        
        verifyAuditInfo(record.getLastUpdated(), record.getLastName(), record.getCreated(), record.getCreateUser());

        logger.debug("id=" + record.getId() + 
                     "  firstName=" + record.getFirstName() + 
                     "  lastName=" + record.getLastName() +
                     "  lastUpdated=" + record.getLastUpdated() +
                     "  lastUpdateUser=" + record.getLastUpdateUser() +
                     "  created=" + record.getCreated() +
                     "  createUser=" + record.getCreateUser());
    }
    
}
