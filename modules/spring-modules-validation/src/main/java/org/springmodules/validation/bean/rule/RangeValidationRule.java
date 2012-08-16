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

package org.springmodules.validation.bean.rule;

import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.Conditions;

/**
 * An {@link AbstractValidationRule} implementation that validates that a comparable value
 * is within specific bounds.
 *
 * @author Uri Boness
 */
public class RangeValidationRule extends AbstractValidationRule {

    public final static String DEFAULT_ERROR_CODE = "range";

    private Comparable min;

    private Comparable max;

    /**
     * Constructs a new RangeValidationRule with given lower (min) and upper (max) bounds
     * for the validated value.
     *
     * @param min The lower bound of the value.
     * @param max The upper bound of the value.
     */
    public RangeValidationRule(Comparable min, Comparable max) {
        super(DEFAULT_ERROR_CODE, createErrorArgumentsResolver(min, max));
        this.min = min;
        this.max = max;
    }

    /**
     * Returns the condition of this validation rule.
     *
     * @see org.springmodules.validation.bean.rule.AbstractValidationRule#getCondition()
     */
    public Condition getCondition() {
        return Conditions.isBetweenIncluding(min, max);
    }

    public Comparable getMin() {
        return min;
    }

    public Comparable getMax() {
        return max;
    }

}
