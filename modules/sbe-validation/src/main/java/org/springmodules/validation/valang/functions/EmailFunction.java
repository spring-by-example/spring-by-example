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

import java.util.regex.Pattern;

/**
 * Email function. Takes one argument. Converts the argument to a string using the <code>toString()</code> method, and
 * checks whether the returned string is a valid email address.
 *
 * @author Uri Boness
 * @since May 26, 2006
 */
public class EmailFunction extends AbstractFunction {

    private static final Pattern pattern = Pattern.compile("^(([A-Za-z0-9]+_+)|([A-Za-z0-9]+\\-+)|([A-Za-z0-9]+\\.+)|([A-Za-z0-9]+\\++))*[A-Za-z0-9]+@((\\w+\\-+)|(\\w+\\.))*\\w{1,63}\\.[a-zA-Z]{2,6}$");

    public EmailFunction(Function[] arguments, int line, int column) {
        super(arguments, line, column);
        definedExactNumberOfArguments(1);
    }

    protected Object doGetResult(Object target) throws Exception {
        Object value = getArguments()[0].getResult(target);
        String email = value.toString();
        return (pattern.matcher(email).matches()) ? Boolean.TRUE : Boolean.FALSE;
    }

}
