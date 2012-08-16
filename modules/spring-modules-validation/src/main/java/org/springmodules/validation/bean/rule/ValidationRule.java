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

import org.springmodules.validation.util.condition.Condition;

/**
 * Represents a validation rule. A validation rule binds an error to a condition.
 *
 * @author Uri Boness
 */
public interface ValidationRule {

    /**
     * Checks whether this validation is applicable on the given object. This can be used
     * to define applicability rules base on runtime factors.
     *
     * @param obj The object to be validated
     * @return True if this validation rule is applicable on the given obj, false otherwise.
     */
    boolean isApplicable(Object obj);

    /**
     * Returns the condition of this validation rule.
     *
     * @return The condition of this validation rule.
     */
    Condition getCondition();

    /**
     * Return the error code of this validation rule. This method cannot return <code>null</code>.
     *
     * @return The error code of this validation rule.
     */
    String getErrorCode();

    /**
     * Returns the arguments that apply to the error code of this validation rule. This
     * method should never return null. If there are no arguments, this method must return
     * an empty array. This method accepts the validated object, this enables runtime generation
     * of arguments based on that object.
     *
     * @param obj The validated object.
     * @return The arguments that apply to the error code of this validation rule.
     */
    Object[] getErrorArguments(Object obj);

    /**
     * Returns the default error message that can be used in case no error message is bound
     * to the error code of the rule. This method can return <code>null</code> to indicate that
     * no default message exists.
     *
     * @return The default error message of this validation rule.
     */
    String getDefaultErrorMessage();

}
