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

import org.springmodules.validation.util.condition.Condition;

/**
 * A compound condition that represent a logical and of all its associated conditions.
 *
 * @author Uri Boness
 */
public class AndCondition extends AbstractCompoundCondition {

    /**
     * Constructs a new AndCondition with the given condition array.
     *
     * @param conditions The conditions this condition is compound from.
     */
    public AndCondition(Condition[] conditions) {
        super(conditions);
    }

    /**
     * Constructs a new AndCondition with the given condition collection.
     *
     * @param conditions The conditions this condition is compound from.
     */
    public AndCondition(Collection conditions) {
        super(conditions);
    }

    /**
     * Checks whether all the associated conditions of this condition return <code>true</code>.
     *
     * @param object The object to be checked.
     * @return <code>true</code> if all the associated conditions return <code>true</code>, <code>false</code> otherwise.
     */
    public boolean doCheck(Object object) {
        Condition[] conditions = getConditions();
        for (int i = 0; i < conditions.length; i++) {
            if (!conditions[i].check(object)) {
                return false;
            }
        }
        return true;
    }

}
