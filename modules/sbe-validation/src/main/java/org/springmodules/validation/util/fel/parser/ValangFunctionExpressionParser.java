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

import org.springmodules.validation.util.fel.FelEvaluationException;
import org.springmodules.validation.util.fel.FelParseException;
import org.springmodules.validation.util.fel.Function;
import org.springmodules.validation.util.fel.FunctionExpressionParser;
import org.springmodules.validation.valang.functions.TargetBeanFunction;
import org.springmodules.validation.valang.parser.ParseException;
import org.springmodules.validation.valang.parser.SimpleValangBased;
import org.springmodules.validation.valang.parser.ValangParser;

/**
 * A {@link FunctionExpressionParser} implementation that knows how to parse valang function expressions.
 *
 * @author Uri Boness
 */
public class ValangFunctionExpressionParser extends SimpleValangBased implements FunctionExpressionParser {

    public Function parse(String expression) {
        return new ValangFunction(expression);
    }

    /**
     * A function that is associated with a valang function expression. This function evaluates the function on
     * the given object and returns the result.
     */
    protected class ValangFunction implements Function {

        private String valangExpression;

        private org.springmodules.validation.valang.functions.Function valangFunction;

        public ValangFunction(String valangExpression) {
            this.valangExpression = valangExpression;
            ValangParser parser = createValangParser(valangExpression);
            try {
                this.valangFunction = parser.function(new TargetBeanFunction());
            } catch (ParseException pe) {
                throw new FelParseException("Could not parse valang function expression '" +
                    valangExpression + "'", pe);
            }
        }

        public Object evaluate(Object argument) {
            try {
                return valangFunction.getResult(argument);
            } catch (Throwable t) {
                throw new FelEvaluationException("Could not evaluate valang expression '" +
                    valangExpression + "' on bean '" + String.valueOf(argument) + "'", t);
            }
        }
    }

}
