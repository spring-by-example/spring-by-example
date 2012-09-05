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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springbyexample.web.service.person.PersonController.FIRST_NAME;
import static org.springbyexample.web.service.person.PersonController.ID;
import static org.springbyexample.web.service.person.PersonController.LAST_NAME;

import org.springbyexample.schema.beans.person.Person;
import org.springbyexample.schema.beans.person.PersonFindResponse;
import org.springbyexample.schema.beans.person.PersonResponse;
import org.springbyexample.web.client.person.PersonClient;
import org.springbyexample.web.service.AbstractPersistenceControllerTest;
import org.springbyexample.web.service.PersistenceMarshallingService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests person client against an embedded REST service 
 * with the main Spring context as the parent context.
 * 
 * @author David Winterfeldt
 */
public class PersonControllerTest extends AbstractPersistenceControllerTest<PersonResponse, PersonFindResponse, Person> {

    @Autowired
    private PersonClient client = null;

    @Override
    protected PersistenceMarshallingService<PersonResponse, PersonFindResponse, Person> getClient() {
        return client;
    }

    @Override
    protected Person createSaveRequest() {
        return new Person().withId(ID).withFirstName(FIRST_NAME).withLastName(LAST_NAME);
    }

    @Override
    protected void verifyRecord(Person record) {
        assertNotNull("Result is null.", record);
        
        assertEquals("'id'", ID.intValue(), record.getId());
        assertEquals("'firstName'", FIRST_NAME, record.getFirstName());
        assertEquals("'lastName'", LAST_NAME, record.getLastName());

        logger.debug("id=" + record.getId() + 
                     "  firstName=" + record.getFirstName() + 
                     "  lastName=" + record.getLastName());
    }
    
}
