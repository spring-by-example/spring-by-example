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

import org.springmodules.validation.bean.rule.resolver.ErrorArgumentsResolver;
import org.springmodules.validation.bean.rule.resolver.StaticErrorArgumentsResolver;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.common.AlwaysTrueCondition;

/**
 * The default implementation of the {@link org.springmodules.validation.bean.rule.ValidationRule} interface. This implementation uses an applicability condition
 * to determine whether this rule is applicable on a given object (see {@link ValidationRule#isApplicable(Object)}).
 *
 * @author Uri Boness
 */
public class DefaultValidationRule implements ValidationRule {

    // the default applicability condition of this validation rule - always applicable.
    private final static Condition DEFAULT_APPLICABILITY_CONDITION = new AlwaysTrueCondition();

    // the condition of this validation rule.
    private Condition condition;

    // determines whether this validation rule is applicable on a given object.
    private Condition applicabilityCondition;

    // the error code of this validation rule.
    private String errorCode;

    // the default error message of this validation rule.
    private String defalutErrorMessage;

    // the resolver to be used to resolve the error arguments.
    private ErrorArgumentsResolver errorArgumentsResolver;

    /**
     * Empty contructor (javabean support).
     */
    public DefaultValidationRule() {
        this(null, null);
    }

    /**
     * Constructs a new DefaultValidationRule with the given condition and error code. The condition
     * is always applicable.
     *
     * @param condition The condition of this validation rule.
     * @param errorCode The error code of this validation rule.
     */
    public DefaultValidationRule(Condition condition, String errorCode) {
        this(condition, DEFAULT_APPLICABILITY_CONDITION, errorCode, errorCode, new Object[0]);
    }

    /**
     * Constructs a new DefaultValidationRule with the given condition, error code, and error arguments. The condition
     * is always applicable.
     *
     * @param condition The condition of this validation rule.
     * @param errorCode The error code of this validation rule.
     * @param errorArguments The error arguments of this validation rule.
     */
    public DefaultValidationRule(Condition condition, String errorCode, Object[] errorArguments) {
        this(condition, DEFAULT_APPLICABILITY_CONDITION, errorCode, errorCode, errorArguments);
    }

    /**
     * Constructs a new DefaultValidationRule with the given condition and error information. The condition is always
     * applicable.
     *
     * @param condition The condition of this validation rule.
     * @param errorCode The error code of this validation rule.
     * @param defalutErrorMessage The default error message of this validation rule.
     * @param errorArguments The error arguments of this validation rule.
     */
    public DefaultValidationRule(
        Condition condition,
        String errorCode,
        String defalutErrorMessage,
        Object[] errorArguments) {

        this(condition, DEFAULT_APPLICABILITY_CONDITION, errorCode, defalutErrorMessage, errorArguments);
    }

    /**
     * Constructs a new DefaultValidationRule with given condition and error code. The applicability of this rule
     * is determined by the given applicability condition.
     *
     * @param condition The condition of this validation rule.
     * @param applicabilityCondition Determines whether this rule is applicable on a given object.
     * @param errorCode The error code of this validation rule.
     */
    public DefaultValidationRule(Condition condition, Condition applicabilityCondition, String errorCode) {
        this(condition, applicabilityCondition, errorCode, errorCode, new Object[0]);
    }

    /**
     * Constructs a new DefaultValidationRule with given condition, error code, and error arguments. The applicability
     * of this rule is determined by the given applicability condition.
     *
     * @param condition The condition of this validation rule.
     * @param applicabilityCondition Determines whether this rule is applicable on a given object.
     * @param errorCode The error code of this validation rule.
     * @param errorArguments The error arguments of this validation rule.
     */
    public DefaultValidationRule(
        Condition condition,
        Condition applicabilityCondition,
        String errorCode,
        Object[] errorArguments) {

        this(condition, applicabilityCondition, errorCode, errorCode, errorArguments);
    }

    /**
     * Constructs a new DefaultValidationRule with given condition, error code, error arguments, and default error
     * message. The applicability of this rule is determined by the given applicability condition.
     *
     * @param condition The condition of this validation rule.
     * @param applicabilityCondition Determines whether this rule is applicable on a given object.
     * @param errorCode The error code of this validation rule.
     * @param defalutErrorMessage The default error message of this validation rule.
     * @param errorArguments The error arguments of this validation rule.
     */
    public DefaultValidationRule(
        Condition condition,
        Condition applicabilityCondition,
        String errorCode,
        String defalutErrorMessage,
        Object[] errorArguments) {

        this(condition, applicabilityCondition, errorCode, defalutErrorMessage, new StaticErrorArgumentsResolver(errorArguments));
    }

    /**
     * Constructs a new DefaultValidationRule with given condition, error code, error arguments resolver, and default error
     * message. The applicability of this rule is determined by the given applicability condition.
     *
     * @param condition The condition of this validation rule.
     * @param applicabilityCondition Determines whether this rule is applicable on a given object.
     * @param errorCode The error code of this validation rule.
     * @param defalutErrorMessage The default error message of this validation rule.
     * @param errorArgumentsResolver The resolver that will be used to resolve the error arguments.
     */
    public DefaultValidationRule(
        Condition condition,
        Condition applicabilityCondition,
        String errorCode,
        String defalutErrorMessage,
        ErrorArgumentsResolver errorArgumentsResolver) {

        this.condition = condition;
        this.applicabilityCondition = applicabilityCondition;
        this.errorCode = errorCode;
        this.errorArgumentsResolver = errorArgumentsResolver;
        this.defalutErrorMessage = defalutErrorMessage;
    }

    /**
     * see {@link ValidationRule#isApplicable(Object)}.
     * <p/>
     * The applicability of this validation rule is determined by the applicability condition.
     * see {@link #getApplicabilityCondition()}.
     */
    public boolean isApplicable(Object obj) {
        return applicabilityCondition.check(obj);
    }

    /**
     * see {@link org.springmodules.validation.bean.rule.ValidationRule#getCondition()}
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * Sets the condition of this validation rule. see {@link #getCondition()}.
     *
     * @param condition The condition of this validation rule.
     */
    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    /**
     * See {@link org.springmodules.validation.bean.rule.ValidationRule#getErrorCode()}
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the error code of this validation rule. see {@link #getErrorCode()}.
     *
     * @param errorCode The error code of this validation rule.
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * See {@link org.springmodules.validation.bean.rule.ValidationRule#getErrorArguments(Object)}
     */
    public Object[] getErrorArguments(Object obj) {
        return errorArgumentsResolver.resolveArguments(obj);
    }

    /**
     * Sets the arguments to attach to the error code of this validation rule. see {@link #getErrorArguments(Object)}.
     *
     * @param errorArguments The arguments to attach to the error code of this validation rule.
     */
    public void setErrorArguments(Object[] errorArguments) {
        this.errorArgumentsResolver = new StaticErrorArgumentsResolver(errorArguments);
    }

    /**
     * Sets the error arguments resolver to be used by this validation rule to resolve the error arguments.
     *
     * @param errorArgumentsResolver The given error arguments resolver.
     */
    public void setErrorArgumentsResolver(ErrorArgumentsResolver errorArgumentsResolver) {
        this.errorArgumentsResolver = errorArgumentsResolver;
    }

    /**
     * See {@link org.springmodules.validation.bean.rule.ValidationRule#getDefaultErrorMessage()}.
     */
    public String getDefaultErrorMessage() {
        return defalutErrorMessage;
    }

    /**
     * Sets the default error message to be used in case no error message is associated with the error code
     * of this validation rule. See {@link #getDefaultErrorMessage()}.
     *
     * @param defalutErrorMessage The default error message of this validation rule.
     */
    public void setDefalutErrorMessage(String defalutErrorMessage) {
        this.defalutErrorMessage = defalutErrorMessage;
    }

    //=============================================== Setter/Getter ====================================================

    /**
     * Returns the applicability condition of this validation rule. This applicability condition determines whether
     * this validation rule is applicable for a given object.
     *
     * @return The applicability condition of this validation rule.
     */
    public Condition getApplicabilityCondition() {
        return applicabilityCondition;
    }

    /**
     * Sets the applicability condition of this validation rule. see {@link #getApplicabilityCondition()}.
     *
     * @param applicabilityCondition The applicability condition of this validation rule.
     */
    public void setApplicabilityCondition(Condition applicabilityCondition) {
        this.applicabilityCondition = applicabilityCondition;
    }

}
