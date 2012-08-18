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

package org.springbyexample.orm.hibernate3.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.orm.hibernate3.bean.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests Person DAO with transaction enabled.
 * The spring configuration uses 
 * &lt;tx:annotation-driven/&gt; to process the 
 * <code>@Transactional</code> annotation in the 
 * <code>PersonDaoImpl</code> class.
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PersonDaoTransactionTest {

    final Logger logger = LoggerFactory.getLogger(PersonDaoTransactionTest.class);
    
    protected static int SIZE = 2;
    protected static Integer ID = new Integer(1);
    protected static String FIRST_NAME = "Joe";
    protected static String LAST_NAME = "Smith";
    protected static String CHANGED_LAST_NAME = "Jackson";

    @Autowired
    protected PersonDao personDao = null;

    @Test
    public void testHibernateTemplate() throws SQLException {
        assertNotNull("Person DAO is null.", personDao);
        
        Collection<Person> lPersons = personDao.findPersons();

        assertNotNull("Person list is null.", lPersons);
        assertEquals("Number of persons should be " + SIZE + ".", SIZE, lPersons.size());
        
        for (Person person : lPersons) {
            assertNotNull("Person is null.", person);
            
            if (ID.equals(person.getId())) {                
                assertEquals("Person first name should be " + FIRST_NAME + ".", FIRST_NAME, person.getFirstName());
                assertEquals("Person last name should be " + LAST_NAME + ".", LAST_NAME, person.getLastName());
                
                person.setLastName(CHANGED_LAST_NAME);

                personDao.save(person);
            }
        }
    }
    
}
