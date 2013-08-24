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
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.FIRST_NAME;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.LAST_NAME;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.NEW_LAST_NAME;

import org.springbyexample.contact.web.client.person.PersonClient;
import org.springbyexample.contact.web.service.AbstractPersistenceContactControllerTest;
import org.springbyexample.mvc.rest.service.PersistenceMarshallingService;
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
public class PersonControllerTest extends AbstractPersistenceContactControllerTest<PersonResponse, PersonFindResponse, Person> {

    @Autowired
    private final PersonClient client = null;

    public PersonControllerTest() {
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
        assertNotNull("Result is null.", record);

        verifyPrimaryKey(record.getId(), save);

        assertEquals("'firstName'", FIRST_NAME, record.getFirstName());

        if (!update) {
            assertEquals("'lastName'", LAST_NAME, record.getLastName());
        } else {
            assertEquals("'lastName'", NEW_LAST_NAME, record.getLastName());
        }

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
