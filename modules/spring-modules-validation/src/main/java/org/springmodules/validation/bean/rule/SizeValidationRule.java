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
 * An implementation of {@link AbstractValidationRule} that validates that a collection/array size
 * is within specific bounds.
 *
 * @author Uri Boness
 */
public class SizeValidationRule extends AbstractValidationRule {

    public final static String DEFAULT_ERROR_CODE = "size";

    private int min;

    private int max;

    /**
     * Constructs a new SizeValidationRule with given lower bound (min) and upper bound (max) for
     * the size of the validated collection/array.
     *
     * @param min The lower bound of the size.
     * @param max The upper bound of the size.
     */
    public SizeValidationRule(int min, int max) {
        super(SizeValidationRule.DEFAULT_ERROR_CODE, createErrorArgumentsResolver(new Integer(min), new Integer(max)));
        this.min = min;
        this.max = max;
    }

    /**
     * Returns the condition of this validation rule.
     *
     * @see org.springmodules.validation.bean.rule.AbstractValidationRule#getCondition()
     */
    public Condition getCondition() {
        return Conditions.sizeRange(min, max);
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}
