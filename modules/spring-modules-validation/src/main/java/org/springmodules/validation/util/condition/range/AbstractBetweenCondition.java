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

/**
 * An {@link AbstractRangeCondition} implementation that serves as a base class for all between conditions.
 *
 * @author Uri Boness
 */
public abstract class AbstractBetweenCondition extends AbstractRangeCondition {

    private Object lowerBound;

    private Object upperBound;

    /**
     * Constructs a new BetweenCondition with the given bounds (upper and lower) the checked value will be
     * compared with.
     *
     * @param lowerBound The lower bound the checked object will be compared with.
     * @param upperBound The upper bound the checked object will be compared with.
     */
    public AbstractBetweenCondition(Comparable lowerBound, Comparable upperBound) {
        this(lowerBound, upperBound, new NumberAwareComparableComparator());
    }

    public AbstractBetweenCondition(Object lowerBound, Object upperBound, Comparator comparator) {
        super(comparator);
        Assert.notNull(lowerBound, "Lower bound cannot be null");
        Assert.notNull(upperBound, "Upper bound cannot be null");
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    //=============================================== Setter/Getter ====================================================

    /**
     * Returns the lower bound that is associated with this condition.
     *
     * @return The lower bound that is associated with this condition.
     */
    public Object getLowerBound() {
        return lowerBound;
    }

    /**
     * Returns the upper bound that is associated with this condition.
     *
     * @return The upper bound that is associated with this condition.
     */
    public Object getUpperBound() {
        return upperBound;
    }

}
