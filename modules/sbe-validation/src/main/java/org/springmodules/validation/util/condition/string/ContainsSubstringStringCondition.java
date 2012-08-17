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
 * An {@link AbstractStringCondition} implementation that checks whether the given strings contain a specific
 * sub-string.
 *
 * @author Uri Boness
 */
public class ContainsSubstringStringCondition extends AbstractStringCondition {

    private String substring;

    /**
     * Constructs a new ContainsSubstringStringCondition with a given sub-string.
     *
     * @param substring The sub-string this condition will look for within the checked strings.
     */
    public ContainsSubstringStringCondition(String substring) {
        this.substring = substring;
    }

    /**
     * Checks whether the given text contains the sub-string associated with this condition.
     *
     * @param text The string to be checked.
     * @return <code>true</code> if the given text contains the sub-string, <code>false</code> otherwise.
     */
    protected boolean checkString(String text) {
        return text.indexOf(substring) > -1;
    }

    //============================================= Setter/Getter ===================================================

    /**
     * Returns the sub-string that is associated with this condition.
     *
     * @return The sub-string that is associated with this condition.
     */
    public String getSubstring() {
        return substring;
    }

}
