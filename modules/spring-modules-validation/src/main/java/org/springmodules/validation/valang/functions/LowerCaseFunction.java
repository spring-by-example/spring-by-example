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


/**
 * <p>Converts a string to lower case.
 *
 * @author Steven Devijver
 * @since Apr 23, 2005
 */
public class LowerCaseFunction extends AbstractFunction {

    public LowerCaseFunction(Function[] arguments, int line, int column) {
        super(arguments, line, column);
        definedExactNumberOfArguments(1);
    }

    protected Object doGetResult(Object target) {
        return getArguments()[0].getResult(target).toString().toLowerCase();
    }

}
