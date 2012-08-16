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

package org.springmodules.validation.bean.context;

import org.apache.commons.lang.ArrayUtils;

/**
 * A simple and default implemenation of the {@link ValidationContext} interface.
 *
 * @author Uri Boness
 */
public class DefaultValidationContext implements ValidationContext {

    private String[] tokens;

    /**
     * Construsts a new DefaultValidationContext with a given supported validation token.
     *
     * @param token The validation token supported by this context.
     */
    public DefaultValidationContext(String token) {
        this(new String[] { token });
    }

    /**
     * Construsts a new DefaultValidationContext with given supported validation tokens.
     *
     * @param tokens The validation tokens supported by this context.
     */
    public DefaultValidationContext(String[] tokens) {
        this.tokens = tokens;
    }

    /**
     * @see #supportsTokens(String[])
     */
    public boolean supportsTokens(String token) {
        return supportsTokens(new String[] { token });
    }

    /**
     * @see ValidationContext#supportsTokens(String[])
     */
    public boolean supportsTokens(String[] tokens) {
        for (int i=0; i<tokens.length; i++) {
            if (ArrayUtils.contains(this.tokens, tokens[i])) {
                return true;
            }
        }
        return false;
    }


    //============================================== Setter/Getter =====================================================

    public String[] getTokens() {
        return tokens;
    }

}
