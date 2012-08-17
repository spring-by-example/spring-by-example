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

package org.springmodules.validation.bean.rule;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springmodules.validation.util.condition.AbstractCondition;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.Conditions;
import org.springmodules.validation.bean.context.ValidationContextUtils;

/**
 * A {@link ValidationRule} implementation that wraps another validation rule and is associated with a specific
 * property name. This validation rule is applicable on an object only if the object has the associated property and
 * if wrapped rule is applicable on the value of that property. The condition of the rule is condition of the wrapped
 * rule applied on the value of the associated property of the object.
 *
 * @author Uri Boness
 */
public class PropertyValidationRule implements ValidationRule {

    // the name of the associated property.
    private String propertyName;

    // the wrapped validation rule.
    private ValidationRule rule;

    private Condition applicabilityCondition;

    private String[] contextTokens;

    /**
     * Constructs a new PropertyValidationRule (javabean support).
     */
    public PropertyValidationRule() {
        this(null, null);
    }

    /**
     * Constructs a new PropertyValidationRule with a given property and a wrapped validation rule.
     *
     * @param propertyName The name of the associated property.
     * @param rule The validation rule to be wrapped.
     */
    public PropertyValidationRule(String propertyName, ValidationRule rule) {
        this.propertyName = propertyName;
        this.rule = rule;
        applicabilityCondition = new DefaultApplicabilityCondition(propertyName, rule);
        contextTokens = null;
    }

    /**
     * Determines whether this rule is applicable on the given object. In practice, this validation rule is applicable
     * only if the given object a property that match the property associated with this rule and if the wrapped rule
     * is applicable on the value of that property.
     *
     * @param obj The object to be validated.
     * @return <code>true</code> if this rule is applicable on the given object, <code>false</code> otherwise.
     * @see ValidationRule#isApplicable(Object)
     */
    public boolean isApplicable(Object obj) {
        return checkContext(contextTokens) && applicabilityCondition.check(obj);
    }

    /**
     * Returns the conditoin of this validation rule. In practice, applying this condition means applying the
     * condition of the wrapped rule on the value of the associated property of the validated object.
     *
     * @return The condition of this validation rule.
     * @see ValidationRule#getCondition()
     */
    public Condition getCondition() {
        return Conditions.property(propertyName, rule.getCondition());
    }

    /**
     * Returns the error code of this condition. This error code is the same as the error code of the wrapped rule.
     *
     * @return The error code of this condition.
     */
    public String getErrorCode() {
        return rule.getErrorCode();
    }

    /**
     * Returns the arguments for the error of this validation rule. These arguments are the same as the ones of the
     * wrapped rule.
     *
     * @param obj The validated object.
     * @return The arguments for the error of this validation rule.
     */
    public Object[] getErrorArguments(Object obj) {
        return rule.getErrorArguments(obj);
    }

    /**
     * Returns the default error message of this validation rule. This error message is the same as the error message
     * of the wrapped rule.
     *
     * @return The default error message of this validation rule.
     */
    public String getDefaultErrorMessage() {
        return rule.getDefaultErrorMessage();
    }


    public void setApplicabilityCondition(Condition applicabilityCondition) {
        this.applicabilityCondition = applicabilityCondition;
    }

    public void setContextTokens(String[] contextTokens) {
        this.contextTokens = contextTokens;
    }

    protected static boolean checkContext(String[] tokens) {
        return ValidationContextUtils.tokensSupportedByCurrentContext(tokens);
    }

    //=================================================== Inner Classes ================================================

    protected static class DefaultApplicabilityCondition extends AbstractCondition {

        private String propertyName;
        private ValidationRule rule;

        public DefaultApplicabilityCondition(String propertyName, ValidationRule rule) {
            this.propertyName = propertyName;
            this.rule = rule;
        }

        public boolean doCheck(Object obj) {
            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(obj.getClass(), propertyName);
            if (propertyDescriptor == null) {
                return false;
            }
            Object value = new BeanWrapperImpl(obj).getPropertyValue(propertyName);
            return rule.isApplicable(value);
        }

    }
}
