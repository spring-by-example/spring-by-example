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

package org.springmodules.validation.util.fel.parser;

import ognl.Ognl;
import ognl.OgnlException;
import org.springmodules.validation.util.fel.FelEvaluationException;
import org.springmodules.validation.util.fel.FelParseException;
import org.springmodules.validation.util.fel.Function;
import org.springmodules.validation.util.fel.FunctionExpressionParser;

/**
 * A {@link FunctionExpressionParser} implementation that knows how to parse OGNL expressions and
 * return the appropriate function.
 *
 * @author Uri Boness
 */
public class OgnlFunctionExpressionParser implements FunctionExpressionParser {

    public Function parse(String expression) {
        return new OgnlFunction(expression);
    }

    /**
     * A function that is associated with an OGNL expression and evaluates this expression on
     * the given object.
     */
    protected class OgnlFunction implements Function {

        private String expressionAsString;

        private Object ognlExpression;

        public OgnlFunction(String expressionAsString) {
            this.expressionAsString = expressionAsString;
            try {
                this.ognlExpression = Ognl.parseExpression(expressionAsString);
            } catch (OgnlException oe) {
                throw new FelParseException("Could not parse OGNL expression '" + expressionAsString + "'", oe);
            }
        }

        public Object evaluate(Object argument) {
            try {
                return Ognl.getValue(ognlExpression, argument);
            } catch (OgnlException oe) {
                throw new FelEvaluationException("Could not evaluate OGNL expression '" + expressionAsString +
                    "' on argument '" + String.valueOf(argument), oe);
            }
        }
    }

}
