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
 * Represents a context of validation. In every application there can be different validation contexts in which different
 * validation rules apply. This context represent those validation contexts. A validation contexts supports specific
 * validation context tokens. A context token are strings that bind validation rules to specific tokens.
 *
 * @author Uri Boness
 */
public interface ValidationContext {

    /**
     * Returns whether this validation context supports the given validation tokens.
     *
     * @param tokens The given validation tokens.
     * @return <code>true</code> if this context supports the given validation tokens, <code>false</code> otherwise.
     */
    boolean supportsTokens(String[] tokens);

}
