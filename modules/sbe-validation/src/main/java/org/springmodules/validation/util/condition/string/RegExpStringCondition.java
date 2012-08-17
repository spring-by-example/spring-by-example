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

import java.util.regex.Pattern;

import org.springframework.util.Assert;

/**
 * An {@link AbstractStringCondition} implementation that checks whether the checked strings match a specific regular
 * expression.
 *
 * @author Uri Boness
 */
public class RegExpStringCondition extends AbstractStringCondition {

    private final Pattern pattern;

    /**
     * Constructs a new RegExpStringCondition with a given regular expression that the checkCalendar text will be checked
     * against.
     *
     * @param regexp The given regular expression.
     */
    public RegExpStringCondition(String regexp) {
        Assert.notNull(regexp, "The given regular expression cannot be null");
        this.pattern = Pattern.compile(regexp);
    }

    /**
     * Constructs a new RegExpStringCondition with a given {@link Pattern} that the checked text will be checked
     * against.
     *
     * @param pattern The pattern that the checked text will be checked against.
     */
    public RegExpStringCondition(Pattern pattern) {
        Assert.notNull(pattern, "The given pattern cannot be null");
        this.pattern = pattern;
    }

    /**
     * Checks whether the given text matches the regular expression that is associated with this condition.
     *
     * @param text The text to be checked.
     * @return <code>true</code> if the given text matches the regular expression that is associated with this
     *         condition, <code>false</code> otherwise.
     */
    protected boolean checkString(String text) {
        return pattern.matcher(text).matches();
    }

    //============================================= Setter/Getter ===================================================

    /**
     * Returns the pattern that is associated with this condition.
     *
     * @return The pattern that is associated with this condition.
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * Returns the regular expression that is associated with this condition.
     *
     * @return The regular expression that is associated with this condition.
     */
    public String getRegExp() {
        return pattern.pattern();
    }

}
