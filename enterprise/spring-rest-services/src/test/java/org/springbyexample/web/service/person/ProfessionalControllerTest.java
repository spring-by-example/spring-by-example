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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.schema.beans.person.Person;
import org.springbyexample.schema.beans.person.Professional;
import org.springbyexample.schema.beans.person.ProfessionalFindResponse;
import org.springbyexample.schema.beans.person.ProfessionalResponse;
import org.springbyexample.schema.beans.response.ResponseResult;
import org.springbyexample.web.client.person.ProfessionalClient;
import org.springbyexample.web.service.AbstractRestControllerTest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests person service client against an embedded web service 
 * with the main Spring context as the parent context.
 * 
 * @author David Winterfeldt
 */
public class ProfessionalControllerTest extends AbstractRestControllerTest {

    final static Logger logger = LoggerFactory.getLogger(ProfessionalControllerTest.class);
    
    protected final static Integer ID = 1;
    protected final static String FIRST_NAME = "Joe";
    protected final static String LAST_NAME = "Smith";

    protected final static Integer SECOND_ID = 2;
    protected final static String SECOND_FIRST_NAME = "John";
    protected final static String SECOND_LAST_NAME = "Jackson";

    @Autowired
    protected ProfessionalClient client = null;
    
    @Test
    public void testFindById() {
        ProfessionalResponse response = client.findById(ID);
        
        assertNotNull("Response is null.", response);
        
        verifyPerson(response.getResult());
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
        verifyPerson(response.getResults().get(0));
    }

    @Test
    public void testFind() {
        ProfessionalFindResponse response = client.find();
        assertNotNull("Response is null.", response);

        int expectedCount = 2;
        assertEquals("count", expectedCount, response.getCount());
        
        assertNotNull("Response results is null.", response.getResults());
        verifyPerson(response.getResults().get(0));
    }

    @Test
    public void testSave() {
        Professional request = new Professional().withId(ID).withFirstName(FIRST_NAME).withLastName(LAST_NAME);
        
        ProfessionalResponse response = client.save(request);
        
        assertNotNull("Response is null.", response);
        
        verifyPerson(response.getResult());

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
     * Tests person is valid.
     */
    protected void verifyPerson(Person person) {
        assertNotNull("Result is null.", person);
        
        assertEquals("Expected id of '" + ID + "'.", ID.intValue(), person.getId());
        assertEquals("Expected first name of '" + FIRST_NAME + "'.", FIRST_NAME, person.getFirstName());
        assertEquals("Expected last name of '" + LAST_NAME + "'.", LAST_NAME, person.getLastName());

        logger.debug("id=" + person.getId() + 
                     "  firstName=" + person.getFirstName() + 
                     "  lastName=" + person.getLastName());
    }
    
}
