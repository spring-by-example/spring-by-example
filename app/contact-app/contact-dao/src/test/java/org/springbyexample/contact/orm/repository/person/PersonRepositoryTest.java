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
package org.springbyexample.contact.orm.repository.person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.ADDR;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.CITY;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.COUNTRY;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.EXPECTED_ADDRESS_COUNT;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.EXPECTED_COUNT;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.FIRST_ID;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.FIRST_NAME;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.LAST_NAME;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.SECOND_ID;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.STATE;
import static org.springbyexample.contact.test.constants.person.PersonTestConstants.ZIP_POSTAL;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.contact.orm.entity.AbstractAuditableEntity;
import org.springbyexample.contact.orm.entity.person.Address;
import org.springbyexample.contact.orm.entity.person.Person;
import org.springbyexample.contact.orm.entity.person.Professional;
import org.springbyexample.contact.orm.repository.AbstractRepositoryTest;
import org.springbyexample.contact.orm.repository.PersonRepository;
import org.springbyexample.contact.orm.repository.ProfessionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * Tests Person repository.
 * 
 * @author David Winterfeldt
 */
public class PersonRepositoryTest extends AbstractRepositoryTest {

    final Logger logger = LoggerFactory.getLogger(PersonRepositoryTest.class);

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ProfessionalRepository professionalRepository;
    
    @Test
    public void testFindOne() {
        Person person = personRepository.findOne(FIRST_ID);

        testPersonOne(person);
    }
    
    @Test
    public void testFindAll() {
        Collection<Person> persons = personRepository.findAll();

        assertNotNull("Person list is null.", persons);
        assertEquals("Number of persons should be " + EXPECTED_COUNT + ".", EXPECTED_COUNT, persons.size());
        
        for (Person person : persons) {
            logger.debug(person.toString());
            
            if (FIRST_ID.equals(person.getId())) {                
                testPersonOne(person);
            } else if (SECOND_ID.equals(person.getId())) {
                testPersonTwo(person);
            }
        }
    }

    @Test
    public void testFindByFirstNameLike() {
        List<Person> persons = personRepository.findByFirstNameLike("J%");
        
        int expectedCount = 2;
        
        assertNotNull("Person list is null.", persons);
        assertEquals("Number of persons should be " + expectedCount + ".", expectedCount, persons.size());
        
        Person person = persons.get(0);
        
        testPersonOne(person);
    }

    @Test
    public void testFindByLastName() {
        List<Person> persons = personRepository.findByLastName(LAST_NAME);

        int expectedCount = 1;
        
        assertNotNull("Person list is null.", persons);
        assertEquals("Number of persons should be " + expectedCount + ".", expectedCount, persons.size());

        Person person = persons.get(0);
        
        testPersonOne(person);
    }

    @Test
    public void testFindByAddress() {
        List<Person> persons = personRepository.findByAddress(ADDR);
        
        int expectedCount = 1;
        
        assertNotNull("Person list is null.", persons);
        assertEquals("Number of persons should be " + expectedCount + ".", expectedCount, persons.size());
        
        Person person = persons.get(0);
        
        testPersonOne(person);
    }

    @Test
    public void testFindByAddressPage() {
        String firstName = "Jack";
        String lastName = "Johnson";
        String companyName = "Spring Pizza";

        int page = 0;
        int size = 10;

        for (int i = 0; i < 35; i++) {
            createProfessional(firstName, lastName, companyName, ADDR);
        }
        
        Page<Person> pageResult = personRepository.findByAddress(ADDR, new PageRequest(page, size));
        List<Person> persons = pageResult.getContent();
        
        int expectedCount = size;
        
        assertNotNull("Person list is null.", persons);
        assertEquals("Number of persons should be " + expectedCount + ".", expectedCount, persons.size());

        // query last page
        page = pageResult.getTotalPages() - 1;
        pageResult = personRepository.findByAddress(ADDR, new PageRequest(page, size));
        persons = pageResult.getContent();
        
        // created 35 records with the same address, one existing
        expectedCount = 6;
        
        assertNotNull("Person list is null.", persons);
        assertEquals("Number of persons should be " + expectedCount + ".", expectedCount, persons.size());
        
    }

    @Test
    public void testFindByName() {
        List<Person> persons = personRepository.findByName(FIRST_NAME, LAST_NAME);
        
        int expectedCount = 1;
        
        assertNotNull("Person list is null.", persons);
        assertEquals("Number of persons should be " + expectedCount + ".", expectedCount, persons.size());
        
        Person person = persons.get(0);
        
        testPersonOne(person);
    }

    @Test
    public void testSave() {
        String firstName = "Jack";
        String lastName = "Johnson";
        String companyName = "Spring Pizza";
        
        Person person = createProfessional(firstName, lastName, companyName, ADDR);
        
        // get PK of first address
        Integer addressId = person.getAddresses().iterator().next().getId();

        // test saved person
        testPerson(person, 
                   firstName, lastName,  
                   EXPECTED_ADDRESS_COUNT, addressId, ADDR, CITY, STATE, ZIP_POSTAL,
                   true, companyName);

        person = professionalRepository.findOne(person.getId());

        // test retrieved person just saved
        testPerson(person, 
                   firstName, lastName,  
                   EXPECTED_ADDRESS_COUNT, addressId, ADDR, CITY, STATE, ZIP_POSTAL,
                   true, companyName);

        Collection<Person> persons = personRepository.findAll();

        int expectedCount = EXPECTED_COUNT + 1;
        
        assertNotNull("Person list is null.", persons);
        assertEquals("Number of persons should be " + expectedCount + ".", expectedCount, persons.size());
    }

    @Test
    public void testUpdate() {
        Person person = personRepository.findOne(FIRST_ID);
        testPersonOne(person);
        
        String lastName = "Jones"; 
        person.setLastName(lastName);
        
        personRepository.saveAndFlush(person);

        person = personRepository.findOne(FIRST_ID);
        testPersonOne(person, lastName);
    }

    @Test
    public void testDelete() {
        personRepository.delete(FIRST_ID);

        // person should be null after delete
        Person person = personRepository.findOne(FIRST_ID);
        assertNull("Person is not null.", person);
    }

    /**
     * Create professional.
     */
    private Person createProfessional(String firstName, String lastName, String companyName,
                                            String addr) {
        Professional person = new Professional();
        Set<Address> addresses = new HashSet<Address>();
        Address address = new Address();
        addresses.add(address);
        
        address.setAddress(addr);
        address.setCity(CITY);
        address.setState(STATE);
        address.setZipPostal(ZIP_POSTAL);
        address.setCountry(COUNTRY);
        
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setCompanyName(companyName);
        person.setAddresses(addresses);
        
        Person result = personRepository.saveAndFlush(person);
        
        return result;
    }
    
    /**
     * Tests person with a PK of one.
     */
    private void testPersonOne(Person person) {
        testPersonOne(person, LAST_NAME);
    }
    
    /**
     * Tests person with a PK of one.
     */
    private void testPersonOne(Person person, String lastName) {
        String schoolName = "NYU";
        
        Integer addressId = new Integer(1);
        
        testPerson(person, 
                   FIRST_NAME, lastName,  
                   EXPECTED_ADDRESS_COUNT, addressId, ADDR, CITY, STATE, ZIP_POSTAL,
                   false, schoolName);
    }

    /**
     * Tests person with a PK of two.
     */
    private void testPersonTwo(Person person) {
        String firstName = "John";
        String lastName = "Wilson";
        String companyName = "Spring Pizza";
        
        int expectedAddresses = 2;
                
        Integer addressId = new Integer(3);
        String addr = "47 Howard St.";
        String city = "San Francisco";
        String state = "CA";
        String zipPostal = "94103";

        testPerson(person, 
                   firstName, lastName,  
                   expectedAddresses, addressId, addr, city, state, zipPostal,
                   true, companyName);
    }

    /**
     * Tests person.
     */
    private void testPerson(Person person, 
                            String firstName, String lastName, 
                            int expectedAddresses, Integer addressId,
                            String addr, String city, String state, String zipPostal,
                            boolean professional, String professionName) {
        assertNotNull("Person is null.", person);
        
        assertEquals("firstName", firstName, person.getFirstName());
        assertEquals("lastName", lastName, person.getLastName());
        
        assertNotNull("Person's address list is null.", person.getAddresses());
        assertEquals("addresses", expectedAddresses, person.getAddresses().size());
        
//        if (professional) {
//            assertTrue("Person should be an instance of professional.", (person instanceof Professional));
//            assertEquals("companyName", professionName, ((Professional)person).getCompanyName());
//        } else {
//            assertTrue("Person should be an instance of student.", (person instanceof Student));
//            assertEquals("schoolName", professionName, ((Student)person).getSchoolName());            
//        }
        
        for (Address address : person.getAddresses()) {
            assertNotNull("Address is null.", address);
            
            if (addressId.equals(address.getId())) {
                assertEquals("address.id", addressId, address.getId());
                assertEquals("address.addr", addr, address.getAddress());
                
                assertEquals("address.city", city, address.getCity());
                assertEquals("address.state", state, address.getState());
                assertEquals("address.zipPostal" + zipPostal + "'.", zipPostal, address.getZipPostal());
                assertEquals("address.country", COUNTRY, address.getCountry());
                
                testAuditable(address);
            }
        }
        
        testAuditable(person);
    }

    /**
     * Tests auditable entity.
     */
    private void testAuditable(AbstractAuditableEntity auditRecord) {
        assertNotNull("lastUpdated", auditRecord.getLastModifiedDate());
        assertNotNull("lastUpdatedBy", auditRecord.getLastModifiedBy());
        assertNotNull("created", auditRecord.getCreatedDate());
        assertNotNull("createdBy", auditRecord.getCreatedBy());
    }
 
}
