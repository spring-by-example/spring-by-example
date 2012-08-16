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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * A validator that is compound of aother validators. This validator deligates the validation
 * task to all its contained validators.
 *
 * @author Uri Boness
 */
public class CompoundValidator implements Validator {

    // the validators this validator is compound of.
    private List validators;

    /**
     * Constructs a new CompoundValidator with no internal validators.
     */
    public CompoundValidator() {
        this(new Validator[0]);
    }

    /**
     * Constructs a new CompoundValidator with the given internal validators.
     *
     * @param validators The internal validators this validator is compound of.
     */
    public CompoundValidator(Validator[] validators) {
        setValidators(validators);
    }

    /**
     * Returns whether this validator supports the given class. In practice this validator
     * supports the given class only if any of its internal validators support it.
     *
     * @param clazz The class to be validated.
     * @return Whether this validator supports the given class.
     * @see Validator#supports(Class)
     */
    public boolean supports(Class clazz) {
        for (Iterator i = validators.iterator(); i.hasNext();) {
            Validator validator = (Validator) i.next();
            if (validator.supports(clazz)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates the given object. All internal validators that support the given object class
     * will perform the actual validation.
     *
     * @param obj The validated object.
     * @param errors A registry where validation errors are registered.
     * @see Validator#validate(Object, org.springframework.validation.Errors)
     */
    public void validate(Object obj, Errors errors) {
        for (Iterator i = validators.iterator(); i.hasNext();) {
            Validator validator = (Validator) i.next();
            if (validator.supports(obj.getClass())) {
                validator.validate(obj, errors);
            }
        }
    }

    /**
     * Adds the given validator to this compound validator.
     *
     * @param validator The validator to be added to this compound validator.
     */
    public void addValidator(Validator validator) {
        validators.add(validator);
    }

    /**
     * Adds the given validators to this compound validator.
     *
     * @param validators The validators to be added to this compound validator.
     */
    public void addValidators(Validator[] validators) {
        for (int i = 0; i < validators.length; i++) {
            addValidator(validators[i]);
        }
    }

    /**
     * Sets the internal validators of this compound validator.
     *
     * @param validators The internal validators of this compound validator.
     */
    public void setValidators(Validator[] validators) {
        List validatorList = new ArrayList(validators.length);
        for (int i = 0; i < validators.length; i++) {
            validatorList.add(validators[i]);
        }
        this.validators = validatorList;
    }


}
