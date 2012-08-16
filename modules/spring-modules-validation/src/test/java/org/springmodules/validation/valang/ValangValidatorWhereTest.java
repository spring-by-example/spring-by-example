/*
 * Copyright 2004-2009 the original author or authors.
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

package org.springmodules.validation.valang;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;
import org.springmodules.validation.valang.Person.CreditRating;

/**
 * <code>ValangValidator</code> where predicate test.
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ValangValidatorWhereTest {

	final Logger logger = LoggerFactory.getLogger(ValangValidatorWhereTest.class);
	
	@Autowired
	private Validator validator = null;

	/**
	 * Test passing validation.
	 */
	@Test
	public void testValidValidation() {
		assertNotNull("Validator is null.", validator);

		Person person = new Person();
		person.setFirstName("Joe");
		person.setLastName("Smith");
		person.setAge(21);
		person.setCreditStatus(CreditStatus.PENDING);
		person.setCreditRating(CreditRating.EXCELLENT);
		person.setLastUpdated(new Date());
		
		Errors errors = new MapBindingResult(new HashMap(), "person");

		validator.validate(person, errors);
		
		assertTrue("Should not have any errors.", !errors.hasErrors());
	}

	/**
     * Test invalid where validation.
     */
    @Test
    public void testInvalidWhereValidation() {
        assertNotNull("Validator is null.", validator);

        // since first name isn't valid for the list in the where clause 
        // the main validation won't run, so it won't fail on the validLastName function.
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Smythe");
        person.setAge(21);
        person.setCreditStatus(CreditStatus.PENDING);
        person.setCreditRating(CreditRating.EXCELLENT);
        person.setLastUpdated(new Date());
        
        Errors errors = new MapBindingResult(new HashMap(), "person");

        validator.validate(person, errors);
        
        assertTrue("Should not have any errors.", !errors.hasErrors());
    }

    /**
     * Test invalid validation.
     */
    @Test
    public void testInvalidValidation() {
        assertNotNull("Validator is null.", validator);

        // since first name isn't valid for the list in the where clause 
        // the main validation won't run, so it won't fail on the validLastName function.
        Person person = new Person();
        person.setFirstName("Joe");
        person.setLastName("Smythe");
        person.setAge(21);
        person.setCreditStatus(CreditStatus.PENDING);
        person.setCreditRating(CreditRating.EXCELLENT);
        person.setLastUpdated(new Date());
        
        Errors errors = new MapBindingResult(new HashMap(), "person");

        validator.validate(person, errors);
        
        assertTrue("Should have an error.", errors.hasErrors());
    }

}
