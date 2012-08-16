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

package org.springmodules.validation.bean.rule.resolver;

/**
 * An {@link org.springmodules.validation.bean.rule.resolver.ErrorArgumentsResolver} that is statically pre-configured with the error arguments, that is, the
 * arguments are not dependent on the validated object.
 *
 * @author Uri Boness
 */
public class StaticErrorArgumentsResolver implements ErrorArgumentsResolver {

    private Object[] arguments;

    /**
     * Constructs a new StaticErrorArgumentsResolver with no arguments.
     */
    public StaticErrorArgumentsResolver() {
        this(new Object[0]);
    }

    /**
     * Constructs a new StaticErrorArgumentsResolver with given static arguments.
     *
     * @param arguments The given static arguments.
     */
    public StaticErrorArgumentsResolver(Object[] arguments) {
        this.arguments = arguments;
    }

    /**
     * Returns the arguments that are configured in this resolver.
     *
     * @see ErrorArgumentsResolver#resolveArguments(Object)
     */
    public Object[] resolveArguments(Object obj) {
        return arguments;
    }

    //=============================================== Setter/Getter ====================================================

    /**
     * Sets the static arguments to be returned by this resolver.
     *
     * @param arguments The static arguments to be returned by this resolver.
     */
    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }
}
