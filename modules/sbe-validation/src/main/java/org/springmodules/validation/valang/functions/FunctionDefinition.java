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
 * Defines a function. This definition holds the function name
 * and the fully qualified name of the function class.
 *
 * @author Uri Boness
 */
public class FunctionDefinition {

    private String name;

    private String className;

    /**
     * Constructs a new FunctionDefinition.
     */
    public FunctionDefinition() {
        this(null, null);
    }

    /**
     * Constructs a new FunctionDefinition with a given name and fully qulified class name.
     *
     * @param name The name of the function.
     * @param className The fully qualified class name of the function.
     */
    public FunctionDefinition(String name, String className) {
        this.name = name;
        this.className = className;
    }

    //=============================================== Setter/Getter ====================================================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
