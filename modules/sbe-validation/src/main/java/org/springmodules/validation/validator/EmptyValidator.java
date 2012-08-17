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

/**
 * A validator that doesn't perform any validation.
 *
 * @author Uri Boness
 */
public class EmptyValidator implements Validator {

    /**
     * See {@link Validator#supports(Class)}.
     *
     * @return <code>true</code>. This validator supports all classes.
     */
    public boolean supports(Class clazz) {
        return true;
    }

    /**
     * See {@link Validator#validate(Object, org.springframework.validation.Errors)}.
     * <p/>
     * Does nothing.
     */
    public void validate(Object obj, Errors errors) {
        // do nothing
    }
}
