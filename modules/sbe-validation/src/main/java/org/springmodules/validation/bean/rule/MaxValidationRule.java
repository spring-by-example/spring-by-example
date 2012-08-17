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
 * An {@link AbstractValidationRule} that validates and checks that a comparable value is less than or equals
 * a specific upper bound.
 *
 * @author Uri Boness
 */
public class MaxValidationRule extends AbstractValidationRule {

    public final static String DEFAULT_ERROR_CODE = "max";

    private Comparable max;

    /**
     * Constructs a new MaxValidationRule with a given upper bound.
     *
     * @param max The upper bound.
     */
    public MaxValidationRule(Comparable max) {
        super(DEFAULT_ERROR_CODE, createErrorArgumentsResolver(max));
        this.max = max;
    }

    /**
     * Returns the condition of this validation rule.
     *
     * @see org.springmodules.validation.bean.rule.AbstractValidationRule#getCondition()
     */
    public Condition getCondition() {
        return Conditions.isLte(max);
    }

    public Comparable getMax() {
        return max;
    }

}
