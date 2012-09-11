/*
 * Copyright 2007-2012 the original author or authors.
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
package org.springbyexample.jaxb.beans.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Converts a date <code>String</code> to a <code>Date</code> 
 * and back.
 * 
 * @author David Winterfeldt
 */
public class DateXmlAdapter extends XmlAdapter<String, DateTime> {

    private static final String PATTERN = "yyyy-MM-dd";
    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern(PATTERN);

    @Override
    public DateTime unmarshal(String value) {
        return formatter.parseDateTime(value);
    }

    @Override
    public String marshal(DateTime value) {
        return formatter.print(value);
    }

}
