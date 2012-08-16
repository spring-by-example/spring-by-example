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
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.common.AlwaysTrueCondition;
import org.springmodules.validation.bean.context.ValidationContextHolder;
import org.springmodules.validation.bean.context.ValidationContext;
import org.springmodules.validation.bean.context.ValidationContextUtils;

/**
 * A super class for all specific validation rules.
 *
 * @author Uri Boness
 */
public abstract class AbstractValidationRule implements ValidationRule {

    private String errorCode;

    private String defaultErrorMessage;

    private ErrorArgumentsResolver errorArgumentsResolver;

    private Condition applicabilityCondition;

    private String[] contextTokens;

    /**
     * Constructs a new AbstractValidationRule with a default error code. By default, the error arguments
     * of this validation rule will only return the validated object itself.
     *
     * @param defaultErrorCode
     */
    protected AbstractValidationRule(String defaultErrorCode) {
        this(defaultErrorCode, createErrorArgumentsResolver(new Object[0]));
    }

    /**
     * Constructs a new AbstractValidationRule with a default error code and error argument resolver.
     *
     * @param defaultErrorCode The default error code for this validation rule.
     * @param defaultErrorArgumentsResolver The argument resolver for this validation rule.
     */
    protected AbstractValidationRule(String defaultErrorCode, ErrorArgumentsResolver defaultErrorArgumentsResolver) {
        this.errorCode = defaultErrorCode;
        this.defaultErrorMessage = this.errorCode;
        this.errorArgumentsResolver = defaultErrorArgumentsResolver;
        this.applicabilityCondition = new AlwaysTrueCondition();
        this.contextTokens = null;
    }

    /**
     * Checks if this validation rule is applicable on a given object by performing the following two steps:
     * <ol>
     * <li>If the object is <code>null</code> and {@link #supportsNullValues()} returns <code>false</code>
     * then returning <code>false</code>.</li>
     * <li>Returning whatever the applicablity condition check returns for the given object
     * ({@link #setApplicabilityCondition(org.springmodules.validation.util.condition.Condition)})</li>
     *
     * @see ValidationRule#isApplicable(Object)
     */
    public boolean isApplicable(Object obj) {
        if (contextTokens != null && !checkContexts(contextTokens)) {
            return false;
        }
        if (obj == null && !supportsNullValues()) {
            return false;
        }
        return applicabilityCondition.check(obj);
    }

    /**
     * @see org.springmodules.validation.bean.rule.ValidationRule#getErrorCode()
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Returns the error arguments for this validation rule based on the given validated object. The arguments are
     * determined by the associated {@link ErrorArgumentsResolver}.
     *
     * @see #setErrorArgumentsResolver(org.springmodules.validation.bean.rule.resolver.ErrorArgumentsResolver)
     * @see ValidationRule#getErrorArguments(Object)
     */
    public Object[] getErrorArguments(Object obj) {
        return errorArgumentsResolver.resolveArguments(obj);
    }

    /**
     * @see org.springmodules.validation.bean.rule.ValidationRule#getDefaultErrorMessage()
     */
    public String getDefaultErrorMessage() {
        return defaultErrorMessage;
    }

    /**
     * Determines whether this validation rule supports <code>null</code> values. This method by default returns false,
     * any sub-class that supports <code>null</code> values should override this method and return <code>true</code>.
     *
     * @return whether this validation rule supports <code>null</code> values.
     */
    protected boolean supportsNullValues() {
        return false;
    }

    //================================================== Setters =======================================================

    /**
     * Sets the error code of this validation rule.
     *
     * @param errorCode The error code of this validation rule.
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Sets the default error message of this validation rule.
     *
     * @param defaultErrorMessage The default error message of this validation rule.
     */
    public void setDefaultErrorMessage(String defaultErrorMessage) {
        this.defaultErrorMessage = defaultErrorMessage;
    }

    /**
     * Sets the {@link ErrorArgumentsResolver} that will be used by this validation to extract the error arguments
     * based on the validated object.
     *
     * @param errorArgumentsResolver The error argument resolver that will be used by this validator.
     */
    public void setErrorArgumentsResolver(ErrorArgumentsResolver errorArgumentsResolver) {
        this.errorArgumentsResolver = errorArgumentsResolver;
    }

    /**
     * Sets the applicability condition that along with the {@link #supportsNullValues()} method determines whether
     * this condition is applicable on a given object or not.
     *
     * @param applicabilityCondition The applicability condition that will be used by this validation rule when deciding
     * whether it is applicable on a given object.
     */
    public void setApplicabilityCondition(Condition applicabilityCondition) {
        this.applicabilityCondition = applicabilityCondition;
    }

    /**
     * Sets the validation contexts names in which this validation rule is applicable.
     *
     * @param contextTokens The validation context names in which this validation rule is applicable. <code>null</code>
     *        represents applicability in any context (even if one doesn't exsit).
     */
    public void setContextTokens(String[] contextTokens) {
        this.contextTokens = contextTokens;
    }

    //=============================================== Helper Methods ===================================================

    /**
     * A helper method for sub-classes helping with the creation of error argument resolvers. The resolver created by
     * this method will return an array that contains the validated object and the given <code>arg</code> as the error
     * arguments.
     *
     * @param arg The error argument to be returned along with the validated object.
     * @return The created error arguments resolver.
     */
    protected static ErrorArgumentsResolver createErrorArgumentsResolver(Object arg) {
        return createErrorArgumentsResolver(new Object[]{arg});
    }

    /**
     * A helper method for sub-classes helping with the creation of error argument resolvers. The resolver created by
     * this method will return an array that contains the validated object and the given arguments as the error
     * arguments.
     *
     * @param arg1 The error argument to be returned in position {1}
     * @param arg2 The error argument to be returned in position {2}
     * @return The created error arguments resolver.
     */
    protected static ErrorArgumentsResolver createErrorArgumentsResolver(Object arg1, Object arg2) {
        return createErrorArgumentsResolver(new Object[]{arg1, arg2});
    }

    /**
     * A helper method for sub-classes helping with the creation of error argument resolvers. The resolver created by
     * this method will return an array that contains the validated object and the given arguments as the error
     * arguments.
     *
     * @param arg1 The error argument to be returned in position {1}
     * @param arg2 The error argument to be returned in position {2}
     * @param arg3 The error argument to be returned in position {3}
     * @return The created error arguments resolver.
     */
    protected static ErrorArgumentsResolver createErrorArgumentsResolver(Object arg1, Object arg2, Object arg3) {
        return createErrorArgumentsResolver(new Object[]{arg1, arg2, arg3});
    }

    /**
     * A helper method for sub-classes helping with the creation of error argument resolvers. The resolver created by
     * this method will return an array that contains the validated object and the given arguments as the error
     * arguments.
     *
     * @param arguments The error arguments that will be return along with the validated object, starting in position {1}.
     * @return The created error arguments resolver.
     */
    protected static ErrorArgumentsResolver createErrorArgumentsResolver(final Object[] arguments) {
        return new ErrorArgumentsResolver() {
            public Object[] resolveArguments(Object obj) {
                Object[] result = new Object[arguments.length + 1];
                System.arraycopy(arguments, 0, result, 1, arguments.length);
                result[0] = obj;
                return result;
            }
        };
    }

    protected static boolean checkContexts(String[] contextTokens) {
        return ValidationContextUtils.tokensSupportedByCurrentContext(contextTokens);
    }

}
