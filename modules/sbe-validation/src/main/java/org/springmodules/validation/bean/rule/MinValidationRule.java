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
 * An {@link AbstractValidationRule} that validates and checks that a comparable value
 * is greater than or equals a specific lower bound.
 *
 * @author Uri Boness
 */
public class MinValidationRule extends AbstractValidationRule {

    public final static String DEFAULT_ERROR_CODE = "min";

    private Comparable min;

    /**
     * Constructs a new MinValidationRule with a given lower bound. The error arguments
     * of this rule are by default the validated value and the lower bound (in this order).
     *
     * @param min The lower bound.
     */
    public MinValidationRule(Comparable min) {
        super(DEFAULT_ERROR_CODE, createErrorArgumentsResolver(min));
        this.min = min;
    }

    /**
     * Returns the condition of this validation rule.
     *
     * @see org.springmodules.validation.bean.rule.AbstractValidationRule#getCondition()
     */
    public Condition getCondition() {
        return Conditions.isGte(min);
    }

    public Comparable getMin() {
        return min;
    }

}
