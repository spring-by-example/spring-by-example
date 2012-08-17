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

import org.springframework.util.Assert;

/**
 * An {@link AbstractStringCondition} implementation that checks whether the checked strings are not longer then
 * a specific length.
 *
 * @author Uri Boness
 */
public class MaxLengthStringCondition extends AbstractStringCondition {

    private int maxLength;

    /**
     * Creates a new MaxLengthStringCondition with a given maximum length.
     *
     * @param maxLength The maximum length the checked string will be checked against.
     */
    public MaxLengthStringCondition(int maxLength) {
        Assert.isTrue(maxLength >= 0, "Given maximum length must be a non-negative value");
        this.maxLength = maxLength;
    }

    /**
     * Checks whether the given text is not longer than the maximum length that is associated with this condition.
     *
     * @param text The checked text.
     * @return <code>true</code> if the given text is not longer than the maximum length associated with this condition,
     *         <code>false</code> otherwise.
     */
    protected boolean checkString(String text) {
        return text.length() <= maxLength;
    }

    //============================================= Setter/Getter ===================================================

    /**
     * Returns the maximum length that is associated with this condition.
     *
     * @return The maximum length that is associated with this condition.
     */
    public int getMaxLength() {
        return maxLength;
    }

}
