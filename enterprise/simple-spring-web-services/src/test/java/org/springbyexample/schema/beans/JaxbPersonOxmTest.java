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

package org.springbyexample.schema.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.person.schema.beans.Person;
import org.springbyexample.person.schema.beans.PersonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.MarshallingFailureException;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests JAXB generated beans with Spring OXM for serialization/deserialization.
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JaxbPersonOxmTest {

    final Logger logger = LoggerFactory.getLogger(JaxbPersonOxmTest.class);
 
    protected final static Integer ID = new Integer(1);
    protected final static String FIRST_NAME = "Joe";
    protected final static String LAST_NAME = "Smith";

    protected final static Integer SECOND_ID = new Integer(2);
    protected final static String SECOND_FIRST_NAME = "John";
    protected final static String SECOND_LAST_NAME = "Jackson";

    protected final static String MARSHALLER_RESULT = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                                                      "<ns2:person-response xmlns:ns2=\"http://www.springbyexample.org/person/schema/beans\">" +
                                                      "<person><id>1</id><first-name>Joe</first-name><last-name>Smith</last-name></person>" +
                                                      "<person><id>2</id><first-name>John</first-name><last-name>Jackson</last-name></person>" +
                                                      "</ns2:person-response>";

    @Autowired
    protected Marshaller marshaller = null;
    
    @Autowired
    protected Unmarshaller unmarshaller = null;

    /**
     * Tests marshaller.
     */ 
    @Test
    public void testMarshaller() {
        assertNotNull("Marshaller is null.", marshaller);

        PersonResponse personList = new PersonResponse().withPerson(
                new Person().withId(ID).withFirstName(FIRST_NAME).withLastName(LAST_NAME),
                new Person().withId(SECOND_ID).withFirstName(SECOND_FIRST_NAME).withLastName(SECOND_LAST_NAME));
                
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        try {
            marshaller.marshal(personList, new StreamResult(out));
        } catch (IOException e) {
            throw new MarshallingFailureException(e.getMessage(), e);
        }
        
        logger.debug("Marshalled person list.  '" + out.toString() + "'.");

        assertEquals("Marshalled person list was expected to be '" + MARSHALLER_RESULT + "'.", MARSHALLER_RESULT, out.toString());
    }

    /**
     * Tests unmarshaller.
     */ 
    @Test
    public void testUnarshaller() throws XmlMappingException, IOException {
        assertNotNull("Unmarshaller is null.", unmarshaller);
        
        ByteArrayInputStream in = new ByteArrayInputStream(MARSHALLER_RESULT.getBytes());
        
        Object value = unmarshaller.unmarshal(new StreamSource(in));
        
        assertNotNull("Unmarshalled instance is null.", value);
        assertTrue("Not an instance of Persons.", (value instanceof PersonResponse));
        
        PersonResponse personResponse = (PersonResponse)value;
        
        int expectedSize = 2;
        assertEquals("Expected person list to be a size of '" + expectedSize + "'.", expectedSize, personResponse.getPerson().size());
        
        Person person = personResponse.getPerson().get(0);
        
        assertNotNull("Person is null.", person);
        
        assertEquals("Expected id of '" + ID + "'.", ID.intValue(), person.getId());
        assertEquals("Expected first name of '" + FIRST_NAME + "'.", FIRST_NAME, person.getFirstName());
        assertEquals("Expected last name of '" + LAST_NAME + "'.", LAST_NAME, person.getLastName());

        Person person2 = personResponse.getPerson().get(1);
        
        assertNotNull("Person is null.", person2);
        
        assertEquals("Expected id of '" + SECOND_ID + "'.", SECOND_ID.intValue(), person2.getId());
        assertEquals("Expected first name of '" + SECOND_FIRST_NAME + "'.", SECOND_FIRST_NAME, person2.getFirstName());
        assertEquals("Expected last name of '" + SECOND_LAST_NAME + "'.", SECOND_LAST_NAME, person2.getLastName());
    }
    
}
