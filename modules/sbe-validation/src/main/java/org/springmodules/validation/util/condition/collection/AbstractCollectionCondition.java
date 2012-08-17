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

package org.springmodules.validation.util.condition.collection;

import java.util.Collection;

import org.springmodules.validation.util.condition.NonNullAcceptingCondition;

/**
 * A base class for all collection conditions.
 *
 * @author Uri Boness
 */
public abstract class AbstractCollectionCondition extends NonNullAcceptingCondition {

    /**
     * See {@link NonNullAcceptingCondition#beforeObjectChecked(Object)}.
     * <p/>
     * Also verifies the given object is of a {@link Collection} or an array.
     */
    protected void beforeObjectChecked(Object object) {
        super.beforeObjectChecked(object);
        if (!(object instanceof Collection) && !object.getClass().isArray()) {
            throw new IllegalArgumentException(getClass().getName() + " can only checkCalendar collection, iterators, or arrays");
        }
    }

    /**
     * See {@link org.springmodules.validation.util.condition.AbstractCondition#doCheck(Object)}.<br/>
     * <p/>
     * If the given object is an array, the call is delegated to {@link #checkArray(Object)}.<br/>
     * <p/>
     * If the given object is of a {@link Collection} type, the call is delegated to
     * {@link #checkCollection(java.util.Collection)}.
     */
    public final boolean doCheck(Object object) {
        if (object.getClass().isArray()) {
            return checkArray(object);
        }
        return checkCollection((Collection) object);
    }

    /**
     * Checks whether the given array adheres to this Condition.
     *
     * @param array The array to be checked.
     * @return <code>true</code> if the given array adheres to this condition, <code>false</code> otherwise.
     */
    protected abstract boolean checkArray(Object array);

    /**
     * Checks whether the given collection adheres to this Condition.
     *
     * @param collection The collection to be checked.
     * @return <code>true</code> if the given collection adheres to this condition, <code>false</code> otherwise.
     */
    protected abstract boolean checkCollection(Collection collection);

}
