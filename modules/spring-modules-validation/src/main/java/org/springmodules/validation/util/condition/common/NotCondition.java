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

import org.springframework.util.Assert;
import org.springmodules.validation.util.condition.AbstractCondition;
import org.springmodules.validation.util.condition.Condition;

/**
 * A condition that represents a logical <code>NOT</code> of another condition.
 *
 * @author Uri Boness
 */
public class NotCondition extends AbstractCondition {

    private Condition condition;

    /**
     * Constructs a new NotCondition with the condition to reverse.
     *
     * @param condition The condition to reverse.
     */
    public NotCondition(Condition condition) {
        Assert.notNull(condition, "Condition cannot be null");
        this.condition = condition;
    }

    /**
     * Checks and reverses the associated condition on the given object.
     *
     * @param object The object to be checked.
     * @return <code>true</code> if the associated condition returns <code>false</code>, <code>false</code> otherwise.
     */
    public boolean doCheck(Object object) {
        return !condition.check(object);
    }

    //============================================= Setter/Getter ===================================================

    /**
     * Returns the reverse condition associated with this condition.
     *
     * @return The reverse condition associated with this condition.
     */
    public Condition getCondition() {
        return condition;
    }

}
