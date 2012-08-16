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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Regular expression match function. Takes two arguments: the pattern as a string and the string
 * to be matched. The toString method of the second parameter value is called to retrieve the
 * string value. This function is not null-safe.
 *
 * @author Steven Devijver
 * @since Sep 15, 2005
 */
public class RegExFunction extends AbstractFunction {

    private Map patternCache = null;

    public RegExFunction(Function[] arguments, int line, int column) {
        super(arguments, line, column);
        definedExactNumberOfArguments(2);
        patternCache = new HashMap();
    }

    protected Object doGetResult(Object target) throws Exception {
        Object value = getArguments()[0].getResult(target);
        if (value instanceof String) {
            Pattern pattern = null;
            if (patternCache.containsKey(value)) {
                pattern = (Pattern) patternCache.get(value);
            } else {
                pattern = Pattern.compile((String) value);
                patternCache.put(value, pattern);
            }
            String str = getArguments()[1].getResult(target).toString();
            return pattern.matcher(str).matches() ? Boolean.TRUE : Boolean.FALSE;
        } else {
            throw new Exception("No String value for regular expression");
        }
    }

}
