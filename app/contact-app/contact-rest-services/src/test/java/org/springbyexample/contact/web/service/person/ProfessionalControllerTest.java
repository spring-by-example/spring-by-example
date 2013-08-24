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
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.FIRST_ID;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.FIRST_NAME;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.LAST_NAME;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.NEW_LAST_NAME;
import static org.springbyexample.contact.web.service.person.ProfessionalController.COMPANY_NAME;

import org.springbyexample.contact.web.client.person.ProfessionalClient;
import org.springbyexample.contact.web.service.AbstractPersistenceContactControllerTest;
import org.springbyexample.mvc.rest.service.PersistenceMarshallingService;
import org.springbyexample.schema.beans.person.Professional;
import org.springbyexample.schema.beans.person.ProfessionalFindResponse;
import org.springbyexample.schema.beans.person.ProfessionalResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests professional client against an embedded REST service
 * with the main Spring context as the parent context.
 *
 * @author David Winterfeldt
 */
public class ProfessionalControllerTest extends AbstractPersistenceContactControllerTest<ProfessionalResponse, ProfessionalFindResponse, Professional> {

    @Autowired
    private final ProfessionalClient client = null;

    public ProfessionalControllerTest() {
        super(1, 2);
    }

    @Override
    protected PersistenceMarshallingService<ProfessionalResponse, ProfessionalFindResponse, Professional> getClient() {
        return client;
    }

    @Override
    protected Professional generateCreateRequest() {
        // update since primary key is specified
        return new Professional().withId(FIRST_ID).withFirstName(FIRST_NAME).withLastName(LAST_NAME)
                                 .withCompanyName(COMPANY_NAME);
    }

    @Override
    protected Professional generateUpdateRequest(Professional request) {
        return request.withLastName(NEW_LAST_NAME);
    }

    @Override
    protected Professional generateDeleteRequest() {
        return new Professional().withId(id);
    }

    @Override
    protected void verifyRecord(Professional record, boolean save, boolean update) {
        assertNotNull("Result is null.", record);

        verifyPrimaryKey(record.getId(), save);

        assertEquals("'firstName'", FIRST_NAME, record.getFirstName());

        if (!update) {
            assertEquals("'lastName'", LAST_NAME, record.getLastName());
        } else {
            assertEquals("'lastName'", NEW_LAST_NAME, record.getLastName());
        }

        assertEquals("'companyName'", COMPANY_NAME, record.getCompanyName());

//        verifyAuditInfo(record.getLastUpdated(), record.getLastName(), record.getCreated(), record.getCreateUser());

        logger.debug("id=" + record.getId() +
                     "  firstName=" + record.getFirstName() +
                     "  lastName=" + record.getLastName() +
                     "  companyName=" + record.getCompanyName());
    }

}
