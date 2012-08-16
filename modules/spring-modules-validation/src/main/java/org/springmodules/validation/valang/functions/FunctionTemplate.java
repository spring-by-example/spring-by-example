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

import org.springmodules.validation.valang.ValangException;

/**
 * @author Steven Devijver
 * @since Jun 25, 2005
 */
public class FunctionTemplate {

    private int line = 0;

    private int column = 0;

    public FunctionTemplate(int line, int column) {
        super();
        this.line = line;
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }

    public String getAtLineString() {
        return " at line " + getLine() + ", column " + getColumn();
    }

    public Object execute(Object target, FunctionCallback functionCallback) {
        try {
            return functionCallback.execute(target);
        } catch (Exception e) {
            throw new ValangException(e, line, column);
        }
    }
}
