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

package org.springmodules.validation.util.condition.common;

import org.springmodules.validation.util.condition.AbstractCondition;

/**
 * A condition that checks whether the checked object is <code>null</code>.
 *
 * @author Uri Boness
 */
public class IsNullCondition extends AbstractCondition {

    /**
     * Checks whether the given object is <code>null</code>.
     *
     * @param object The object to be checked.
     * @return <code>true</code> if the given object is <code>null</code>, <code>false</code> otherwise.
     */
    public boolean doCheck(Object object) {
        return object == null;
    }

}
