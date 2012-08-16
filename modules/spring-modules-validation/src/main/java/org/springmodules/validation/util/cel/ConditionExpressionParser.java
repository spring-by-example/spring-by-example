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

package org.springmodules.validation.util.cel;

import org.springmodules.validation.util.condition.Condition;

/**
 * Represents a parser that can parse a condition expression and return the appropriate condition.
 *
 * @author Uri Boness
 * @see Condition
 */
public interface ConditionExpressionParser {

    /**
     * Parses the given condition expression and returns the parsed condition.
     *
     * @param expression The given condition expression.
     * @return The parsed condition.
     * @throws CelParseException thrown when the condition expression parsing fails.
     */
    Condition parse(String expression) throws CelParseException;

}
