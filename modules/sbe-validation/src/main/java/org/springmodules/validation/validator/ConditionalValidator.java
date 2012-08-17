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

package org.springmodules.validation.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springmodules.validation.util.condition.Condition;

/**
 * A validator that is associated with a condition that determines whether it should
 * be applied on a given object or not.
 *
 * @author Uri Boness
 */
public class ConditionalValidator implements Validator {

    private Condition condition;

    private Validator validator;

    public ConditionalValidator() {
    }

    /**
     * Constructs a new ConditionalValidator with a given underlying validator and
     * the condition.
     *
     * @param validator The underlying validator.
     * @param condition The condition.
     */
    public ConditionalValidator(Validator validator, Condition condition) {
        this.validator = validator;
        this.condition = condition;
    }

    /**
     * Returns whether this validation supports the given class. The call is practically
     * being deligated to the unerlying validator.
     *
     * @param clazz The class to be validated.
     * @return True if this validator supports the given class, false otherwise.
     */
    public boolean supports(Class clazz) {
        return validator.supports(clazz);
    }

    /**
     * Validates the given objects only if the given object adheres to the associated condition.
     *
     * @param obj The validated object.
     * @param errors The registery where validation error codes will be registered.
     */
    public void validate(Object obj, Errors errors) {
        if (condition.check(obj)) {
            validator.validate(obj, errors);
        }
    }

    //============================================= Setter/Getter ===================================================

    /**
     * Sets the condition of this conditional validator.
     *
     * @param condition The condition of this conditional validator.
     */
    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    /**
     * Returns the condition associated with this conditional validator.
     *
     * @return The condition associated with this conditional validator.
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * Sets the underlying validator.
     *
     * @param validator The underlying validator.
     */
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    /**
     * Returns the underlying validator.
     *
     * @return The underlying validator.
     */
    public Validator getValidator() {
        return validator;
    }

}
