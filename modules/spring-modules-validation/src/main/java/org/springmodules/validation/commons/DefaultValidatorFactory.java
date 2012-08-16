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

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import org.apache.commons.validator.Form;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.xml.sax.SAXException;

/**
 * @author Daniel Miller
 * @author Rob Harrop
 */
public class DefaultValidatorFactory implements ValidatorFactory, InitializingBean {

    /**
     * Key used to store the Spring <code>Errors</code> instance in the <code>Validator</code>
     */
    public static final String ERRORS_KEY = "org.springframework.validation.Errors";

    /**
     * The <code>Log</code> instance used by this class.
     */
    private static Logger log = LoggerFactory.getLogger(DefaultValidatorFactory.class);

    /**
     * the Commons Validator <code>ValidatorResources</code> used to load validation configuration.
     */
    private ValidatorResources validatorResources;

    /**
     * Checks that the <code>ValidatorResources</code> exists and has been configured with resources
     * via a call to <code>setValidationConfigLocations</code>.
     *
     * @throws org.springframework.beans.FatalBeanException if <code>setValidationConfigLocations()</code> has not been called.
     */
    public void afterPropertiesSet() throws Exception {
        if (this.validatorResources == null) {
            throw new FatalBeanException("Unable to locate validation configuration. Property [validationLocations] is required.");
        }
    }

    /**
     * Sets the locations of the validation configuration files from which to load validation rules. Creates an instance
     * of <code>ValidatorResources</code> from the specified configuration files.
     *
     * @see org.springframework.core.io.Resource
     * @see ValidatorResources
     */
    public void setValidationConfigLocations(Resource[] validationConfigLocations) {

        if (DefaultValidatorFactory.log.isInfoEnabled()) {
            DefaultValidatorFactory.log.info("Loading validation configurations from [" + StringUtils.arrayToCommaDelimitedString(validationConfigLocations) + "]");
        }

        try {
            InputStream[] inputStreams = new InputStream[validationConfigLocations.length];

            for (int i = 0; i < inputStreams.length; i++) {
                inputStreams[i] = validationConfigLocations[i].getInputStream();
            }

            this.validatorResources = new ValidatorResources(inputStreams);
        }
        catch (IOException e) {
            throw new FatalBeanException("Unable to read validation configuration due to IOException.", e);
        }
        catch (SAXException e) {
            throw new FatalBeanException("Unable to parse validation configuration XML", e);
        }
    }

    /**
     * Gets a new instance of a <code>org.apache.commons.validator.Validator</code> for the given bean.
     *
     * @param beanName The name of the bean for which this <code>Validator</code> will be created
     * @see org.apache.commons.validator.Validator
     */
    public Validator getValidator(String beanName, Object bean, Errors errors) {
        Validator validator = new Validator(validatorResources, beanName);
        validator.setParameter(DefaultValidatorFactory.ERRORS_KEY, errors);
        validator.setParameter(Validator.BEAN_PARAM, bean);
        return validator;
    }


    /**
     * Returns true if this validator factory can create a validator that
     * supports the given <code>beanName</code> and <code>locale</code>.
     *
     * @param beanName name of the bean to be validated.
     * @param locale <code>Locale</code> to search under.
     * @return <code>true</code> if this validator factory can create a validator for
     *         the given bean name, <code>false otherwise.
     */
    public boolean hasRulesForBean(String beanName, Locale locale) {
        Form form = validatorResources.getForm(locale, beanName);
        return (form != null);
    }

    /**
     * Gets the managed instance of <code>ValidatorResources</code>.
     */
    public ValidatorResources getValidatorResources() {
        return validatorResources;
    }

}
