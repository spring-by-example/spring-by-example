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
import static org.springbyexample.web.service.person.ProfessionalController.COMPANY_NAME;

import java.util.List;

import org.springbyexample.schema.beans.person.Professional;
import org.springbyexample.schema.beans.person.ProfessionalFindResponse;
import org.springbyexample.schema.beans.person.ProfessionalResponse;
import org.springbyexample.web.client.person.ProfessionalClient;
import org.springbyexample.web.service.AbstractPersistenceControllerTest;
import org.springbyexample.web.service.PersistenceMarshallingService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests professional client against an embedded REST service 
 * with the main Spring context as the parent context.
 * 
 * @author David Winterfeldt
 */
public class ProfessionalControllerTest extends AbstractPersistenceControllerTest<ProfessionalResponse, ProfessionalFindResponse, Professional> {

    @Autowired
    private ProfessionalClient client = null;
    
    @Override
    protected PersistenceMarshallingService<ProfessionalResponse, ProfessionalFindResponse, Professional> getClient() {
        return client;
    }

    @Override
    protected Professional getResult(ProfessionalResponse response) {
        return response.getResult();
    }

    @Override
    protected List<Professional> getResults(ProfessionalFindResponse response) {
        return response.getResults();
    }

    @Override
    protected Professional createSaveRequest() {
        return new Professional().withId(ID).withFirstName(FIRST_NAME).withLastName(LAST_NAME)
                                 .withCompanyName(COMPANY_NAME);
    }

    @Override
    protected void verifyRecord(Professional record) {
        assertNotNull("Result is null.", record);
        
        assertEquals("'id'", ID.intValue(), record.getId());
        assertEquals("'firstName'", FIRST_NAME, record.getFirstName());
        assertEquals("'lastName'", LAST_NAME, record.getLastName());
        assertEquals("'companyName'", COMPANY_NAME, record.getCompanyName());

        logger.debug("id=" + record.getId() + 
                     "  firstName=" + record.getFirstName() + 
                     "  lastName=" + record.getLastName() +
                     "  companyName=" + record.getCompanyName());
    }

}
