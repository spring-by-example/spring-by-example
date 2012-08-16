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

package org.springmodules.validation.valang.predicates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.Predicate;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springmodules.validation.valang.functions.Function;


/**
 * <p>Validation rule implementation that will validate a target
 * bean an return an error message is the validation fails.
 *
 * @author Steven Devijver
 * @since 23-04-2005
 */
public class BasicValidationRule implements ValidationRule {

    private final Predicate predicate;
    private final Predicate wherePredicate;
    private final String defaultPropertyName;
    private final String errorMessage;
    private final String errorKey;
    private final Collection<Function> errorArgs;

    /**
     * Constructor.
     * 
     * @param       field           Default property/field name.
     * @param       predicate       Predicate used for comparison.
     * @param       errorMessage    Default error message.
     */
    public BasicValidationRule(String defaultPropertyName, Predicate predicate, String errorMessage) {
        Assert.notNull(defaultPropertyName, "Field parameter must not be null.");
        Assert.notNull(predicate, "Predicate parameter must not be null.");
        Assert.notNull(errorMessage, "Error message parameter must not be null.");

        this.defaultPropertyName = defaultPropertyName;
        this.predicate = predicate;
        this.wherePredicate = null;
        this.errorMessage = errorMessage;
        this.errorKey = null;
        this.errorArgs = null;
    }

    /**
     * Constructor.
     * 
     * @param       field               Default property/field name.
     * @param       predicate           Predicate used for comparison.
     * @param       wherePredicate      Where predicate used for whether or not validation should be performed.
     * @param       errorKey            Message resource key for the error.
     * @param       errorMessage        Default error message.
     * @param       errorArgs           Error message argument.
     */
    public BasicValidationRule(String defaultPropertyName, Predicate predicate, Predicate wherePredicate,
                               String errorKey, String errorMessage, Collection<Function> errorArgs) {
        Assert.notNull(defaultPropertyName, "Field parameter must not be null.");
        Assert.notNull(predicate, "Predicate parameter must not be null.");
        Assert.notNull(errorMessage, "Error message parameter must not be null.");

        this.defaultPropertyName = defaultPropertyName;
        this.predicate = predicate;
        this.wherePredicate = wherePredicate;
        this.errorMessage = errorMessage;
        this.errorKey = errorKey;
        this.errorArgs = errorArgs;
        
    }

    /**
     * Gets predicate.
     */
    public Predicate getPredicate() {
        return this.predicate;
    }

//    /**
//     * Sets predicate.
//     */
//    private void setPredicate(Predicate predicate) {
//        Assert.notNull(predicate, "Predicate parameter must not be null.");
//
//        this.predicate = predicate;
//    }

    /**
     * Gets error message.
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }

//    /**
//     * Sets error message.
//     */
//    private void setErrorMessage(String errorMessage) {
//        Assert.notNull(errorMessage, "Error message parameter must not be null.");
//
//        this.errorMessage = errorMessage;
//    }

    /**
     * Gets field.
     */
    public String getField() {
        return this.defaultPropertyName;
    }

//    /**
//     * Sets field.
//     */
//    private void setField(String field) {
//        Assert.notNull(field, "Field parameter must not be null.");
//
//        this.defaultPropertyName = field;
//    }

//    private void setErrorKey(String errorKey) {
//        this.errorKey = errorKey;
//    }

    /**
     * Gets error key.
     */
    public String getErrorKey() {
        return this.errorKey;
    }

//    private void setErrorArgs(Collection errorArgs) {
//        this.errorArgs = errorArgs;
//    }

    /**
     * Get error args.
     */
    public Collection<Function> getErrorArgs() {
        return this.errorArgs;
    }

    /**
     * Validates bean and records any errors.
     */
    public void validate(Object target, Errors errors) {
        // if the where predicate isn't null continue, 
        // otherwise only continue evaluate where predicate evaluates to true
        if ((wherePredicate != null ? wherePredicate.evaluate(target) : true)) {
            if (!getPredicate().evaluate(target)) {
                // Take into account error key and error args for localization
                if (StringUtils.hasLength(getErrorKey())) {
                    if (!CollectionUtils.isEmpty(getErrorArgs())) {
                        Collection<String> tmpColl = new ArrayList<String>();

                        for (Function errorArgFunction : getErrorArgs()) {
                            tmpColl.add(errorArgFunction.getResult(target).toString());
                        }

                        errors.rejectValue(getField(), getErrorKey(), tmpColl.toArray(), getErrorMessage());
                    } else {
                        errors.rejectValue(getField(), getErrorKey(), getErrorMessage());
                    }
                } else {
                    errors.rejectValue(getField(), getField(), getErrorMessage());
                }    
            }
        }
    }


}
