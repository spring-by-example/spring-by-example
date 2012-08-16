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

/**
 * An {@link AbstractRangeCondition} implementation that checks whether the checked range is between two
 * specific values (including).
 *
 * @author Uri Boness
 */
public class BetweenIncludingCondition extends AbstractBetweenCondition {

    /**
     * Constructs a new BetweenIncludingCondition with the given bounds (upper and lower) the checked range will be
     * compared with.
     *
     * @param lowerBound The lower bound the checked object will be compared with.
     * @param upperBound The upper bound the checked object will be compared with.
     */
    public BetweenIncludingCondition(Comparable lowerBound, Comparable upperBound) {
        super(lowerBound, upperBound);
    }

    /**
     * Constructs a new BetweenIncludingCondition with the given bounds (upper and lower) and the comparator to be used
     * to compare the checked value.
     *
     * @param lowerBound The lower bound the checked value will be compared with.
     * @param upperBound The upper bound the checked value will be compared with.
     * @param comparator The comparator that will be used to compare the checked value.
     */
    public BetweenIncludingCondition(Object lowerBound, Object upperBound, Comparator comparator) {
        super(lowerBound, upperBound, comparator);
    }

    /**
     * Checks whether the given value is between the bounds associated with this condition.
     *
     * @param value The value to be checked.
     * @param comparator The comparator to be used to compare the checked value.
     * @return <code>true</code> if the given range is greater than or equals the lower bound and smaller than
     *         or equals the upper bound, <code>false</code> otherwise.
     */
    protected boolean checkRange(Object value, Comparator comparator) {
        return comparator.compare(value, getLowerBound()) >= 0 && comparator.compare(value, getUpperBound()) <= 0;
    }

}
