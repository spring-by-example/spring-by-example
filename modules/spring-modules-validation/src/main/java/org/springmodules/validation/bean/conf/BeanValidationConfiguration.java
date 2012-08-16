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

package org.springmodules.validation.bean.conf;

import org.springframework.validation.Validator;
import org.springmodules.validation.bean.rule.ValidationRule;

/**
 * Holds the various validatoin rules of a bean. This configuration is made out of the following configuration rules:
 * <ul>
 * <li>
 * <code>Global Rules</code> - Rules that are associated with the bean itself. Validation error of this rule will
 * be associated with the bean as well (see {@link org.springframework.validation.Errors#getGlobalError()}).
 * </li>
 * <li>
 * <code>Property Rules</code> - Rules that are associated with a specific property of the bean. Validation error
 * of this rule will be associated with the property as well
 * (see {@link org.springframework.validation.Errors#getFieldError(String)}). Note that the condition of these
 * rules however sould still be applied on the bean itself.
 * </li>
 * <li>
 * <code>Custom Validator</code> - This is not a validation rule per se, but a custom validator that can be
 * associated with the bean. This enables associating already exsiting or perhaps very complex validators with
 * the bean.
 * </li>
 * <li>
 * <code>Required Validatable Properties</code> - These are all the bean properties that need to be valid on their
 * own in order for the bean itself to be valid. A simple example can be a collection property, where in order
 * for the bean to be valid, all elements in the collection need to be valid as well.
 * </li>
 * </ul>
 *
 * @author Uri Boness
 */
public interface BeanValidationConfiguration {

    /**
     * Returns the global validation rules for the bean. This method never returns <code>null</code>. If no global rule
     * is defined, an empty array is returned.
     *
     * @return The global validation rules for the bean.
     */
    ValidationRule[] getGlobalRules();

    /**
     * Returns the property validation rules for the bean that are associated with the given property name. This method
     * never returns <code>null</code>. If no rule is associated with the given property, an empty array is returned.
     *
     * @param propertyName The name of the bean property.
     * @return The property validation rules for the bean.
     */
    ValidationRule[] getPropertyRules(String propertyName);

    /**
     * Returns a list of all properties that are associated with validation rules.
     *
     * @return A list of all properties that are associated with validation rules.
     */
    String[] getValidatedProperties();

    /**
     * Returns the custom validator associated with this configuration, <code>null</code> if no custom validator
     * is associated.
     *
     * @return The customer validator associated with this configuration.
     */
    Validator getCustomValidator();

    /**
     * Returns the configured cascade validations.
     *
     * @return The configured cascade validations.
     */
    CascadeValidation[] getCascadeValidations();

}
