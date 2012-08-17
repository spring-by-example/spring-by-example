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

import org.springmodules.validation.util.date.DateParseException;
import org.springmodules.validation.util.date.DateParser;

/**
 * <p>Literal function that re-parses date string
 * every time the result is asked for.
 *
 * @author Steven Devijver
 * @since 26-04-2005
 */
public class DateLiteralFunction implements Function {

    private String value = null;

    private DateParser dateParser = null;

    private int line = 0;

    private int column = 0;

    public DateLiteralFunction(String value, DateParser dateParser, int line, int column) {
        super();
        setValue(value);
        setDateParser(dateParser);
        setLine(line);
        setColumn(column);
    }

    private void setValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value parameter must not be null!");
        }
        this.value = value;
    }

    private String getValue() {
        return this.value;
    }

    private void setDateParser(DateParser dateParser) {
        if (dateParser == null) {
            throw new IllegalArgumentException("Date parser argument should not be null!");
        }
        this.dateParser = dateParser;
    }

    private DateParser getDateParser() {
        return this.dateParser;
    }

    private void setLine(int line) {
        this.line = line;
    }

    private int getLine() {
        return this.line;
    }

    private void setColumn(int column) {
        this.column = column;
    }

    private int getColumn() {
        return this.column;
    }

    public Object getResult(Object target) {
        try {
            return getDateParser().parse(getValue());
        } catch (DateParseException e) {
            throw new RuntimeException("Could not parse date [" + getValue() + "] at line " + getLine() + ", column " + getColumn() + ".");
        }
    }

}
