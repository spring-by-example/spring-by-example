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
 * An {@link AbstractStringCondition} implementation that checks that a given string is
 * within a specific length range.
 *
 * @author Uri Boness
 */
public class LengthRangeStringCondition extends AbstractStringCondition {

    private int minLength;

    private int maxLength;

    /**
     * Constructs a new LengthRangeStringCondition with given min and max range boundries.
     *
     * @param minLength The minimum length of the text range.
     * @param maxLength The maximum length of the text range.
     */
    public LengthRangeStringCondition(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    /**
     * Checks that the given text is within the range defined by this condition.
     *
     * @param text The text to be checked.
     * @return <code>true</code> if the given text is within the range, <code>false</code> otherwise.
     */
    protected boolean checkString(String text) {
        int length = text.length();
        return length >= minLength && length <= maxLength;
    }

    //=============================================== Setter/Getter ====================================================

    /**
     * Returns the minimum length of the range.
     *
     * @return The minimum length of the range.
     */
    public int getMinLength() {
        return minLength;
    }

    /**
     * Returns the maximum length of the range.
     *
     * @return The maximum length of the range.
     */
    public int getMaxLength() {
        return maxLength;
    }

}
