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

import org.springmodules.validation.util.cel.ConditionExpressionParser;
import org.springmodules.validation.util.condition.Condition;

/**
 * An {@link AbstractValidationRule} implementation that validates and checks a value based on a condition expression
 * (See package org.springmodules.validation.util.cel).
 *
 * @author Uri Boness
 */
public class ExpressionValidationRule extends AbstractValidationRule {

    public final static String DEFAULT_ERROR_CODE = "expression";

    private Condition condition;

    /**
     * Constructs a new ExpressionValidationRule with given expression and expression parser.
     *
     * @param parser The expression parser to use when parsing the given expression.
     * @param expression The condition expression.
     */
    public ExpressionValidationRule(ConditionExpressionParser parser, String expression) {
        super(DEFAULT_ERROR_CODE);
        condition = parser.parse(expression);
    }

    /**
     * Returns the condition of this validation rule.
     *
     * @see org.springmodules.validation.bean.rule.AbstractValidationRule#getCondition()
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * This rule supports null values as the expression may operate on null values.
     *
     * @return true.
     */
    protected boolean supportsNullValues() {
        return true;
    }
}
