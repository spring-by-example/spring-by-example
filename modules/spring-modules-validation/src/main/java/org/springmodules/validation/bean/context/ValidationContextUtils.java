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

/**
 * @author Uri Boness
 */
public class ValidationContextUtils {

    public static void setContext(String supportedToken) {
        ValidationContextHolder.setValidationContext(new DefaultValidationContext(supportedToken));
    }

    public static void setContext(String[] supportedTokens) {
        ValidationContextHolder.setValidationContext(new DefaultValidationContext(supportedTokens));
    }

    public static ValidationContext extendContext(String[] additionalTokens) {
        ValidationContext context = ValidationContextHolder.getValidationContext();
        ValidationContext newContext = new ExtendingValidationContext(additionalTokens, context);
        ValidationContextHolder.setValidationContext(newContext);
        return context;
    }

    public static void clearContext() {
        ValidationContextHolder.clearContext();
    }

    public static boolean tokensSupportedByCurrentContext(String[] tokens) {
        if (tokens == null) {
            return true;
        }

        ValidationContext context = ValidationContextHolder.getValidationContext();

        if (context == null) {
            return false;
        }

        return context.supportsTokens(tokens);
    }
}
