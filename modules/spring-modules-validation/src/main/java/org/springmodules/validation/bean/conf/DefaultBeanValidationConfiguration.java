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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.validation.Validator;
import org.springmodules.validation.bean.rule.ValidationRule;
import org.springmodules.validation.validator.CompoundValidator;

/**
 * A default implementation of {@link org.springmodules.validation.bean.conf.BeanValidationConfiguration}.
 *
 * @author Uri Boness
 */
public class DefaultBeanValidationConfiguration implements MutableBeanValidationConfiguration {

    // a list of all the required validatable property names (list elements are strings).
    private List cascadeValidations;

    // a list of all the global rules (list elements are ValidationRule's)
    private List globalRules;

    // the property validatoin rules (key: string - the property name, value: ValidationRule array)
    private Map rulesByProperty;

    // the custom validator for the bean.
    private CompoundValidator customValidator;

    /**
     * Constructs a new DefaultBeanValidationConfiguration with no rules, validatable properties, or custom validator.
     */
    public DefaultBeanValidationConfiguration() {
        cascadeValidations = new ArrayList();
        globalRules = new ArrayList();
        rulesByProperty = new HashMap();
        customValidator = new CompoundValidator();
    }

    /**
     * @see org.springmodules.validation.bean.conf.BeanValidationConfiguration#getGlobalRules()
     */
    public ValidationRule[] getGlobalRules() {
        return (ValidationRule[]) globalRules.toArray(new ValidationRule[globalRules.size()]);
    }

    /**
     * Sets the global rules for this configuration.
     *
     * @param globalRules The global rules for this configuration
     */
    public void setGlobalRules(ValidationRule[] globalRules) {
        List rules = new ArrayList();
        CollectionUtils.addAll(rules, globalRules);
        this.globalRules = rules;
    }

    /**
     * Adds the given rule as global rules for this configuration.
     *
     * @param globalRule The rule to be added as global rules for this configuration.
     */
    public void addGlobalRule(ValidationRule globalRule) {
        globalRules.add(globalRule);
    }

    /**
     * Adds the given rules as global rules for this configuration.
     *
     * @param globalRules The rules to be added as global rules for this configuration.
     */
    public void addGlobalRules(ValidationRule[] globalRules) {
        CollectionUtils.addAll(this.globalRules, globalRules);
    }

    /**
     * @see org.springmodules.validation.bean.conf.BeanValidationConfiguration#getPropertyRules(String).
     */
    public ValidationRule[] getPropertyRules(String propertyName) {
        List rules = (List) rulesByProperty.get(propertyName);
        if (rules == null || rules.isEmpty()) {
            return new ValidationRule[0];
        }
        return (ValidationRule[]) rules.toArray(new ValidationRule[rules.size()]);
    }

    /**
     * @see org.springmodules.validation.bean.conf.BeanValidationConfiguration#getValidatedProperties()
     */
    public String[] getValidatedProperties() {
        return (String[]) rulesByProperty.keySet().toArray(new String[rulesByProperty.size()]);
    }

    /**
     * Sets the property validation rules for the given property.
     *
     * @param propertyName The name of the property.
     * @param rules The validation rules for the given property.
     */
    public void setPropertyRules(String propertyName, ValidationRule[] rules) {
        List ruleList = new ArrayList();
        CollectionUtils.addAll(ruleList, rules);
        rulesByProperty.put(propertyName, ruleList);
    }

    /**
     * Adds the given validation rule to the given property.
     *
     * @param propertyName The name of the property.
     * @param rule The validation rule to be added to the given property.
     */
    public void addPropertyRule(String propertyName, ValidationRule rule) {
        List rules = (List) rulesByProperty.get(propertyName);
        if (rules == null) {
            rules = new ArrayList();
            rulesByProperty.put(propertyName, rules);
        }
        rules.add(rule);
    }

    /**
     * Adds the given validation rules to the given property.
     *
     * @param propertyName The name of the property.
     * @param extraRules The extra validation rules that will be added to the given property.
     */
    public void addPropertyRules(String propertyName, ValidationRule[] extraRules) {
        List rules = (List) rulesByProperty.get(propertyName);
        if (rules == null) {
            rules = new ArrayList();
            rulesByProperty.put(propertyName, rules);
        }
        CollectionUtils.addAll(rules, extraRules);
    }

    /**
     * @see org.springmodules.validation.bean.conf.BeanValidationConfiguration#getCustomValidator()
     */
    public Validator getCustomValidator() {
        return customValidator;
    }

    /**
     * @see org.springmodules.validation.bean.conf.BeanValidationConfiguration#getCascadeValidations()
     */
    public CascadeValidation[] getCascadeValidations() {
        return (CascadeValidation[]) cascadeValidations.toArray(new CascadeValidation[cascadeValidations.size()]);
    }

    /**
     * Sets the custom validator for this configuration.
     *
     * @param validator The custom validator for this configuration.
     */
    public void setCustomValidator(Validator validator) {
        setCustomValidators(new Validator[]{validator});
    }

    /**
     * Sets the custom validator for this configuration. The custom validator will be compound with the given
     * validators.
     *
     * @param validators The validators that will make the custom validator of this configuration.
     */
    public void setCustomValidators(Validator[] validators) {
        customValidator = new CompoundValidator(validators);
    }

    /**
     * Adds the given validator to the custom validator of this configuration.
     *
     * @param validator The validator to be added to the custom validator of this configuration.
     */
    public void addCustomValidator(Validator validator) {
        customValidator.addValidator(validator);
    }

    /**
     * Adds the given validators to the custom validator of this configuration.
     *
     * @param validators The validators to be added to the custom validator of this configuration.
     */
    public void addCustomValidators(Validator[] validators) {
        customValidator.addValidators(validators);
    }

    /**
     * Sets the cascade validations of this configuration.
     * <p/>
     * param cascadeValidations The cascade validations of this configuration.
     */
    public void setCascadeValidations(CascadeValidation[] cascadeValidations) {
        this.cascadeValidations = new ArrayList();
        for (int i = 0; i < cascadeValidations.length; i++) {
            this.cascadeValidations.add(cascadeValidations[i]);
        }
    }

    /**
     * Adds the given cascade validation to this configuration.
     *
     * @param cascadeValidation The cascase validation to be added to this configuration.
     */
    public void addCascadeValidation(CascadeValidation cascadeValidation) {
        addCascadeValidations(new CascadeValidation[]{cascadeValidation});
    }

    /**
     * Adds the given cascade validations to this configuration.
     *
     * @param cascadeValidations The cascade validation to be added to this configuration.
     */
    public void addCascadeValidations(CascadeValidation[] cascadeValidations) {
        for (int i = 0; i < cascadeValidations.length; i++) {
            if (!this.cascadeValidations.contains(cascadeValidations[i])) {
                this.cascadeValidations.add(cascadeValidations[i]);
            }
        }
    }

}
