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

package org.springmodules.validation.util.condition.string;

import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.NonNullAcceptingTypeSpecificCondition;

/**
 * An abstract condition that can be applied on objects of type {@link java.lang.String}.
 *
 * @author Uri Boness
 */
public abstract class AbstractStringCondition extends NonNullAcceptingTypeSpecificCondition {

    protected final static String EMPTY_STRING = "";

    /**
     * Constructs a new AbstractStringCondition.
     */
    protected AbstractStringCondition() {
        super(String.class);
    }

    /**
     * See {@link Condition#check(Object)}
     *
     * @throws IllegalArgumentException if the given object is either <code>null</code> or not of a
     * <code>java.lang.String</code> type.
     */
    public final boolean doCheck(Object object) {
        return checkString((String) object);
    }

    /**
     * Checks whether the given string adheres to this condition.
     *
     * @param text The string to be checked.
     * @return <code>true</code> if the given string adheres to this condition, <code>false</code> otherwise.
     */
    protected abstract boolean checkString(String text);

}
