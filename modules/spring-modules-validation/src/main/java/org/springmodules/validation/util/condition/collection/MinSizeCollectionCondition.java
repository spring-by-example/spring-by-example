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

import java.lang.reflect.Array;
import java.util.Collection;

import org.springframework.util.Assert;

/**
 * An {@link AbstractCollectionCondition} implementation that checks whether the given collection or array
 * is longer then a specific minimum size.
 *
 * @author Uri Boness
 */
public class MinSizeCollectionCondition extends AbstractCollectionCondition {

    private int minSize;

    /**
     * Constructs a new MinSizeCollectionCondition with a given minimum size.
     *
     * @param minSize The minimum size.
     */
    public MinSizeCollectionCondition(int minSize) {
        Assert.isTrue(minSize >= 0, "Min size cannot be negative");
        this.minSize = minSize;
    }

    /**
     * Checks whether the length of the given array is greater than or equals the minimum size associated
     * with this condition.
     *
     * @param array The array to be checked.
     * @return <code>true</code> if the length of the given array is greater than or equals the minimum size
     *         associated with this condition, <code>false</code> otherwise.
     */
    protected boolean checkArray(Object array) {
        return Array.getLength(array) >= minSize;
    }

    /**
     * Checks whether the size of the given collection is greater than or equals the minimum size associated
     * with this condition.
     *
     * @param collection The collection to be checked.
     * @return <code>true</code> if the size of the given collection is greater than or equals the minimum size
     *         associated with this condition, <code>false</code> otherwise.
     */
    protected boolean checkCollection(Collection collection) {
        return collection.size() >= minSize;
    }

    //============================================= Setter/Getter ===================================================

    /**
     * Returns the minimum size associated with this condition.
     *
     * @return The minimum size associated with this condition.
     */
    public int getMinSize() {
        return minSize;
    }

}
