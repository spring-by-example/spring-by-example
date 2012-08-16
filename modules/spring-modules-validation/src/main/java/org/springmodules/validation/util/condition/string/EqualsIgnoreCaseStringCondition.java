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
 * An {@link AbstractStringCondition} implementation that checks whether the checked strings equal a specific string
 * while ignoring the letter casing.
 *
 * @author Uri Boness
 */
public class EqualsIgnoreCaseStringCondition extends AbstractStringCondition {

    private String text;

    /**
     * Constructs a new EqualsIgnoreCaseStringCondition with a given text to be compared with the checked strings.
     *
     * @param text The text to be compared with the checked strings.
     */
    public EqualsIgnoreCaseStringCondition(String text) {
        this.text = text;
    }

    /**
     * Checks whether the given text equals the text that is associated with this condition while ignoring the letter
     * casing.
     *
     * @param text The text to be checed.
     * @return <code>true</code> if the given text equals the text that is associated with this condition,
     *         <code>false</code> otherwise.
     */
    protected boolean checkString(String text) {
        return this.text.equalsIgnoreCase(text);
    }

    //============================================= Setter/Getter ===================================================

    public String getText() {
        return text;
    }

}
