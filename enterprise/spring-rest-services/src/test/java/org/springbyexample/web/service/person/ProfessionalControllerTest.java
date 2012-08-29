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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.schema.beans.person.Professional;
import org.springbyexample.schema.beans.person.ProfessionalFindResponse;
import org.springbyexample.schema.beans.person.ProfessionalResponse;
import org.springbyexample.schema.beans.response.ResponseResult;
import org.springbyexample.web.client.person.ProfessionalClient;
import org.springbyexample.web.service.AbstractRestControllerTest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests professional client against an embedded REST service 
 * with the main Spring context as the parent context.
 * 
 * @author David Winterfeldt
 */
public class ProfessionalControllerTest extends AbstractRestControllerTest {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ProfessionalClient client = null;

    @Test
    public void testFindById() {
        ProfessionalResponse response = client.findById(ID);
        
        assertNotNull("Response is null.", response);
        
        verifyRecord(response.getResult());
    }

    @Test
    public void testPaginatedFind() {
        int page = 0;
        int pageSize = 2;
        
        ProfessionalFindResponse response = client.find(page, pageSize);
        assertNotNull("Response is null.", response);
        
        int expectedCount = 2;
        assertEquals("count", expectedCount, response.getCount());
        
        assertNotNull("Response results is null.", response.getResults());
        verifyRecord(response.getResults().get(0));
    }

    @Test
    public void testFind() {
        ProfessionalFindResponse response = client.find();
        assertNotNull("Response is null.", response);

        int expectedCount = 2;
        assertEquals("count", expectedCount, response.getCount());
        
        assertNotNull("Response results is null.", response.getResults());
        verifyRecord(response.getResults().get(0));
    }

    @Test
    public void testSave() {
        Professional request = new Professional().withId(ID).withFirstName(FIRST_NAME).withLastName(LAST_NAME)
                                                 .withCompanyName(COMPANY_NAME);
        
        ProfessionalResponse response = client.save(request);
        
        assertNotNull("Response is null.", response);
        
        verifyRecord(response.getResult());

        int expectedCount = 1;
        assertEquals("messageList.size", expectedCount, response.getMessageList().size());

        logger.info(response.getMessageList().get(0).getMessage());
    }

    @Test
    public void testDelete() {
        ResponseResult response = client.delete(ID);
        
        assertNotNull("Response is null.", response);

        int expectedCount = 1;
        assertEquals("messageList.size", expectedCount, response.getMessageList().size());

        logger.info(response.getMessageList().get(0).getMessage());
    }

    /**
     * Verify professional request.
     */
    protected void verifyRecord(Professional request) {
        assertNotNull("Result is null.", request);
        
        assertEquals("'id'", ID.intValue(), request.getId());
        assertEquals("'firstName'", FIRST_NAME, request.getFirstName());
        assertEquals("'lastName'", LAST_NAME, request.getLastName());
        assertEquals("'companyName'", COMPANY_NAME, request.getCompanyName());

        logger.debug("id=" + request.getId() + 
                     "  firstName=" + request.getFirstName() + 
                     "  lastName=" + request.getLastName() +
                     "  companyName=" + request.getCompanyName());
    }

}
