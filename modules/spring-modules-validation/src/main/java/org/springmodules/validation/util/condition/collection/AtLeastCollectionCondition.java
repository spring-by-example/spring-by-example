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

package org.springmodules.validation.util.condition.collection;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.util.Assert;
import org.springmodules.validation.util.condition.Condition;

/**
 * An {@link AbstractCollectionCondition} implementation that checks whether at least X elements in a collection
 * or array adhere to a specific condition.
 *
 * @author Uri Boness
 */
public class AtLeastCollectionCondition extends AbstractCollectionElementCondition {

    private int count;

    /**
     * Constructs a new AtLeastCollectionCondition with a given element condition.
     *
     * @param elementCondition The condition to be checked on the collection elements.
     */
    public AtLeastCollectionCondition(Condition elementCondition, int count) {
        super(elementCondition);
        Assert.isTrue(count >= 0, "Count cannot be negative");
        this.count = count;
    }

    /**
     * Checks whether at least X objects in the given array adhere to the associated condition. X is determined
     * by the <code>getCount()</code> method call.
     *
     * @param array The array to be checked.
     * @return <code>true</code> if at least X objects in the given array adhere to the associated element condition,
     *         <code>false</code> otherwise.
     */
    protected boolean checkArray(Object array) {
        int counter = 0;
        for (int i = 0; i < Array.getLength(array); i++) {
            if (getElementCondition().check(Array.get(array, i))) {
                counter++;
            }
        }
        return counter >= getCount();
    }

    /**
     * Checks whether at least X elements in the given collection adhere to the associated condition. X is
     * determined by the <code>getCount()</code> method call.
     *
     * @param collection The collection to be checked.
     * @return <code>true</code> if at least X elements in the given collection adhere to the associated element
     *         condition, <code>false</code> otherwise.
     */
    protected boolean checkCollection(Collection collection) {
        int counter = 0;
        for (Iterator iter = collection.iterator(); iter.hasNext();) {
            if (getElementCondition().check(iter.next())) {
                counter++;
            }
        }
        return counter >= getCount();
    }

    //=============================================== Setter/Getter ====================================================

    /**
     * Returns how many element must adhere to the associated condition in order for this condition to "pass".
     *
     * @return How many element must adhere to the associated condition in order for this condition to "pass".
     */
    public int getCount() {
        return count;
    }

}
