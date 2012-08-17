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

package org.springmodules.validation.commons;

import java.util.Locale;

import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorResources;
import org.springframework.validation.Errors;

/**
 * @author Daniel Miller
 */
public interface ValidatorFactory {

    /**
     * Gets a new instance of a validator for the given bean (form).
     *
     * @param beanName The name of the bean for which this validator will be created.
     */
    public Validator getValidator(String beanName, Object bean, Errors errors);

    /**
     * Returns true if this validator factory can create a validator that
     * supports the given <code>beanName</code> and <code>locale</code>.
     *
     * @param beanName String name of the bean to be validated.
     * @param locale Locale of the validator to create.
     * @return true if this validator factory can create a validator for the
     *         given bean name.
     */
    public boolean hasRulesForBean(String beanName, Locale locale);

    /**
     * @return Returns the validatorResources.
     */
    public ValidatorResources getValidatorResources();
}
