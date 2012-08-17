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
 * <p>Creates a wrapper for a function for Spring configured functions 
 * so &lt;aop:scoped-proxy&gt; can be used.</p>
 *
 * @author David Winterfeldt
 */
public class FunctionWrapper {

    private String functionName = null;
    private Function function = null;

    /**
     * Gets function name.  Used during autodiscovery of functions 
     * in Spring context.
     */
    public String getFunctionName() {
        return functionName;
    }

    /**
     * Sets function name.  Used during autodiscovery of functions 
     * in Spring context.
     */
    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    /**
     * Gets function.
     */
    public Function getFunction() {
        return function;
    }

    /**
     * Sets function.
     */
    public void setFunction(Function function) {
        this.function = function;
    }

}
