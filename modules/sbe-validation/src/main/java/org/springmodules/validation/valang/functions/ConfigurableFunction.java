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
 * <p>Allows a <code>Function</code> to be configured in Spring and 
 * then have it's arguments, line number, and row number set.</p>
 *
 * @author David Winterfeldt
 */
public interface ConfigurableFunction {

    /**
     * Gets number of expected arguments.
     */
    public int getExpectedNumberOfArguments();
    
    /**
     * Sets arguments, line number, and column number.
     */
    public void setArguments(int expectedNumberOfArguments, Function[] arguments,
                             int line, int column);

}
