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

package org.springmodules.validation.util.condition;

import org.springframework.core.NestedRuntimeException;

/**
 * Can be thrown by any condition at execution.
 *
 * @author Uri Boness
 */
public class ConditionException extends NestedRuntimeException {

    public ConditionException(String msg) {
        super(msg);
    }

    public ConditionException(String msg, Throwable ex) {
        super(msg, ex);
    }

}
