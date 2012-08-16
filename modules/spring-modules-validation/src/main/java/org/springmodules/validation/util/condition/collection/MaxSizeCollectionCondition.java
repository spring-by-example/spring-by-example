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
 * is longer then a specific maximum size.
 *
 * @author Uri Boness
 */
public class MaxSizeCollectionCondition extends AbstractCollectionCondition {

    private int maxSize;

    /**
     * Constructs a new MaxSizeCollectionCondition with a given maximum size.
     *
     * @param maxSize The maximum size.
     */
    public MaxSizeCollectionCondition(int maxSize) {
        Assert.isTrue(maxSize >= 0, "Max size cannot be negative");
        this.maxSize = maxSize;
    }

    /**
     * Checks whether the length of the given array is smaller than or equals the maximum size associated
     * with this condition.
     *
     * @param array The array to be checked.
     * @return <code>true</code> if the length of the given array is smaller than or equals the maximum size
     *         associated with this condition, <code>false</code> otherwise.
     */
    protected boolean checkArray(Object array) {
        return Array.getLength(array) <= maxSize;
    }

    /**
     * Checks whether the size of the given collection is smaller than or equals the maximum size associated
     * with this condition.
     *
     * @param collection The collection to be checked.
     * @return <code>true</code> if the size of the given collection is smaller than or equals the maximum size
     *         associated with this condition, <code>false</code> otherwise.
     */
    protected boolean checkCollection(Collection collection) {
        return collection.size() <= maxSize;
    }

    //============================================= Setter/Getter ===================================================

    /**
     * Returns the maximum size associated with this condition.
     *
     * @return The maximum size associated with this condition.
     */
    public int getMaxSize() {
        return maxSize;
    }

}
