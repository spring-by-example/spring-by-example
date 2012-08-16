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
 * A base class for all single bound conditions.
 *
 * @author Uri Boness
 */
public abstract class AbstractSingleBoundCondition extends AbstractRangeCondition {

    protected Object bound;

    /**
     * Constructs a new AbstractSingleBoundCondition with a given bound.
     *
     * @param bound The bound.
     */
    public AbstractSingleBoundCondition(Comparable bound) {
        this(bound, new NumberAwareComparableComparator());
    }

    /**
     * Constructs a new AbstractSingleBoundCondition with given bound and the comparator to be used to
     * compare the checked value.
     *
     * @param bound The bound bound.
     * @param comparator The comparator.
     */
    public AbstractSingleBoundCondition(Object bound, Comparator comparator) {
        super(comparator);
        Assert.notNull(bound, "Bound cannot be null");
        this.bound = bound;
    }

}
