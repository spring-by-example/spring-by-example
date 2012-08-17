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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ValangValidatorTest {

//    @Autowired
//    @Qualifier("testCustomFunctions")
//    private Validator customFunctions01Validator = null;

    @Autowired
    @Qualifier("testFunctionFromApplicationContext")
    private Validator customFunctions03validator = null;

    @Autowired
    @Qualifier("personValidator")
    private Validator personValidator = null;

//    @Test
//    public void testCustomFunctions() {
//        LifeCycleBean lifeCycleBean = new LifeCycleBean();
//        Person person = new Person();
//        person.setLifeCycleBean(lifeCycleBean);
//        person.setFirstName("FN");
//
//        BindException errors = new BindException(person, "person");
//        customFunctions01Validator.validate(person, errors);
//
//        assertTrue(lifeCycleBean.isApplicationContextSet());
//        assertTrue(lifeCycleBean.isApplicationEventPublisher());
//        assertTrue(lifeCycleBean.isBeanFactorySet());
//        assertTrue(lifeCycleBean.isMessageSourceSet());
//        assertTrue(lifeCycleBean.isResourceLoaderSet());
//
//        assertTrue(errors.hasFieldErrors("firstName"));
//    }

    @Test
    public void testFunctionsFromApplicationContext() {
        Person person = new Person();
        person.setFirstName("FN");
        person.setFirstName("LN");

        BindException errors = new BindException(person, "person");
        customFunctions03validator.validate(person, errors);

        assertTrue(errors.hasFieldErrors("firstName"));
    }

    @Test
    public void testPersonValidator() {
        Person person = new Person();
        Map map = new HashMap();
        map.put("name", "Steven");
        map.put("passwd", "pas");
        person.setForm(map);
        Errors errors = new BindException(person, "person");
        personValidator.validate(person, errors);

        assertFalse(errors.hasFieldErrors("form[name]"));
        assertTrue(errors.hasFieldErrors("form[passwd]"));
    }

    @Test
    public void testCustomFunction() throws Exception {
        ValangValidator validator = new ValangValidator();
        validator.addCustomFunction("tupper", "org.springmodules.validation.valang.functions.UpperCaseFunction");
        validator.setValang("{ firstName : tupper(?) == 'FN' : 'First name is empty' }");
        validator.afterPropertiesSet();

        Person person = new Person();
        person.setFirstName("fn");
        BindException errors = new BindException(person, "person");
        validator.validate(person, errors);

        assertFalse(errors.hasErrors());
    }

    //================================================ Inner Classes ===================================================

    private class Person {

        private String firstName;
        private String lastName;
        private Map form;

        public Map getForm() {
            return form;
        }

        public void setForm(Map form) {
            this.form = form;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }

}
