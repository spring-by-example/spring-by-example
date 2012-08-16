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

import org.springmodules.validation.util.cel.CelParseException;
import org.springmodules.validation.util.cel.ConditionExpressionParser;
import org.springmodules.validation.util.condition.Condition;

/**
 * A {@link ConditionExpressionParser} implementation that knows how to parse boolean OGNL expressions and return
 * the appropriate condition.
 *
 * @author Uri Boness
 */
public class OgnlConditionExpressionParser implements ConditionExpressionParser {

    public Condition parse(String expression) throws CelParseException {
        try {
            return new OgnlCondition(expression);
        } catch (IllegalArgumentException iae) {
            throw new CelParseException("Could not parse OGNL expression '" + expression + "'", iae);
        }
    }

}
