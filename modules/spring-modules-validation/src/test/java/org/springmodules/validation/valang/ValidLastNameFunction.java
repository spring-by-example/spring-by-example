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

package org.springmodules.validation.valang;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springmodules.validation.valang.functions.AbstractFunction;
import org.springmodules.validation.valang.functions.Function;

/**
 * Checks if a last name is valid.
 * 
 * @author David Winterfeldt
 *
 */
public class ValidLastNameFunction extends AbstractFunction {

    final Logger logger = LoggerFactory.getLogger(ValidLastNameFunction.class);
    
    final Set<String> lValidLastNames = new HashSet<String>();
    
    /**
     * Constructor
     */
    public ValidLastNameFunction(Function[] arguments, int line, int column) {
        super(arguments, line, column);
        definedExactNumberOfArguments(1);

        lValidLastNames.add("Anderson");
        lValidLastNames.add("Jackson");
        lValidLastNames.add("Johnson");
        lValidLastNames.add("Jones");
        lValidLastNames.add("Smith");
    }

    /**
     * Checks if the last name is blocked.
     * 
     * @return      Object      Returns a <code>boolean</code> for 
     *                          whether or not the last name is blocked.
     */
    @Override
    protected Object doGetResult(Object target) {
        boolean result = true;
        
        String symbol = getArguments()[0].getResult(target).toString();
        
        if (!lValidLastNames.contains(symbol)) {
            result = false;
        }
        
        return result;
    }
    
}
