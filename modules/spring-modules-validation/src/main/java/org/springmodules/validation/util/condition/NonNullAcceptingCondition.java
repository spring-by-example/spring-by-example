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

import org.springframework.util.Assert;

/**
 * A base class for all conditions that cannot check <code>null</code> values.
 *
 * @author Uri Boness
 */
public abstract class NonNullAcceptingCondition extends AbstractCondition {

    /**
     * Checks whether the checked object is <code>null</code>.
     *
     * @param object The checked object.
     * @throws IllegalArgumentException when the checked object is <code>null</code>.
     */
    protected void beforeObjectChecked(Object object) {
        Assert.notNull(object, getClass().getName() + " cannot check 'null' values");
    }

}
