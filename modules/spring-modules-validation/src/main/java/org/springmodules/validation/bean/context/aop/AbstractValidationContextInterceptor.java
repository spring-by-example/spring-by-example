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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springmodules.validation.bean.context.ValidationContext;
import org.springmodules.validation.bean.context.ValidationContextHolder;
import org.springmodules.validation.bean.context.ValidationContextUtils;

/**
 * A parent class to any around validation context advice that intercepts method invocations.
 * <p/>
 * This advice is based on {@link org.springmodules.validation.bean.context.DefaultValidationContext} and relies on
 * the {@link #getValidationContextTokens(org.aopalliance.intercept.MethodInvocation)} implementation to supply the
 * appropiate context.
 *
 * @author Uri Boness
 */
public abstract class AbstractValidationContextInterceptor implements MethodInterceptor {

    private boolean extendExistingContext = false;

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        String[] contextTokens = getValidationContextTokens(methodInvocation);

        ValidationContext originalContext = ValidationContextHolder.getValidationContext();
        if (originalContext != null && extendExistingContext) {
            ValidationContextUtils.extendContext(contextTokens);
        } else {
            ValidationContextUtils.setContext(contextTokens);
        }

        Object result = methodInvocation.proceed();

        // setting the validation context back to the original one (the one that was set before this advice was called)
        if (originalContext != null) {
            ValidationContextHolder.setValidationContext(originalContext);
        } else {
            ValidationContextUtils.clearContext();
        }

        return result;

    }

    /**
     * Will be implemented by all subclasses to determine what validation context tokens will be supported by the
     * validation context associated with the given method invocation.
     *
     * @param methodInvocation The given method invocation.
     * @return The tokens supported by the validation context associated with the given method invocation.
     */
    protected abstract String[] getValidationContextTokens(MethodInvocation methodInvocation);


    //============================================== Setter/Getter =====================================================

    /**
     * Determines whether this interceptor should extend the already exsiting context (if one already exists). If so,
     * the tokens returned by {@link #getValidationContextTokens(org.aopalliance.intercept.MethodInvocation)} will be
     * supported along with all tokens supported by the already existing validation context.
     * <p/>
     * By default this advice does <b>not</b> extend the already existing context, instead it creates and sets a new one.
     *
     * @param extendExistingContext Determines whether this interceptor should extend the already exsiting context
     *        (if one already exists).
     */
    public void setExtendExistingContext(boolean extendExistingContext) {
        this.extendExistingContext = extendExistingContext;
    }
}
