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
 * An {@link AbstractValidationRule} that represents a validation rule to validate string values based on a regular
 * expression.
 *
 * @author Uri Boness
 */
public class RegExpValidationRule extends AbstractValidationRule {

    public final static String DEFAULT_ERROR_CODE = "regexp";

    private String expression;

    /**
     * Constructs a new RegExpValidationRule with a given regular expression.
     *
     * @param expression The given regular expression.
     */
    public RegExpValidationRule(String expression) {
        super(DEFAULT_ERROR_CODE, createErrorArgumentsResolver(expression));
        this.expression = expression;
    }

    /**
     * Returns the regular expression condition.
     *
     * @see org.springmodules.validation.bean.rule.AbstractValidationRule#getCondition()
     */
    public Condition getCondition() {
        return Conditions.regexp(expression);
    }

    public static String getDefaultErrorCode() {
        return DEFAULT_ERROR_CODE;
    }

}
