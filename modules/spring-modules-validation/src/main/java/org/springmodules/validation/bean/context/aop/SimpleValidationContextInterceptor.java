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

package org.springmodules.validation.bean.context.aop;

import org.aopalliance.intercept.MethodInvocation;

/**
 * A simple implementaion of {@link AbstractValidationContextInterceptor} that is configured with a list of tokens which
 * are always returned regardless of the specific method invocation.
 *
 * @author Uri Boness
 */
public class SimpleValidationContextInterceptor extends AbstractValidationContextInterceptor {

    private String[] tokens;

    /**
     * @see AbstractValidationContextInterceptor#getValidationContextTokens(org.aopalliance.intercept.MethodInvocation)
     */
    protected String[] getValidationContextTokens(MethodInvocation methodInvocation) {
        return tokens;
    }


    //============================================== Setter/Getter =====================================================

    /**
     * Returns the validation context tokens that are always returned by this advice.
     *
     * @return The validation context tokens that are always returned by this advice.
     */
    public String[] getTokens() {
        return tokens;
    }

    /**
     * Sets the validation context tokens to be returned.
     *
     * @param tokens The validation context tokens to be returned.
     */
    public void setTokens(String[] tokens) {
        this.tokens = tokens;
    }

}
