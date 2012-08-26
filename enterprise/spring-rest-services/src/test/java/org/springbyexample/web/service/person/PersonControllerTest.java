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

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.schema.beans.person.PersonResponse;
import org.springbyexample.web.client.person.PersonClient;
import org.springbyexample.web.service.AbstractRestControllerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Tests person service client against an embedded web service 
 * with the main Spring context as the parent context.
 * 
 * @author David Winterfeldt
 */
public class PersonControllerTest extends AbstractRestControllerTest {

    final static Logger logger = LoggerFactory.getLogger(PersonControllerTest.class);
    
    protected final static Integer ID = new Integer(1);
    protected final static String FIRST_NAME = "Joe";
    protected final static String LAST_NAME = "Smith";

    protected final static Integer SECOND_ID = new Integer(2);
    protected final static String SECOND_FIRST_NAME = "John";
    protected final static String SECOND_LAST_NAME = "Jackson";

    @Autowired
    protected PersonClient client = null;
    
    /**
     * Tests person service client.
     */ 
    @Test
    @DirtiesContext
    public void testFindById() {
        PersonResponse response = client.findById(ID);
        
        assertNotNull("Response is null.", response);
        assertNotNull("Result is null.", response.getResult());
        
//        PersonResponse response = client.getPersons(new GetPersonsRequest());
//        
//        logger.debug("Received response from server.");
//        
//        assertNotNull("Response is null.", response);
//        
//        int expectedSize = 2;
//        assertEquals("Expected person list to be a size of '" + expectedSize + "'.", expectedSize, response.getPerson().size());
//      
//        Person person = response.getPerson().get(0);
//      
//        assertNotNull("Person is null.", person);
//      
//        assertEquals("Expected id of '" + ID + "'.", ID.intValue(), person.getId());
//        assertEquals("Expected first name of '" + FIRST_NAME + "'.", FIRST_NAME, person.getFirstName());
//        assertEquals("Expected last name of '" + LAST_NAME + "'.", LAST_NAME, person.getLastName());
//    
//        Person person2 = response.getPerson().get(1);
//      
//        assertNotNull("Person is null.", person2);
//      
//        assertEquals("Expected id of '" + SECOND_ID + "'.", SECOND_ID.intValue(), person2.getId());
//        assertEquals("Expected first name of '" + SECOND_FIRST_NAME + "'.", SECOND_FIRST_NAME, person2.getFirstName());
//        assertEquals("Expected last name of '" + SECOND_LAST_NAME + "'.", SECOND_LAST_NAME, person2.getLastName());
//        
//        logger.debug("person1=[" +
//                     "id=" + person.getId() + 
//                     "  firstName=" + person.getFirstName() + 
//                     "  lastName=" + person.getLastName() + 
//                     "]," +
//                     "person2=[" +
//                     "id=" + person2.getId() + 
//                     "  firstName=" + person2.getFirstName() + 
//                     "  lastName=" + person2.getLastName() + 
//                     "]");
    }
    
}
