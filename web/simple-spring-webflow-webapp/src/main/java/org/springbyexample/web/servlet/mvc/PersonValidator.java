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

package org.springbyexample.web.servlet.mvc;

import org.springbyexample.web.orm.entity.Person;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


/**
 * Person Spring Web Flow Validator.
 * Spring Web Flow activated validation based on bean name (${model} + 'Validator').
 * 
 * @author David Winterfeldt
 */
@Component
public class PersonValidator {

    /**
     * Spring Web Flow activated validation (validate + ${state}).
     * Validates 'personForm' view state after binding to person.
     */
    public void validatePersonForm(Person person, MessageContext context) {
        if (!StringUtils.hasText(person.getFirstName())) {
            context.addMessage(new MessageBuilder().error().source("firstName").code("person.form.firstName.error").build());
        }

        if (!StringUtils.hasText(person.getLastName())) {
            context.addMessage(new MessageBuilder().error().source("lastName").code("person.form.lastName.error").build());
        }
    }
    
}
