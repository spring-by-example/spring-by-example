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

import java.io.StringReader;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springmodules.validation.valang.functions.Function;
import org.springmodules.validation.valang.functions.TargetBeanFunction;
import org.springmodules.validation.valang.parser.ParseException;
import org.springmodules.validation.valang.parser.ValangParser;

/**
 * Resolves error arguments based on valang expressions.
 *
 * @author Uri Boness
 */
public class ValangErrorArgumentsResolver implements ErrorArgumentsResolver {

    private final static Logger logger = LoggerFactory.getLogger(ValangErrorArgumentsResolver.class);

    private Function[] functions;

    /**
     * Creates a new ValangErrorArgumentsResolver with no argument expressions, that is, there will be no arguments.
     */
    public ValangErrorArgumentsResolver() {
        this(new String[0]);
    }

    /**
     * Creates a new ValangErrorArgumentsResolver with the given argument expressions. This constructor uses the default
     * {@link ValangParser).
     *
     * @param expressions The argument valang expressions.
     */
    public ValangErrorArgumentsResolver(String[] expressions) {
        this(expressions, null);
    }

    /**
     * Creates a new ValangErrorArgumentsResolver with given argument expressions and custom functions that will be used
     * by the valang parser.
     *
     * @param expressions The arguments valang expressions.
     * @param functionsByName A map of custom valang functiosns where the key is the function name and the value is
     * the name of the function class.
     */
    public ValangErrorArgumentsResolver(String[] expressions, Map functionsByName) {
        if (expressions == null) {
            expressions = new String[0];
        }
        functions = new Function[expressions.length];
        for (int i = 0; i < expressions.length; i++) {
            functions[i] = parseFunction(expressions[i], functionsByName);
        }
    }

    /**
     * Returns the error arguments that are resolved by the configured valang expressions.
     *
     * @see org.springmodules.validation.bean.rule.resolver.ErrorArgumentsResolver#resolveArguments(Object)
     */
    public Object[] resolveArguments(Object obj) {
        Object[] args = new Object[functions.length];
        for (int i = 0; i < args.length; i++) {
            args[i] = functions[i].getResult(obj);
        }
        return args;
    }

    //=============================================== Helper Methods ===================================================

    /**
     * Parses a valang {@link Function} from the given argument expression and custom functions.
     *
     * @param expression The given argument expression.
     * @param functionsByName The custom valang functions.
     * @return The parsed function.
     */
    protected Function parseFunction(String expression, Map functionsByName) {
        ValangParser parser = new ValangParser(new StringReader(expression));
        parser.setFunctionsByName(functionsByName);
        try {
            return parser.function(new TargetBeanFunction());
        } catch (ParseException pe) {
            logger.error("Could not parse valang expression '" + expression + "' to a function", pe);
            throw new IllegalArgumentException("Could not parse valang expression '" + expression + "' to a function");
        }
    }

}
