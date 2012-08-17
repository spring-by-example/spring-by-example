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

import java.util.Collection;

import org.springframework.util.Assert;
import org.springmodules.validation.util.condition.AbstractCondition;
import org.springmodules.validation.util.condition.Condition;

/**
 * A base class for all compound conditions.
 *
 * @author Uri Boness
 */
public abstract class AbstractCompoundCondition extends AbstractCondition {

    private Condition[] conditions;

    /**
     * Constructs a new AbstractCompoundCondition with the given array of conditions.
     *
     * @param conditions The conditions this condition is compound from.
     */
    public AbstractCompoundCondition(Condition[] conditions) {
        Assert.notNull(conditions, "Compound condition cannot accept null as conditions");
        this.conditions = conditions;
    }

    /**
     * Constructs a new AbstractCompoundCondition with the given collection of conditions.
     *
     * @param conditions The conditions this condition is compound from.
     */
    public AbstractCompoundCondition(Collection conditions) {
        Assert.notNull(conditions, "Compound condition cannot accept null as conditions");
        this.conditions = (Condition[]) conditions.toArray(new Condition[conditions.size()]);
    }

    //=============================================== Setter/Getter ====================================================

    /**
     * Returns the conditions this condition is compound from.
     *
     * @return The conditions this condition is compound from.
     */
    public Condition[] getConditions() {
        return conditions;
    }

}
