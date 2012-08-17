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
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class ValangValidatorPerformanceTest {

	final Logger logger = LoggerFactory.getLogger(ValangValidatorPerformanceTest.class);
	
	@Autowired
	@Qualifier("personValidator")
	private Validator personValidator = null;

	@Autowired
    @Qualifier("bytecodePersonValidator")
    private Validator bytecodePersonValidator = null;

    @Autowired
    @Qualifier("personMapValidator")
    private Validator personMapValidator = null;

    @Autowired
    @Qualifier("bytecodeMapPersonValidator")
    private Validator bytecodeMapPersonValidator = null;

    @Autowired
    @Qualifier("personListValidator")
    private Validator personListValidator = null;

    @Autowired
    @Qualifier("bytecodeListPersonValidator")
    private Validator bytecodeListPersonValidator = null;

    @Autowired
    @Qualifier("personArrayValidator")
    private Validator personArrayValidator = null;

    @Autowired
    @Qualifier("bytecodeArrayPersonValidator")
    private Validator bytecodeArrayPersonValidator = null;

    
    @Autowired
    private Map<String, Object> hVars = null;

    @Autowired
    private List<Object> lVars = null;

	int count = 1000 * 1000;

	/**
	 * Test standard property's performance.
	 */
	@Test
	public void testValangProperty() throws Exception {
		assertNotNull("Validator is null.", personValidator);
		assertNotNull("Bytecode Validator is null.", bytecodePersonValidator);

	    Person bean = new Person();
        bean.setFirstName("Joe");
        bean.setLastName("Smith");
        bean.setAge(21);
        bean.setCreditStatus(CreditStatus.PENDING);
        bean.setCreditRating(CreditRating.EXCELLENT);
        bean.setLastUpdated(new Date());
        
		double personValidatorResult = validate(personValidator, bean);
		double bytecodePersonValidatorResult = validate(bytecodePersonValidator, bean);

		logger.info("Person validator took {}ns, and bytecode person validator took {}ns.", 
		            personValidatorResult, bytecodePersonValidatorResult);
		
		// really should be at least twice as fast
		assertTrue("Bytecode person validator should be faster than standard.", 
		          (bytecodePersonValidatorResult < personValidatorResult));
	}

	/**
     * Test <code>Map</code> property's performance.
     */
    @Test
    public void testValangMap() throws Exception {
        assertNotNull("Validator is null.", personValidator);
        assertNotNull("Bytecode Validator is null.", bytecodePersonValidator);

        BeanWrapper bean = new BeanWrapper();
        bean.setMapVars(hVars);

        double personValidatorResult = validate(personMapValidator, bean);
        double bytecodePersonValidatorResult = validate(bytecodeMapPersonValidator, bean);

        logger.info("Person map validator took {}ns, and bytecode person map validator took {}ns.", 
                    personValidatorResult, bytecodePersonValidatorResult);
        
        // really should be at least twice as fast
        assertTrue("Bytecode person map validator should be at least 5 times faster than standard.", 
                  ((bytecodePersonValidatorResult / 5) < personValidatorResult));
    }

    /**
     * Test <code>List</code> property's index performance.
     */
    @Test
    public void testValangList() throws Exception {
        assertNotNull("Validator is null.", personValidator);
        assertNotNull("Bytecode Validator is null.", bytecodePersonValidator);

        BeanWrapper bean = new BeanWrapper();
        bean.setListVars(lVars);

        double personValidatorResult = validate(personListValidator, bean);
        double bytecodePersonValidatorResult = validate(bytecodeListPersonValidator, bean);

        logger.info("Person list validator took {}ns, and bytecode person list validator took {}ns.", 
                    personValidatorResult, bytecodePersonValidatorResult);
        
        // really should be at least twice as fast
        assertTrue("Bytecode person list validator should be at least 5 times faster than standard.", 
                  ((bytecodePersonValidatorResult / 5) < personValidatorResult));
    }

    /**
     * Test array's index performance.
     */
    @Test
    public void testValangArray() throws Exception {
        assertNotNull("Validator is null.", personValidator);
        assertNotNull("Bytecode Validator is null.", bytecodePersonValidator);

        BeanWrapper bean = new BeanWrapper();
        bean.setVars(lVars.toArray());

        double personValidatorResult = validate(personArrayValidator, bean);
        double bytecodePersonValidatorResult = validate(bytecodeArrayPersonValidator, bean);

        logger.info("Person array validator took {}ns, and bytecode person array validator took {}ns.", 
                    personValidatorResult, bytecodePersonValidatorResult);
        
        // really should be at least twice as fast
        assertTrue("Bytecode person array validator should be at least 5 times faster than standard.", 
                  ((bytecodePersonValidatorResult / 5) < personValidatorResult));
    }
    
	/**
	 * Use validator to perform basic validation.
	 * Expects validation to not generate any errors since 
	 * it reuses the errors object.
	 */
	private double validate(Validator validator, Object bean) {
        double result = -1;
        
        Errors errors = new MapBindingResult(new HashMap(), "person");

        // warmup
        for (int i = 0; i < count; i++) {
            validator.validate(bean, errors);
        }

        long start = System.nanoTime();
        
        for (int i = 0; i < count; i++) {
            validator.validate(bean, errors);
        }

        long end = System.nanoTime();
        
        result = ((end - start)/count);
        
        logger.info("Took {}ns.", result);
        
        assertTrue("Should not have any errors.", !errors.hasErrors());
        
        return result;
	}

}
