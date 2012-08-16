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

package org.springmodules.validation.util.date;

import java.util.Date;

/**
 * <p>DateParser is a general contract to turn a string into a date or
 * else throw an exception.
 *
 * @author Steven Devijver
 * @since 25-04-2005
 */
public interface DateParser {

    public Date parse(String str) throws DateParseException;
}

