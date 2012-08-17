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

import org.springframework.util.Assert;

/**
 * A holder to hold thread bound validation contexts. Calling {@link #setValidationContext(ValidationContext)} and then
 * {@link #getValidationContext()} by the same thread garenteas that the second call will return the same runtime
 * {@link ValidationContext} instance as was set by the first call.
 *
 * @author Uri Boness
 */
public class ValidationContextHolder {

    private static ThreadLocal contextHolder = new ThreadLocal();

    /**
     * Returns the {@link ValidationContext} held by this holder, or <code>null</code> if nothing is held.
     *
     * @return The {@link ValidationContext} held by this holder.
     */
    public static ValidationContext getValidationContext() {
        return (ValidationContext)contextHolder.get();
    }

    /**
     * Sets the ${ValidationContext} to be held by this holder.
     *
     * @param context
     */
    public static void setValidationContext(ValidationContext context) {
        Assert.notNull(context,
            "Cannot set a null validation context. To clear this holder please call clearContext() instead");
        contextHolder.set(context);
    }

    /**
     * Clears this holder. After calling the method this holder will no longer hold any validation context.     *
     */
    public static void clearContext() {
        contextHolder.remove();
    }

}
