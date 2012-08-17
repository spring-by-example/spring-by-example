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

package org.springmodules.validation.bean.rule.resolver;

import org.springmodules.validation.util.fel.Function;
import org.springmodules.validation.util.fel.FunctionExpressionParser;

/**
 * Resolves error arguments based on valang expressions.
 *
 * @author Uri Boness
 */
public class FunctionErrorArgumentsResolver implements ErrorArgumentsResolver {

    private Function[] functions;

    public FunctionErrorArgumentsResolver(Function[] functions) {
        this.functions = functions;
    }

    public FunctionErrorArgumentsResolver(String[] expressions, FunctionExpressionParser functionExpressionParser) {
        this(parseFunctionExpressions(expressions, functionExpressionParser));
    }

    /**
     * Returns the error arguments that are resolved by the configured function expressions.
     *
     * @see org.springmodules.validation.bean.rule.resolver.ErrorArgumentsResolver#resolveArguments(Object)
     */
    public Object[] resolveArguments(Object obj) {
        Object[] args = new Object[functions.length];
        for (int i = 0; i < args.length; i++) {
            args[i] = functions[i].evaluate(obj);
        }
        return args;
    }

    //=============================================== Helper Methods ===================================================

    protected static Function[] parseFunctionExpressions(String[] expressions, FunctionExpressionParser parser) {
        Function[] functions = new Function[expressions.length];
        for (int i = 0; i < expressions.length; i++) {
            functions[i] = parser.parse(expressions[i]);
        }
        return functions;
    }

}
