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

package org.springbyexample.orm.jpa.inheritance.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.orm.jpa.inheritance.bean.Address;
import org.springbyexample.orm.jpa.inheritance.bean.Person;
import org.springbyexample.orm.jpa.inheritance.bean.Professional;
import org.springbyexample.orm.jpa.inheritance.bean.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests Person DAO.
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PersonInheritanceDaoTest {

    final Logger logger = LoggerFactory.getLogger(PersonInheritanceDaoTest.class);
    
    @Autowired
    protected PersonInheritanceDao personDao = null;

    @Test
    @DirtiesContext // reset context so in memory DB re-inits
    public void testHibernateTemplate() throws SQLException {
        assertNotNull("Person DAO is null.", personDao);
        
        Collection<Person> lPersons = personDao.findPersons();

        int expected = 3;
        
        assertNotNull("Person list is null.", lPersons);
        assertEquals("Number of persons should be " + expected + ".", expected, lPersons.size());
        
        Integer firstId = new Integer(1);
        Integer secondId = new Integer(2);
        
        for (Person person : lPersons) {
            assertNotNull("Person is null.", person);
            
            if (firstId.equals(person.getId())) {                
                String firstName = "Joe";
                String lastName = "Smith";
                String schoolName = "NYU";
                
                int expectedAddresses = 1;

            	assertEquals("Person first name should be " + firstName + ".", firstName, person.getFirstName());
                assertEquals("Person last name should be " + lastName + ".", lastName, person.getLastName());
            
                assertNotNull("Person's address list is null.", person.getAddresses());
                assertEquals("Number of person's address list should be " + expectedAddresses + ".", expectedAddresses, person.getAddresses().size());
                
                assertTrue("Person should be an instance of student.", (person instanceof Student));
                assertEquals("School name should be " + schoolName + ".", schoolName, ((Student)person).getSchoolName());
                
                Integer addressId = new Integer(1);
                String addr = "1060 West Addison St.";
                String city = "Chicago";
                String state = "IL";
                String zipPostal = "60613";
                
                for (Address address : person.getAddresses()) {
                	assertNotNull("Address is null.", address);
                	
                	assertEquals("Address id should be '" + addressId + "'.", addressId, address.getId());
                	assertEquals("Address address should be '" + address + "'.", addr, address.getAddress());
                	
                	assertEquals("Address city should be '" + city + "'.", city, address.getCity());
                	assertEquals("Address state should be '" + state + "'.", state, address.getState());
                	assertEquals("Address zip/postal should be '" + zipPostal + "'.", zipPostal, address.getZipPostal());
                }
            } else if (secondId.equals(person.getId())) {                
                String firstName = "John";
                String lastName = "Wilson";
                String companyName = "Spring Pizza";
                
                int expectedAddresses = 2;

            	assertEquals("Person first name should be " + firstName + ".", firstName, person.getFirstName());
                assertEquals("Person last name should be " + lastName + ".", lastName, person.getLastName());
            
                assertNotNull("Person's address list is null.", person.getAddresses());
                assertEquals("Number of person's address list should be " + expectedAddresses + ".", expectedAddresses, person.getAddresses().size());

                assertTrue("Person should be an instance of professional.", (person instanceof Professional));
                assertEquals("Company name should be " + companyName + ".", companyName, ((Professional)person).getCompanyName());
                
                Integer addressId = new Integer(3);
                String addr = "47 Howard St.";
                String city = "San Francisco";
                String state = "CA";
                String zipPostal = "94103";

                for (Address address : person.getAddresses()) {
                	assertNotNull("Address is null.", address);
                	
                	if (addressId.equals(address.getId())) {
	                	assertEquals("Address id should be '" + addressId + "'.", addressId, address.getId());
	                	assertEquals("Address address should be '" + address + "'.", addr, address.getAddress());
	                	
	                	assertEquals("Address city should be '" + city + "'.", city, address.getCity());
	                	assertEquals("Address state should be '" + state + "'.", state, address.getState());
	                	assertEquals("Address zip/postal should be '" + zipPostal + "'.", zipPostal, address.getZipPostal());
                	}
                }
            }

            logger.debug(person.toString());
        }
    }
    
}
