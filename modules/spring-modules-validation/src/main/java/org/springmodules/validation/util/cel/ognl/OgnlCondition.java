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

package org.springmodules.validation.util.cel.ognl;

import ognl.Ognl;
import ognl.OgnlException;
import org.springframework.util.Assert;
import org.springmodules.validation.util.condition.AbstractCondition;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.lang.NestedIllegalArgumentException;

/**
 * A {@link Condition} that checks the given object based on a boolean OGNL expression.
 *
 * @author Uri Boness
 */
public class OgnlCondition extends AbstractCondition {

    private String expression;

    private Object compiledExpression;

    /**
     * Constructs a new OgnlCondition with given OGNL boolean expression.
     *
     * @param expression The given OGNL boolean expression.
     */
    public OgnlCondition(String expression) {
        Assert.notNull(expression, "OGNL expression cannot be null");
        this.expression = expression;
        try {
            compiledExpression = Ognl.parseExpression(expression);
        } catch (OgnlException oe) {
            throw new NestedIllegalArgumentException("Could not parse OGNL boolean expression '" + expression + "'", oe);
        }
    }

    /**
     * @see AbstractCondition#doCheck(Object)
     */
    public boolean doCheck(Object object) {
        try {
            return ((Boolean) Ognl.getValue(compiledExpression, object, Boolean.class)).booleanValue();
        } catch (OgnlException e) {
            throw new IllegalArgumentException("Could not evaluate expression '" + expression +
                "' on given object '" + String.valueOf(object) + "'");
        }
    }

}
