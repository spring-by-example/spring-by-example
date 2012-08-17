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

package org.springmodules.validation.util.condition.range;

import java.util.Comparator;

import org.springframework.util.Assert;
import org.springmodules.validation.util.condition.NonNullAcceptingCondition;
import org.springmodules.validation.util.condition.NonNullAcceptingTypeSpecificCondition;

/**
 * A base class for all range conditions.
 *
 * @author Uri Boness
 */
public abstract class AbstractRangeCondition extends NonNullAcceptingCondition {

    private Comparator comparator;

    /**
     * Constructs a new AbstractRangeCondition.
     */
    public AbstractRangeCondition(Comparator comparator) {
        Assert.notNull(comparator, "Comparator cannot be null");
        this.comparator = comparator;
    }

    /**
     * See {@link NonNullAcceptingTypeSpecificCondition#doCheck(Object)}.
     * <p/>
     * Delegates to {@link #checkRange(Object, java.util.Comparator)}
     */
    public final boolean doCheck(Object bean) {
        return checkRange(bean, comparator);
    }

    /**
     * Checks the condition upon the given range object.
     *
     * @param value The value to be checked
     * @param comparator The comparator to be used for the checked value.
     * @return <code>true</code> if the given range adheres to this condition, <code>false</code> otherwise.
     */
    protected abstract boolean checkRange(Object value, Comparator comparator);

}
