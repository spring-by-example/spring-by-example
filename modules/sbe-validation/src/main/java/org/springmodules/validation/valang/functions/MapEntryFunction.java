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

import java.util.Map;

/**
 * @author Steven Devijver
 * @since 27-04-2005
 */
public class MapEntryFunction implements Function {

    private Function mapFunction = null;

    private Function keyFunction = null;

    private int beginLine = 0;

    private int beginColumn = 0;

    public MapEntryFunction(Function mapFunction, Function keyFunction, int beginLine, int beginColumn) {
        super();
        setMapFunction(mapFunction);
        setKeyFunction(keyFunction);
        setBeginLine(beginLine);
        setBeginColumn(beginColumn);
    }

    private void setMapFunction(Function mapFunction) {
        this.mapFunction = mapFunction;
    }

    public Function getMapFunction() {
        return this.mapFunction;
    }

    private void setKeyFunction(Function keyFunction) {
        this.keyFunction = keyFunction;
    }

    public Function getKeyFunction() {
        return this.keyFunction;
    }

    private void setBeginLine(int beginLine) {
        this.beginLine = beginLine;
    }

    private int getBeginLine() {
        return this.beginLine;
    }

    private void setBeginColumn(int beginColumn) {
        this.beginColumn = beginColumn;
    }

    private int getBeginColumn() {
        return this.beginColumn;
    }

    public Object getResult(Object target) {
        Object map = getMapFunction().getResult(target);
        if (map instanceof Map) {
            return ((Map) map).get(getKeyFunction().getResult(target));
        } else {
            throw new ClassCastException("Could to cast property value to java.util.Map at line " + getBeginLine() + ", column " + getBeginColumn() + ".");
        }
    }

}
