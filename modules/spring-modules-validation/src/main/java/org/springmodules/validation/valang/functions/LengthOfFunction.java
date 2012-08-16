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

package org.springmodules.validation.valang.functions;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * <p>Gets the length of a string.
 *
 * @author Steven Devijver
 * @author Uri Boness
 * @since Apr 23, 2005
 */
public class LengthOfFunction extends AbstractFunction {

    public LengthOfFunction(Function[] arguments, int line, int column) {
        super(arguments, line, column);
        definedExactNumberOfArguments(1);
    }

    protected Object doGetResult(Object target) {

        Object result = getArguments()[0].getResult(target);

        if (Collection.class.isAssignableFrom(result.getClass())) {
            return new Integer(((Collection) result).size());
        }

        if (result.getClass().isArray()) {
            return new Integer(Array.getLength(result));
        }

        return new Integer(result.toString().length());
    }

}
