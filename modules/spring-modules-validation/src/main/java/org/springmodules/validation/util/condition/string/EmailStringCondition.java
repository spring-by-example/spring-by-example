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

package org.springmodules.validation.util.condition.string;

/**
 * An {@link org.springmodules.validation.util.condition.string.AbstractStringCondition} implementation that checks whether
 * the checked strings represent a valid email address.
 *
 * @author Uri Boness
 */
public class EmailStringCondition extends RegExpStringCondition {

    private final static String EMAIL_REGEXP = "^(([A-Za-z0-9]+_+)|([A-Za-z0-9]+\\-+)|([A-Za-z0-9]+\\.+)|([A-Za-z0-9]+\\++))*[A-Za-z0-9]+@((\\w+\\-+)|(\\w+\\.))*\\w{1,63}\\.[a-zA-Z]{2,6}$";

    /**
     * Constructs a new EmailStringCondition.
     */
    public EmailStringCondition() {
        super(EMAIL_REGEXP);
    }

}
