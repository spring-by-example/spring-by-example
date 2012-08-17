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

/**
 * An {@link AbstractCollectionCondition} that checks that the size/length of the the checked collection/array is
 * with in a specific bounds.
 *
 * @author Uri Boness
 */
public class SizeRangeCollectionCondition extends AbstractCollectionCondition {

    private int minSize;

    private int maxSize;

    /**
     * Constructs a new SizeRangeCollectionCondition with a given minimum and maximum sizes.
     *
     * @param minSize The given minimum size.
     * @param maxSize The given maximum size.
     */
    public SizeRangeCollectionCondition(int minSize, int maxSize) {
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    /**
     * Checks whether the given array is in the boundries of the range defined by this condition.
     *
     * @see AbstractCollectionCondition#checkArray(Object)
     */
    protected boolean checkArray(Object array) {
        int length = Array.getLength(array);
        return length >= minSize && length <= maxSize;
    }

    /**
     * Checks whether the given collection is in the boundries of the range defined by this condition.
     *
     * @see AbstractCollectionCondition#checkCollection(java.util.Collection)
     */
    protected boolean checkCollection(Collection collection) {
        int size = collection.size();
        return size >= minSize && size <= maxSize;
    }

    //=============================================== Setter/Getter ====================================================

    /**
     * Returns the minimum size of this range condition
     *
     * @return The minimum size of this range condition
     */
    public int getMinSize() {
        return minSize;
    }

    /**
     * Returns the maximum size of this range condition
     *
     * @return The maximum size of this range condition
     */
    public int getMaxSize() {
        return maxSize;
    }

}
