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

package org.springmodules.validation.valang.parser;

import java.util.Map;

/**
 * Objects that are based on the {@link ValangParser} should implement this interface.
 *
 * @author Uri Boness
 */
public interface ValangBased {

    /**
     * Adds the a new custom function to be used in the valang el.
     *
     * @param functionName The name of the function.
     * @param functionClassName The fully qualified class name of the function.
     */
    void addCustomFunction(String functionName, String functionClassName);

    /**
     * Sets custom functions that should be registered with the used {@link ValangParser}.
     *
     * @param functionByName the custom functions where the key is the function name and the value is the function
     * class FQN.
     */
    void setCustomFunctions(Map<String, Object> functionByName);

    /**
     * Sets the date parser that should be registered with the used {@link ValangParser}.
     *
     * @param parserByRegexp the date parsers where the key is the regexp associated with the parser and the value
     * is the parser class FQN.
     */
    void setDateParsers(Map parserByRegexp);

}
