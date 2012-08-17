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

import org.springframework.beans.BeanWrapperImpl;
import org.springmodules.validation.util.fel.FelEvaluationException;
import org.springmodules.validation.util.fel.Function;
import org.springmodules.validation.util.fel.FunctionExpressionParser;

/**
 * A {@link FunctionExpressionParser} implementation that knows how to parse BeanWrapper-like property paths and
 * returns a function that extracts the appropriate property value from the given object (bean).
 *
 * @author Uri Boness
 */
public class PropertyPathFunctionExpressionParser implements FunctionExpressionParser {

    public Function parse(String expression) {
        return new PropertyPathFunction(expression);
    }

    /**
     * A function that is associated with a property path. On evaluation, the appropriate property value is
     * extracted from the given object (bean).
     */
    protected class PropertyPathFunction implements Function {

        private String propertyPath;

        public PropertyPathFunction(String propertyPath) {
            this.propertyPath = propertyPath;
        }

        public Object evaluate(Object argument) {
            try {
                return new BeanWrapperImpl(argument).getPropertyValue(propertyPath);
            } catch (Throwable t) {
                throw new FelEvaluationException("Could not evaluate path '" + propertyPath +
                    "' on bean '" + String.valueOf(argument) + "'", t);
            }
        }
    }

}
