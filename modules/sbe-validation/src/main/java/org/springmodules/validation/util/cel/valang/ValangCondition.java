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

package org.springmodules.validation.util.cel.valang;

import java.util.Map;

import org.springmodules.validation.util.condition.AbstractCondition;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.valang.parser.ValangBased;

/**
 * A {@link Condition} implementation that checks the given object based on a valang boolean expression.
 *
 * @author Uri Boness
 */
public class ValangCondition extends AbstractCondition implements ValangBased {

    private ValangConditionExpressionParser parser;
    private String expression;
    private Condition compiledExpression;

    /**
     * Constructor
     */
    public ValangCondition(String expression) {
        this(expression, null, null);
    }

    /**
     * Constructor
     */
    public ValangCondition(String expression, Map functionsByName, Map parserByRegexp) {
        this.expression = expression;
        parser = new ValangConditionExpressionParser();
        parser.setCustomFunctions(functionsByName);
        parser.setDateParsers(parserByRegexp);
    }

    public boolean doCheck(Object object) {
        if (compiledExpression == null) {
            compiledExpression = parser.parse(expression);
        }
        return compiledExpression.check(object);
    }

    public void addCustomFunction(String functionName, String functionClassName) {
        parser.addCustomFunction(functionName, functionClassName);
    }

    public void setCustomFunctions(Map functionByName) {
        parser.setCustomFunctions(functionByName);
    }

    public void setDateParsers(Map parserByRegexp) {
        parser.setDateParsers(parserByRegexp);
    }

}
