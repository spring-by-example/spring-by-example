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

package org.springmodules.validation.bean;

import java.util.*;
import java.util.Map.Entry;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springmodules.validation.bean.rule.DefaultValidationRule;
import org.springmodules.validation.bean.rule.PropertyValidationRule;
import org.springmodules.validation.bean.rule.ValidationRule;
import org.springmodules.validation.util.condition.Condition;

/**
 * A {@link org.springframework.validation.Validator} implementation which uses {@link org.springmodules.validation.bean.rule.ValidationRule}'s to define its
 * validation execution. There are two types of validation rules this validator accepts:
 * <ul>
 * <li>
 * Global Rules - Rules that apply on the validated objects and where all validation errors are registered
 * globaly with the {@link Errors} object (i.e. {@link Errors#reject(String)}).
 * </li>
 * <li>
 * Property Rules - Rules that apply on specific properties of the validated objects and where all validation
 * errors are registered with the {@link Errors} object under the context of those properties
 * (i.e. {@link Errors#rejectValue(String, String)}).
 * </li>
 * </ul>
 *
 * @author Uri Boness
 */
public class RuleBasedValidator implements Validator {

    // a list of global ValidationRule's - List<ValidationRule>
    private List globalRules;

    // maps a list of ValidationRule's to propertyNames - Map<String, List<ValidationRule>>
    private Map rulesByProperty;

    /**
     * Contrusts a new RuleBasedValidator for the given type. After contruction, this validator will initially hold
     * no rules.
     */
    public RuleBasedValidator() {
        globalRules = new ArrayList();
        rulesByProperty = new HashMap();
    }

    /**
     * This validator supports all classes. Any object can be validated by this validator as long as the validation rules
     * apply to it.
     *
     * @see Validator#supports(Class)
     */
    public boolean supports(Class clazz) {
        return true;
    }

    /**
     * Validates the given object and registers all validation errors with the given errors object. The validation
     * is done by applying all validation rules associated with this validator on the given object.
     *
     * @see org.springframework.validation.Validator#validate(Object, org.springframework.validation.Errors)
     */
    public void validate(Object obj, Errors errors) {

        // validating using the registered global rules
        for (Iterator iter = globalRules.iterator(); iter.hasNext();) {
            ValidationRule rule = (ValidationRule) iter.next();
            if (rule.isApplicable(obj) && !rule.getCondition().check(obj)) {
                errors.reject(rule.getErrorCode(), rule.getErrorArguments(obj), rule.getDefaultErrorMessage());
            }
        }

        // validating using the registered field rules
        for (Iterator names = rulesByProperty.keySet().iterator(); names.hasNext();) {
            String propertyName = (String) names.next();
            List rules = (List) rulesByProperty.get(propertyName);
            for (Iterator iter = rules.iterator(); iter.hasNext();) {
                ValidationRule rule = (ValidationRule) iter.next();
                if (rule.isApplicable(obj) && !rule.getCondition().check(obj)) {
                    errors.rejectValue(propertyName, rule.getErrorCode(), rule.getErrorArguments(obj), rule.getDefaultErrorMessage());
                }
            }
        }
    }

    //====== Setters to support JavaBean based configuration environment (e.g. spring's application context ===========

    /**
     * Sets extra global validation rules for this validator.
     *
     * @param globalRules The extra global validation rules to be added to this validator.
     */
    public void setExtraGlobalVadlidationRules(ValidationRule[] globalRules) {
        for (int i = 0; i < globalRules.length; i++) {
            addGlobalRule(globalRules[i]);
        }
    }

    /**
     * Sets extra property validation rules for this validator.
     *
     * @param rulesByProperty The extra property validation rules for this validator. The map should hold the property
     * names as keys and {@link ValidationRule} instances as values.
     */
    public void setExtraPropertyValidationRules(Map rulesByProperty) {
        for (Iterator entries = rulesByProperty.entrySet().iterator(); entries.hasNext();) {
            Entry entry = (Entry) entries.next();
            addPropertyRule((String) entry.getKey(), (ValidationRule) entry.getValue());
        }
    }

    //=================================== Property Rules Regitration Methods ==========================================

    /**
     * Adds the given property rule to this validator. A property rule is a condition and error information that is
     * associated with a property name. When this rule is applied on an object, the condition is checked against the
     * value of the associated property of the (validated) object. All validation errors of this rule will be registered
     * with the {@link Errors} object under the context of the associated property. Note that the associated property
     * may be a nested property - in that case, the nested property value will be resolved and the condition will be
     * applied on the this value.
     *
     * @param propertyName The name of the property the added rule is associated with.
     * @param fieldValueCondition The condition of the rule.
     * @param errorCode The error code of the rule.
     */
    public void addPropertyRule(String propertyName, Condition fieldValueCondition, String errorCode) {
        addPropertyRule(propertyName, fieldValueCondition, errorCode, errorCode, new Object[0]);
    }

    /**
     * Adds the given property rule to this validator.
     *
     * @param propertyName The name of the property the added rule is associated with.
     * @param fieldValueCondition The condition of the rule.
     * @param errorCode The error code of the rule.
     * @param args The arguments of the error code of the rule.
     * @see #addPropertyRule(String, org.springmodules.validation.util.condition.Condition, String, Object[])
     */
    public void addPropertyRule(String propertyName, Condition fieldValueCondition, String errorCode, Object[] args) {
        addPropertyRule(propertyName, fieldValueCondition, errorCode, errorCode, args);
    }

    /**
     * Adds the given property rule to this validator.
     *
     * @param propertyName The name of the property the added rule is associated with.
     * @param fieldValueCondition The condition of the rule.
     * @param errorCode The error code of the rule.
     * @param message The default error message of the rule.
     * @param args The arguments of the error code of the rule.
     * @see #addPropertyRule(String, org.springmodules.validation.util.condition.Condition, String, Object[])
     */
    public void addPropertyRule(
        String propertyName,
        Condition fieldValueCondition,
        String errorCode,
        String message,
        Object[] args) {

        addPropertyRule(propertyName, new DefaultValidationRule(fieldValueCondition, errorCode, message, args));
    }

    /**
     * Adds the given property rule for the given property.
     *
     * @param propertyName The name of the property associated with the added rule.
     * @param propertyRule The rule that should be applied on the value of the given property.
     * @see #addPropertyRule(String, org.springmodules.validation.util.condition.Condition, String)
     */
    public void addPropertyRule(String propertyName, ValidationRule propertyRule) {
        addPropertyGlobalRule(propertyName, new PropertyValidationRule(propertyName, propertyRule));
    }

    /**
     * Adds a property rule for the given property. The given rule is actually a global rule, that is, it is being
     * evaluated on the validated object, not on the property value. The only difference between this added rule and
     * a global rule is that the validation errors of this rule are associated with the given property and are not
     * associated globaly with the validated object.
     * <p/>
     * This type of rule comes in handy when a complex validation is required on the validation object, but the error
     * is should only be associated with a specific property. An example would be when two properties of the validated
     * object should match (i.e. have the same value), but if they don't, the error will only be associated with one
     * of them (e.g. when a UserRegistration object holds a password and confirmPassword properties - this should
     * generally match, but if they don't, the error will only be associated with the confirmPassword property).
     *
     * @param propertyName The name of the property to associated with the added rule.
     * @param globalRule The global rule to be added.
     */
    public void addPropertyGlobalRule(String propertyName, ValidationRule globalRule) {
        List rules = (List) rulesByProperty.get(propertyName);
        if (rules == null) {
            rules = new ArrayList();
            rulesByProperty.put(propertyName, rules);
        }
        rules.add(globalRule);
    }

    //==================================== Global Rules Regitration Methods ==========================================

    /**
     * Adds a new global validation rule to this validator. The global validation rule applied on the object level and
     * all validation error of this rule will be registered with the {@link Errors} object globaly
     * (see {@link Errors#reject(String)}).
     *
     * @param condition The condition of the added rule.
     * @param errorCode The error code of the added rule.
     */
    public void addGlobalRule(Condition condition, String errorCode) {
        addGlobalRule(condition, errorCode, errorCode, new Object[0]);
    }

    /**
     * Adds a new global validation rule to this validator.
     *
     * @param condition The condition of the added rule.
     * @param errorCode The error code of the added rule.
     * @param args The arguments for the error of the added rule.
     * @see #addGlobalRule(org.springmodules.validation.util.condition.Condition, String)
     */
    public void addGlobalRule(Condition condition, String errorCode, Object[] args) {
        addGlobalRule(condition, errorCode, errorCode, args);
    }

    /**
     * Adds a new global validation rule to this validator.
     *
     * @param condition The condition of the added rule.
     * @param errorCode The error code of the added rule.
     * @param message The error message of the added rule.
     * @param args The arguments for the error of the added rule.
     * @see #addGlobalRule(org.springmodules.validation.util.condition.Condition, String)
     */
    public void addGlobalRule(Condition condition, String errorCode, String message, Object[] args) {
        addGlobalRule(new DefaultValidationRule(condition, errorCode, message, args));
    }

    /**
     * Adds a new global validation rule to this validator.
     *
     * @param condition The condition of the added rule.
     * @param errorCode The error code of the added rule.
     * @param message The default error message of the added rule.
     * @see #addGlobalRule(org.springmodules.validation.util.condition.Condition, String)
     */
    public void addGlobalRule(Condition condition, String errorCode, String message) {
        addGlobalRule(new DefaultValidationRule(condition, errorCode, message, new Object[0]));
    }

    /**
     * Adds the given validation rule as a global rule to this validator.
     *
     * @param globalRule The global rule to be added to this validator.
     */
    public void addGlobalRule(ValidationRule globalRule) {
        globalRules.add(globalRule);
    }

}
