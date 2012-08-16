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

import org.springframework.core.NestedRuntimeException;

public class ValangException extends NestedRuntimeException {

    private static final long serialVersionUID = -3223482349872384878L;

    private int line = 0;

    private int column = 0;

    public ValangException(String arg0, int line, int column) {
        super(arg0);
        this.line = line;
        this.column = column;
    }

    public ValangException(String arg0, Throwable arg1, int line, int column) {
        super(arg0, arg1);
        this.line = line;
        this.column = column;
    }

    public ValangException(Throwable t, int line, int column) {
        super(t.getMessage(), t);
        this.line = line;
        this.column = column;
    }


    public String getMessage() {
        return super.getMessage() + " at line " + line + ", column " + column;
    }

    public int getLine() {
        return this.line;
    }

    public int getColumn() {
        return this.column;
    }

}
