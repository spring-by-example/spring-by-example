/*
 * Copyright 2007-2014 the original author or authors.
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
package org.springbyexample.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

/**
 * Tests bean validation.
 *
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class BeanValidationTest {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Validator validator;

    @Test
    public void testPerson() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Johnson");

        Map<?, ?> errors = new HashMap();
        MapBindingResult bindingResult = new MapBindingResult(errors, "person");

        validator.validate(person, bindingResult);


        assertTrue("Validator has errors", !bindingResult.hasErrors());
    }

    @Test
    public void testBadPerson() {
        Person person = new Person();
        person.setFirstName("John");

        Map<?, ?> errors = new HashMap();
        MapBindingResult bindingResult = new MapBindingResult(errors, "person");

        validator.validate(person, bindingResult);


        assertTrue("Validator has errors", bindingResult.hasErrors());
    }

    @Test
    public void testPersonAgeGroup() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Johnson");
        person.setAge(21);

        javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(0, constraintViolations.size());

        // validate age group
        constraintViolations = validator.validate(person, AgeCheck.class);

        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void testBadPersonAgeGroup() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Johnson");

        javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(0, constraintViolations.size());

        // validate age group
        constraintViolations = validator.validate(person, AgeCheck.class);

        assertEquals(1, constraintViolations.size());
        assertEquals("You have to be 18 to drive a car", constraintViolations.iterator().next().getMessage());
    }

}
